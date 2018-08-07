package com.waracle.androidtest.cakes;

import android.graphics.Bitmap;

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
        void showCakeThumbnailImage(String imageUrl, Bitmap bitmap);
    }

    public interface ListPresenter {
        void onViewReady();
        void refresh();
        void loadThumbnailImage(String imageUrl);
    }

    private CakeContracts(){
    }
}
