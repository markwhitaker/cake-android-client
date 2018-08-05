package com.waracle.androidtest.data;

import android.util.Log;

import com.waracle.androidtest.model.Cake;
import com.waracle.androidtest.utils.HttpHelper;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTask-derived class used for loading the cake JSON data on a background thread
 */
public class CakeDataLoader extends DataLoader<List<Cake>> {

    private static final String TAG = "CakeDataLoader";

    public interface Listener extends DataLoader.Listener<List<Cake>> {
    }

    public CakeDataLoader(final Listener listener) {
        super(listener);
    }

    @Override
    protected List<Cake> loadData(URL url) {

        final List<Cake> cakes = new ArrayList<>();
        final HttpHelper httpHelper = new HttpHelper();
        final String jsonText = httpHelper.getString(url);

        try {
            // Read string as JSON.
            final JSONArray jsonArray = new JSONArray(jsonText);

            // Build model objects from the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                cakes.add(new Cake(jsonArray.getJSONObject(i)));
            }

            return cakes;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}
