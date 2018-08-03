package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.data.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class CakeListAdapter extends BaseAdapter {

    private final Context context;

    // Can you think of a better way to represent these items???
    private JSONArray items = new JSONArray();

    CakeListAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return items.getJSONObject(position);
        } catch (JSONException e) {
            Log.e("", e.getMessage());
        }
        return null;
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
            TextView title = root.findViewById(R.id.title);
            TextView desc = root.findViewById(R.id.desc);
            ImageView image = root.findViewById(R.id.image);
            try {
                JSONObject object = (JSONObject) getItem(position);
                title.setText(object.getString("title"));
                desc.setText(object.getString("desc"));
                loadImage(object.getString("image"), image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return root;
    }

    void setItems(JSONArray items) {
        this.items = items;
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
