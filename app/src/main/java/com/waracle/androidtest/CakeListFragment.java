package com.waracle.androidtest;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.data.CakeDataLoader;

import org.json.JSONArray;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class CakeListFragment extends ListFragment {

    private ListView mListView;
    private CakeListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = rootView.findViewById(android.R.id.list);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create and set the list adapter.
        mAdapter = new CakeListAdapter(requireContext());
        mListView.setAdapter(mAdapter);

        // Load data from net.
        final CakeDataLoader.Listener dataListener = new CakeDataLoader.Listener() {
            @Override
            public void onDataLoaded(JSONArray jsonArray) {
                mAdapter.setItems(jsonArray);
            }

            @Override
            public void onDataError() {
                // TODO: handle data error
            }
        };

        new CakeDataLoader(dataListener).load(BuildConfig.DATA_URL);
    }
}
