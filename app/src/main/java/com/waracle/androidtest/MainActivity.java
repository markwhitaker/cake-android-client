package com.waracle.androidtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "cakeListFragment";

    private CakeListFragment cakeListFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            cakeListFragment = new CakeListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, cakeListFragment, FRAGMENT_TAG)
                    .commit();
        }
        else {
            cakeListFragment = (CakeListFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            cakeListFragment.loadData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
