package com.waracle.androidtest.cakes.view;

import android.graphics.Bitmap;
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

import java.util.List;

public class CakeListFragment extends Fragment implements CakeContracts.ListView {

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

        if (presenter != null) {
            // Create and set the list adapter.
            adapter = new CakeListAdapter(requireContext(), presenter);
            listView.setAdapter(adapter);

            presenter.onViewReady();
        }
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

    @Override
    public void showCakeThumbnailImage(final String imageUrl, final Bitmap bitmap) {
        adapter.showCakeThumbnailImage(imageUrl, bitmap);
    }

    private void switchToView(final View view) {
        listView.setVisibility(listView == view ? View.VISIBLE : View.GONE);
        errorMessageView.setVisibility(errorMessageView == view ? View.VISIBLE : View.GONE);
        progressBarView.setVisibility(progressBarView == view ? View.VISIBLE : View.GONE);
    }
}
