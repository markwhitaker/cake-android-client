package com.waracle.androidtest.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.waracle.androidtest.utils.HttpHelper;

import java.net.URL;

/**
 * AsyncTask-derived class used for loading thumbnail images on a background thread
 */
public class ImageLoader extends DataLoader<Bitmap> {

    private static final LruCache<URL, Bitmap> bitmapCache = new LruCache<>(20);

    public interface Listener extends DataLoader.Listener<Bitmap> {
    }

    public ImageLoader(Listener listener) {
        super(listener);
    }

    @Override
    protected Bitmap loadData(URL url) {

        // Try to load the image from the cache first
        Bitmap bitmap = bitmapCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }

        final HttpHelper httpHelper = new HttpHelper();
        final byte[] data = httpHelper.getBytes(url);
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        // Add the bitmap to the cache
        if (bitmap != null) {
            bitmapCache.put(url, bitmap);
        }

        return bitmap;
    }
}
