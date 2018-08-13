package com.waracle.androidtest.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to issue HTTP GET requests and handle complexities like redirects.
 */
public class HttpHelper {

    private static final String TAG = "HttpHelper";
    private static final String HEADER_NAME_CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_NAME_LOCATION = "Location";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(\\S+)");
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final int BUFFER_SIZE = 4096;

    private HttpURLConnection connection;
    private int contentLength;
    private String charset;

    public @Nullable byte[] getBytes(@NonNull final URL url) {

        byte[] data = null;
        InputStream inputStream = null;

        try {
            openConnection(url);
            inputStream = new BufferedInputStream(connection.getInputStream());

            if (contentLength > 0) {
                data = readAllBytes(inputStream, contentLength);
            } else {
                data = readAllBytes(inputStream);
            }
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        finally {
            close(inputStream);
        }

        return data;
    }

    public @Nullable String getString(@NonNull final URL url) {

        final byte[] bytes = getBytes(url);
        String string = null;
        if (bytes != null) {
            try {
                string = new String(bytes, charset);
            }
            catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return string;
    }

    private void openConnection(@NonNull final URL url) throws IOException {

        URL redirectUrl = url;

        // Resolve redirects (e.g. HTTP -> HTTPS)
        while (connection == null) {

            connection = (HttpURLConnection) redirectUrl.openConnection();
            final int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String urlLocation = connection.getHeaderField(HEADER_NAME_LOCATION);
                urlLocation = URLDecoder.decode(urlLocation, CHARSET_UTF8);
                redirectUrl = new URL(redirectUrl, urlLocation);
                connection.disconnect();
                connection = null;
            }
        }

        readContentLength();
        readCharset();
    }

    private void readContentLength() {
        final String contentLengthString = connection.getHeaderField(HEADER_NAME_CONTENT_LENGTH);
        try {
            contentLength = Integer.valueOf(contentLengthString);
        } catch (NumberFormatException e) {
            contentLength = 0;
        }
    }

    private void readCharset() {
        final String contentType = connection.getHeaderField(HEADER_NAME_CONTENT_TYPE);
        if (contentType != null) {
            final Matcher matcher = CHARSET_PATTERN.matcher(contentType);
            charset = matcher.find() ? matcher.group(1) : DEFAULT_CHARSET;
        }
    }

    /**
     * Read all bytes from a stream with a known size
     *
     * @param stream        Stream to read
     * @param sizeInBytes   Number of bytes to read
     * @return Array of bytes read from the stream
     * @throws IOException An error occurred reading the stream
     */
    private @NonNull byte[] readAllBytes(@NonNull final InputStream stream, final int sizeInBytes) throws IOException {
        // Read in stream of bytes
        byte[] bytes = new byte[sizeInBytes];
        int totalBytesRead = 0;
        while (totalBytesRead < sizeInBytes) {
            final int bytesRead = stream.read(bytes, totalBytesRead, sizeInBytes - totalBytesRead);
            totalBytesRead += bytesRead;
        }
        close(stream);
        return bytes;
    }

    /**
     * Read all bytes from a stream whose size is unknown
     *
     * @param stream    Stream to read
     * @return Array of bytes read from the stream
     * @throws IOException An error occurred reading the stream
     */
    private @NonNull byte[] readAllBytes(@NonNull final InputStream stream) throws IOException {
        // Read in stream of bytes
        final byte[] buffer = new byte[BUFFER_SIZE];
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        while (true) {
            int bytesRead = stream.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            byteStream.write(buffer, 0, bytesRead);
        }

        final byte[] bytes = byteStream.toByteArray();
        close(byteStream);

        return bytes;
    }

    private void close(@Nullable final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
