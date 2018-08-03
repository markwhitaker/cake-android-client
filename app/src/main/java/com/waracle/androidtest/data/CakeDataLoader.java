package com.waracle.androidtest.data;

import android.util.Log;

import com.waracle.androidtest.MainActivity;
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

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            final InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Can you think of a way to improve the performance of loading data
            // using HTTP headers???

            // Also, Do you trust any utils thrown your way????

            final byte[] bytes = StreamUtils.readUnknownFully(in);

            // Read in charset of HTTP content.
            final String charset = MainActivity.PlaceholderFragment.parseCharset(urlConnection.getRequestProperty("Content-Type"));

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
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonArray;
    }
}
