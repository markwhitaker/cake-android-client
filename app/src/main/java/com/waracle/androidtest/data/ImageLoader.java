package com.waracle.androidtest.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    protected @Nullable Bitmap loadData(@NonNull final URL url) {
        // Try to load the image from the cache first
        Bitmap bitmap = bitmapCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }

        final HttpHelper httpHelper = new HttpHelper();
        final byte[] bytes = httpHelper.getBytes(url);
        if (bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // Add the bitmap to the cache
            if (bitmap != null) {
                bitmapCache.put(url, bitmap);
            }
        }

        return bitmap;
    }
}
