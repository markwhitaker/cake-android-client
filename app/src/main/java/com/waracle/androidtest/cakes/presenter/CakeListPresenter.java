package com.waracle.androidtest.cakes.presenter;

import android.graphics.Bitmap;

import com.waracle.androidtest.BuildConfig;
import com.waracle.androidtest.cakes.CakeContracts;
import com.waracle.androidtest.cakes.model.Cake;

import java.util.List;


public class CakeListPresenter implements CakeContracts.ListPresenter {

    private final CakeContracts.ListView view;

    public CakeListPresenter(final CakeContracts.ListView view) {
        this.view = view;
    }

    @Override
    public void onViewReady() {
        refresh();
    }

    @Override
    public void refresh() {
        view.showCakesLoading();

        final CakeDataLoader.Listener listener = new CakeDataLoader.Listener() {
            @Override
            public void onDataLoaded(final String requestUrl, final List<Cake> cakes) {
                view.showCakes(cakes);
            }

            @Override
            public void onDataError() {
                view.showCakesLoadError();
            }
        };

        new CakeDataLoader(BuildConfig.DATA_URL, listener).load();
    }

    @Override
    public void loadThumbnailImage(final String imageUrl) {
        final ImageLoader.Listener listener = new ImageLoader.Listener() {
            @Override
            public void onDataLoaded(final String imageUrl, final Bitmap bitmap) {
                view.showCakeThumbnailImage(imageUrl, bitmap);
            }

            @Override
            public void onDataError() {
            }
        };

        new ImageLoader(imageUrl, listener).load();
    }
}
