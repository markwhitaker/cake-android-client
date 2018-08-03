package com.waracle.androidtest.data;

import android.util.Log;

import com.waracle.androidtest.utils.HttpUtils;
import com.waracle.androidtest.utils.StreamUtils;
import com.waracle.androidtest.model.Cake;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CakeDataLoader extends DataLoader<List<Cake>> {

    private static final String TAG = "CakeDataLoader";

    public interface Listener extends DataLoader.Listener<List<Cake>> {
    }

    public CakeDataLoader(final Listener listener) {
        super(listener);
    }

    @Override
    protected List<Cake> loadData(URL url) {

        List<Cake> cakes = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            final int contentLength = HttpUtils.getContentLength(urlConnection);
            final byte[] bytes = StreamUtils.readAllBytes(inputStream, contentLength);

            // Read in charset of HTTP content.
            final String charset = HttpUtils.getCharset(urlConnection);

            // Convert byte array to appropriate encoded string.
            final String jsonText = new String(bytes, charset);

            // Read string as JSON.
            final JSONArray jsonArray = new JSONArray(jsonText);

            // Build model objects from the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                cakes.add(new Cake(jsonArray.getJSONObject(i)));
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            cakes = null;
        }
        finally {
            StreamUtils.close(inputStream);

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return cakes;
    }
}
