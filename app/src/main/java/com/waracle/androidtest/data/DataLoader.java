package com.waracle.androidtest.data;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstract base class derived from AsyncTask, used to load some data in the background and inform
 * a listener interface when the data loads or fails.
 *
 * @param <T> Type of data this loader returns
 */
abstract class DataLoader<T> extends AsyncTask<Void, Void, T> {

    interface Listener<T> {
        void onDataLoaded(String requestUrl, T data);
        void onDataError();
    }

    private final String url;
    private final Listener listener;

    DataLoader(@NonNull final String url, @NonNull final Listener listener) {
        this.url = url;
        this.listener = listener;
    }

    public void load()
    {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected final @Nullable T doInBackground(@Nullable final Void... params) {
        try {
            return loadData(new URL(url));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter is not a valid URL: " + url);
        }
    }

    @Override
    protected final void onPostExecute(@Nullable final T data) {
        if (isCancelled()) {
            return;
        }

        if (data == null) {
            listener.onDataError();
        }
        else {
            //noinspection unchecked
            listener.onDataLoaded(url, data);
        }
    }

    abstract protected T loadData(final URL url);
}
