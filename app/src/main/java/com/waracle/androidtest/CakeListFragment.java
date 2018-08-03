package com.waracle.androidtest;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.data.CakeDataLoader;
import com.waracle.androidtest.model.Cake;

import java.util.List;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class CakeListFragment extends ListFragment implements CakeDataLoader.Listener {

    private ListView listView;
    private CakeListAdapter adapter;
    private View errorMessageView;
    private View progressBarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = rootView.findViewById(android.R.id.list);
        errorMessageView = rootView.findViewById(R.id.error_message);
        progressBarView = rootView.findViewById(R.id.progress_bar);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create and set the list adapter.
        adapter = new CakeListAdapter(requireContext());
        listView.setAdapter(adapter);

        loadData();
    }

    public void loadData() {
        adapter.clearCakes();
        switchToView(progressBarView);
        new CakeDataLoader(this).load(BuildConfig.DATA_URL);
    }

    @Override
    public void onDataLoaded(List<Cake> cakes) {
        switchToView(listView);
        adapter.setCakes(cakes);
    }

    @Override
    public void onDataError() {
        switchToView(errorMessageView);
    }

    private void switchToView(final View view) {
        listView.setVisibility(listView == view ? View.VISIBLE : View.GONE);
        errorMessageView.setVisibility(errorMessageView == view ? View.VISIBLE : View.GONE);
        progressBarView.setVisibility(progressBarView == view ? View.VISIBLE : View.GONE);
    }
}
