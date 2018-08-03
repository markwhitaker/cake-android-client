package com.waracle.androidtest.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.waracle.androidtest.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader extends DataLoader<Bitmap> {

    private static final String TAG = "ImageLoader";

    public interface Listener extends DataLoader.Listener<Bitmap> {
    }

    public ImageLoader(Listener listener) {
        super(listener);
    }

    @Override
    protected Bitmap loadData(URL url) {

        Bitmap bitmap;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection)url.openConnection();
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            final byte[] data = StreamUtils.readUnknownFully(inputStream);

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            bitmap = null;
        }
        finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            if (connection != null) {
                connection.disconnect();
            }
        }

        return bitmap;
    }
}