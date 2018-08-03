package com.waracle.androidtest;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Riad on 20/05/2015.
 */
public final class StreamUtils {

    private static final String TAG = "StreamUtils";
    private static final int BUFFER_SIZE = 4096;

    public static byte[] readFully(@NonNull InputStream stream, int contentLength) throws IOException {
        // Read in stream of bytes
        byte[] bytes = new byte[contentLength];
        int totalBytesRead = 0;
        while (totalBytesRead < contentLength) {
            final int bytesRead = stream.read(bytes, totalBytesRead, contentLength - totalBytesRead);
            totalBytesRead += bytesRead;
        }
        StreamUtils.close(stream);
        return bytes;
    }

    public static byte[] readFully(@NonNull InputStream stream) throws IOException {
        // Read in stream of bytes
        final byte[] buffer = new byte[BUFFER_SIZE];
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int totalBytesRead = 0;

        while (true) {
            int bytesRead = stream.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            byteStream.write(buffer, totalBytesRead, bytesRead);
            totalBytesRead += bytesRead;
        }

        final byte[] bytes = byteStream.toByteArray();
        StreamUtils.close(byteStream);

        return bytes;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private StreamUtils()
    {
    }
}
