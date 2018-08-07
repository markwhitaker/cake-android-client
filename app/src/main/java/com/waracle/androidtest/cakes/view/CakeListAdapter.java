package com.waracle.androidtest.cakes.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.cakes.CakeContracts;
import com.waracle.androidtest.cakes.model.Cake;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Standard list adapter for the list of cakes
 */
public final class CakeListAdapter extends BaseAdapter {

    private final Context context;
    private final CakeContracts.ListPresenter presenter;
    private final List<Cake> cakes = new ArrayList<>();
    private final Map<ImageView, String> imageUrlMap = new WeakHashMap<>();

    CakeListAdapter(final Context context, final CakeContracts.ListPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
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
        synchronized (imageUrlMap) {
            imageUrlMap.put(imageView, imageUrl);
        }
        presenter.loadThumbnailImage(imageUrl);
    }

    void showCakeThumbnailImage(final String imageUrl, final Bitmap bitmap) {
        synchronized (imageUrlMap) {
            for (final Map.Entry<ImageView, String> entry : imageUrlMap.entrySet()) {
                if (entry.getValue().equals(imageUrl)) {
                    entry.getKey().setImageBitmap(bitmap);
                }
            }
        }
    }
}
