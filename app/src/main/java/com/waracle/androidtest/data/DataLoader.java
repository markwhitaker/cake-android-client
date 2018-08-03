package com.waracle.androidtest.data;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

abstract class DataLoader<T> extends AsyncTask<String, Void, T> {

    interface Listener<T> {
        void onDataLoaded(T data);
        void onDataError();
    }

    private final Listener listener;

    DataLoader(final Listener listener) {
        this.listener = listener;
    }

    public void load(final String url)
    {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    @Override
    protected final @Nullable T doInBackground(String... params) {

        if (params.length == 0 || TextUtils.isEmpty(params[0])) {
            throw new IllegalArgumentException("You must pass the data's URL as the first parameter");
        }

        URL url;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter is not a valid URL: " + params[0]);
        }

        return loadData(url);
    }

    @Override
    protected final void onPostExecute(@Nullable final T data) {
        if (isCancelled())
        {
            return;
        }

        if (data == null) {
            listener.onDataError();
        }
        else {
            //noinspection unchecked
            listener.onDataLoaded(data);
        }
    }

    abstract protected T loadData(final URL url);
}
