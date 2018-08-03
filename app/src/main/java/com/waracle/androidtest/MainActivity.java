package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment is responsible for loading in some JSON and
     * then displaying a list of cakes with images.
     * Fix any crashes
     * Improve any performance issues
     * Use good coding practices to make code more secure
     */
    public static class PlaceholderFragment extends ListFragment {

        private static final String TAG = PlaceholderFragment.class.getSimpleName();

        private ListView mListView;
        private MyAdapter mAdapter;

        public PlaceholderFragment() { /**/ }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mListView = rootView.findViewById(android.R.id.list);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Create and set the list adapter.
            mAdapter = new MyAdapter();
            mListView.setAdapter(mAdapter);

            // Load data from net.
            final DataLoader.DataListener dataListener = new DataLoader.DataListener() {
                @Override
                public void onDataLoaded(JSONArray jsonArray) {
                    mAdapter.setItems(jsonArray);
                }

                @Override
                public void onDataError() {
                    // TODO: handle data error
                }
            };

            new DataLoader(dataListener).loadData(JSON_URL);
        }

        /**
         * Returns the charset specified in the Content-Type of this header,
         * or the HTTP default (ISO-8859-1) if none can be found.
         */
        public static String parseCharset(String contentType) {
            if (contentType != null) {
                String[] params = contentType.split(",");
                for (int i = 1; i < params.length; i++) {
                    String[] pair = params[i].trim().split("=");
                    if (pair.length == 2) {
                        if (pair[0].equals("charset")) {
                            return pair[1];
                        }
                    }
                }
            }
            return "UTF-8";
        }

        private class MyAdapter extends BaseAdapter {

            // Can you think of a better way to represent these items???
            private JSONArray mItems;
            private ImageLoader mImageLoader;

            MyAdapter() {
                this(new JSONArray());
            }

            MyAdapter(JSONArray items) {
                mItems = items;
                mImageLoader = new ImageLoader();
            }

            @Override
            public int getCount() {
                return mItems.length();
            }

            @Override
            public Object getItem(int position) {
                try {
                    return mItems.getJSONObject(position);
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
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View root = inflater.inflate(R.layout.list_item_layout, parent, false);
                if (root != null) {
                    TextView title = root.findViewById(R.id.title);
                    TextView desc = root.findViewById(R.id.desc);
                    ImageView image = root.findViewById(R.id.image);
                    try {
                        JSONObject object = (JSONObject) getItem(position);
                        title.setText(object.getString("title"));
                        desc.setText(object.getString("desc"));
                        mImageLoader.load(object.getString("image"), image);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return root;
            }

            void setItems(JSONArray items) {
                mItems = items;
                notifyDataSetChanged();
            }
        }

        private static class DataLoader extends AsyncTask<String, Void, JSONArray> {

            public interface DataListener {
                void onDataLoaded(JSONArray jsonArray);
                void onDataError();
            }

            private final DataListener dataListener;

            DataLoader(DataListener dataListener) {
                this.dataListener = dataListener;
            }

            void loadData(final String url)
            {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            }

            @Override
            protected JSONArray doInBackground(String... params) {

                JSONArray jsonArray;
                HttpURLConnection urlConnection = null;

                try {
                    if (params.length == 0) {
                        throw new IllegalArgumentException("You must pass the URL as a parameter");
                    }

                    final URL url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    final InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    // Can you think of a way to improve the performance of loading data
                    // using HTTP headers???

                    // Also, Do you trust any utils thrown your way????

                    final byte[] bytes = StreamUtils.readUnknownFully(in);

                    // Read in charset of HTTP content.
                    final String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

                    // Convert byte array to appropriate encoded string.
                    final String jsonText = new String(bytes, charset);

                    // Read string as JSON.
                    jsonArray = new JSONArray(jsonText);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    jsonArray = null;
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

                return jsonArray;
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                if (jsonArray == null) {
                    dataListener.onDataError();
                }
                else {
                    dataListener.onDataLoaded(jsonArray);
                }
            }
        }
    }
}
