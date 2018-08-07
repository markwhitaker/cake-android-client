package com.waracle.androidtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.data.ImageLoader;
import com.waracle.androidtest.model.Cake;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard list adapter for the list of cakes
 */
public final class CakeListAdapter extends BaseAdapter {

    private static final String TAG = "CakeListAdapter";

    private final Context context;
    private final List<Cake> cakes = new ArrayList<>();

    CakeListAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return cakes.size();
    }

    @Override
    public Object getItem(final int position) {
        return cakes.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        final View view = (convertView == null)
                ? LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)
                : convertView;

        final TextView titleView = view.findViewById(R.id.title);
        final TextView descriptionView = view.findViewById(R.id.desc);
        final ImageView imageView = view.findViewById(R.id.image);

        final Cake cake = (Cake) getItem(position);
        titleView.setText(cake.getTitle());
        descriptionView.setText(cake.getDescription());
        imageView.setImageResource(android.R.color.transparent);

        loadImage(cake.getImageUrl(), imageView);

        return view;
    }

    void setCakes(@NonNull final List<Cake> cakes) {
        this.cakes.clear();
        this.cakes.addAll(cakes);
        notifyDataSetChanged();
    }

    void clearCakes() {
        this.cakes.clear();
        notifyDataSetChanged();
    }

    private void loadImage(final String imageUrl, final ImageView imageView) {
        // Store the URL against the ImageView so we can tell if the ImageView is still showing the
        // same cake when the image load completes.
        imageView.setTag(imageUrl);
        // Hold the ImageView in a WeakReference in case it's been garbage-collected by the time the
        // image loads
        final WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);

        final ImageLoader.Listener listener = new ImageLoader.Listener() {
            @Override
            public void onDataLoaded(final String requestUrl, final Bitmap data) {
                final ImageView view = imageViewRef.get();
                if (view != null && view.getTag().equals(imageUrl)) {
                    view.setImageBitmap(data);
                }
            }

            @Override
            public void onDataError() {
                Log.w(TAG, "Failed to load image at " + imageUrl);
            }
        };

        new ImageLoader(imageUrl, listener).load();
    }
}
