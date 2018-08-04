package com.waracle.androidtest.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {

    private static final String TAG = "HttpUtils";
    private static final String HEADER_NAME_CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_NAME_LOCATION = "Location";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(\\S+)");
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static final String CHARSET_UTF8 = "UTF-8";

    private HttpURLConnection connection;
    private int contentLength;
    private String charset;

    public byte[] getBytes(final URL url) {

        byte[] data = null;
        InputStream inputStream = null;

        try {
            openConnection(url);
            inputStream = new BufferedInputStream(connection.getInputStream());
            data = StreamUtils.readAllBytes(inputStream, contentLength);
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        finally {
            StreamUtils.close(inputStream);
        }

        return data;
    }

    public String getString(final URL url) {

        final byte[] data = getBytes(url);
        String string = "";
        try {
            string = new String(data, charset);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        }
        return string;
    }

    private void openConnection(final URL url) throws IOException {

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
}
