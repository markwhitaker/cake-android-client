package com.waracle.androidtest.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Riad on 20/05/2015.
 */
class StreamUtils {

    private static final String TAG = "StreamUtils";
    private static final int BUFFER_SIZE = 4096;

    /**
     * Read all bytes from a stream
     *
     * @param stream        Stream to read
     * @param sizeInBytes   Size of the stream in bytes, or 0 is size is not known
     * @return Array of bytes read from the stream
     * @throws IOException An error occurred reading the stream
     */
    static byte[] readAllBytes(@NonNull final InputStream stream, final int sizeInBytes) throws IOException {
        if (sizeInBytes > 0) {
            return readAll(stream, sizeInBytes);
        } else {
            return readAll(stream);
        }
    }

    static void close(@Nullable final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
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
    private static byte[] readAll(@NonNull final InputStream stream, final int sizeInBytes) throws IOException {
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
    private static byte[] readAll(@NonNull final InputStream stream) throws IOException {
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

    private StreamUtils()
    {
    }
}
