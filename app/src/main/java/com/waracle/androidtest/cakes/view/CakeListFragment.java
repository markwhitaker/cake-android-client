package com.waracle.androidtest.cakes.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.cakes.CakeContracts;
import com.waracle.androidtest.cakes.model.Cake;
import com.waracle.androidtest.cakes.presenter.CakeDataLoader;

import java.util.List;

public class CakeListFragment extends Fragment implements CakeDataLoader.Listener, CakeContracts.ListView {

    private CakeContracts.ListPresenter presenter;
    private ListView listView;
    private CakeListAdapter adapter;
    private View errorMessageView;
    private View progressBarView;


    public void setPresenter(CakeContracts.ListPresenter presenter) {
        this.presenter = presenter;
    }

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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create and set the list adapter.
        adapter = new CakeListAdapter(requireContext());
        listView.setAdapter(adapter);

        if (presenter != null) {
            presenter.onViewReady();
        }
    }

    @Override
    public void onDataLoaded(final String requestUrl, final List<Cake> cakes) {
    }

    @Override
    public void onDataError() {
    }

    @Override
    public void showCakesLoading() {
        adapter.clearCakes();
        switchToView(progressBarView);
    }

    @Override
    public void showCakes(List<Cake> cakes) {
        switchToView(listView);
        adapter.setCakes(cakes);
    }

    @Override
    public void showCakesLoadError() {
        switchToView(errorMessageView);
    }

    private void switchToView(final View view) {
        listView.setVisibility(listView == view ? View.VISIBLE : View.GONE);
        errorMessageView.setVisibility(errorMessageView == view ? View.VISIBLE : View.GONE);
        progressBarView.setVisibility(progressBarView == view ? View.VISIBLE : View.GONE);
    }
}
