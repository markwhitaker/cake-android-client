package com.waracle.androidtest.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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

    public ImageLoader(@NonNull final String url, @NonNull final Listener listener) {
        super(url, listener);
    }

    @Override
    protected Bitmap loadData(@NonNull final URL url) {
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
