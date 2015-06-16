package org.berlin_vegan.bvapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.berlin_vegan.bvapp.BuildConfig;
import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.adapters.ViewPagerAdapter;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.GastroLocations;

/**
 * Activity for the detail view of a gastro location.
 */
public class GastroActivity extends BaseActivity {

    private static final int NUM_STARS = 25;

    private GastroLocation mGastroLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastro_activity);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mGastroLocation = (GastroLocation) extras.getSerializable("GASTRO_LOCATION");
            }
        } else {
            mGastroLocation = (GastroLocation) savedInstanceState.getSerializable("GASTRO_LOCATION");
        }

        // tab handling
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(mGastroLocation.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gastro_activity, menu);
        MenuItem item = menu.findItem(R.id.action_add_favorite);
        boolean isFavorite = GastroLocations.containsFavorite(mGastroLocation.getId());
        if (isFavorite) {
            item.setIcon(getResources().getDrawable(R.mipmap.ic_star_white_24dp));
        } else {
            item.setIcon(getResources().getDrawable(R.mipmap.ic_star_outline_white_24dp));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // respond to the action bar's up button
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_add_favorite:
                boolean isFavorite = GastroLocations.containsFavorite(mGastroLocation.getId());
                if (!isFavorite) {
                    item.setIcon(getResources().getDrawable(R.mipmap.ic_star_white_24dp));
                    GastroLocations.addFavorite(mGastroLocation.getId());
                } else {
                    item.setIcon(getResources().getDrawable(R.mipmap.ic_star_outline_white_24dp));
                    GastroLocations.removeFavorite(mGastroLocation.getId());
                }
                break;
            case R.id.action_report_error:
                final Intent report = new Intent(Intent.ACTION_SENDTO);
                final String uriText = ""
                        + "mailto:" + Uri.encode("bvapp@berlin-vegan.org")
                        + "?subject=" + Uri.encode(getMessageSubject())
                        + "&body=" + Uri.encode(getMessageBody());
                final Uri uri = Uri.parse(uriText);
                report.setData(uri);
                startActivity(Intent.createChooser(report, getString(R.string.action_report_error)));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("GASTRO_LOCATION", mGastroLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private String getMessageSubject() {
        return getString(R.string.error) + ": "
                + mGastroLocation.getName() + ", "
                + mGastroLocation.getStreet();
    }

    private String getMessageBody() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < NUM_STARS; i++) {
            stars.append("*");
        }
        return stars
                + "\nApp Version: " + BuildConfig.VERSION_GIT_DESCRIPTION
                + "\nDevice Name: " + Build.MODEL
                + "\nPlatform: Android"
                + "\nDevice Version: " + Build.VERSION.RELEASE
                + "\n" + stars
                + "\n\n" + getString(R.string.insert_error_report)
                + "\n\n";
    }
}
