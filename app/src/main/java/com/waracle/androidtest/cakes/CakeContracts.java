package com.waracle.androidtest.cakes;

import com.waracle.androidtest.cakes.model.Cake;

import java.util.List;

/**
 * MVP interfaces for the Cakes feature
 */
public final class CakeContracts {

    public interface ListView {
        void showCakesLoading();
        void showCakes(List<Cake> cakes);
        void showCakesLoadError();
    }

    public interface ListPresenter {
        void onViewReady();
        void refresh();
    }

    private CakeContracts(){
    }
}
