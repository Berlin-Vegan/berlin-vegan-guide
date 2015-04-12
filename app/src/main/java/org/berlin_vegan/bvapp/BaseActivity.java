package org.berlin_vegan.bvapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by georgi on 4/10/15.
 */
public class BaseActivity extends ActionBarActivity {
    private final Activity main = (Activity) this;
    private Context c;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.c = getApplicationContext();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //change the menu based on the calling activity
        if (main instanceof MainListActivity) {
            getMenuInflater().inflate(R.menu.menu_main_list, menu);
        }
        //else other activities
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);
                break;
            case R.id.action_filter:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
