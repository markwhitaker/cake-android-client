package com.waracle.androidtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.data.CakeDataLoader;
import com.waracle.androidtest.model.Cake;

import java.util.List;

public class CakeListFragment extends Fragment implements CakeDataLoader.Listener {

    private ListView listView;
    private CakeListAdapter adapter;
    private View errorMessageView;
    private View progressBarView;

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
