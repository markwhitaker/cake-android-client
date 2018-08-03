package com.waracle.androidtest.data;

import android.util.Log;

import com.waracle.androidtest.HttpUtils;
import com.waracle.androidtest.StreamUtils;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CakeDataLoader extends DataLoader<JSONArray> {

    private static final String TAG = "CakeDataLoader";

    public interface Listener extends DataLoader.Listener<JSONArray> {
    }

    public CakeDataLoader(final Listener listener) {
        super(listener);
    }

    @Override
    protected JSONArray loadData(URL url) {

        JSONArray jsonArray;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            final int contentLength = HttpUtils.getContentLength(urlConnection);
            final byte[] bytes = (contentLength == HttpUtils.UNAVAILABLE_CONTENT_LENGTH)
                    ? StreamUtils.readFully(inputStream)
                    : StreamUtils.readFully(inputStream, contentLength);

            // Read in charset of HTTP content.
            final String charset = HttpUtils.getCharset(urlConnection);

            // Convert byte array to appropriate encoded string.
            final String jsonText = new String(bytes, charset);

            // Read string as JSON.
            jsonArray = new JSONArray(jsonText);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            jsonArray = null;
        }
        finally {
            StreamUtils.close(inputStream);

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonArray;
    }
}
