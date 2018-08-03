package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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

public class CakeListAdapter extends BaseAdapter {

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
    public Object getItem(int position) {
        return cakes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.list_item_layout, parent, false);

        if (root != null) {
            final TextView titleView = root.findViewById(R.id.title);
            final TextView descriptionView = root.findViewById(R.id.desc);
            final ImageView imageView = root.findViewById(R.id.image);

            final Cake cake = (Cake) getItem(position);
            titleView.setText(cake.getTitle());
            descriptionView.setText(cake.getDescription());
            loadImage(cake.getImageUrl(), imageView);
        }

        return root;
    }

    void setCakes(final List<Cake> cakes) {
        this.cakes.clear();
        this.cakes.addAll(cakes);
        notifyDataSetChanged();
    }

    private void loadImage(final String imageUrl, final ImageView imageView)
    {
        final WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);

        ImageLoader.Listener listener = new ImageLoader.Listener() {
            @Override
            public void onDataLoaded(Bitmap data) {
                final ImageView view = imageViewRef.get();
                if (view != null)
                {
                    view.setImageBitmap(data);
                }
            }

            @Override
            public void onDataError() {
                // TODO: handle error
            }
        };

        new ImageLoader(listener).load(imageUrl);
    }
}
