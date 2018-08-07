package com.waracle.androidtest.cakes.presenter;

import com.waracle.androidtest.BuildConfig;
import com.waracle.androidtest.cakes.CakeContracts;
import com.waracle.androidtest.cakes.model.Cake;

import java.util.List;


public class CakeListPresenter implements CakeDataLoader.Listener, CakeContracts.ListPresenter {

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
        new CakeDataLoader(BuildConfig.DATA_URL, this).load();
    }

    @Override
    public void onDataLoaded(final String requestUrl, final List<Cake> cakes) {
        view.showCakes(cakes);
    }

    @Override
    public void onDataError() {
        view.showCakesLoadError();
    }
}
