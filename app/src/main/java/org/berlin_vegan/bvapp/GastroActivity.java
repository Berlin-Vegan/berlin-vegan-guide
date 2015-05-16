package org.berlin_vegan.bvapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

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
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.theme_primary_dark);
            }
        });
        tabs.setViewPager(pager);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_report_error:
                final Intent report = new Intent(Intent.ACTION_SENDTO);
                final StringBuilder uriText = new StringBuilder()
                        .append("mailto:")
                        .append(Uri.encode("bvapp@berlin-vegan.org"))
                        .append("?subject=")
                        .append(Uri.encode(getMessageSubject()))
                        .append("&body=")
                        .append(Uri.encode(getMessageBody()));
                final Uri uri = Uri.parse(uriText.toString());
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
        StringBuilder subject = new StringBuilder()
                .append(getString(R.string.error))
                .append(": ")
                .append(mGastroLocation.getName())
                .append(", ")
                .append(mGastroLocation.getStreet());
        return subject.toString();
    }

    private String getMessageBody() {
        String stars = "";
        for (int i = 0; i < NUM_STARS; i++) {
            stars += "*";
        }
        final StringBuilder body = new StringBuilder()
                .append(stars)
                .append("\nApp Version: ")
                .append(BuildConfig.VERSION_GIT_DESCRIPTION)
                .append("\nDevice Name: ")
                .append(Build.MODEL)
                .append("\nPlatform: Android")
                .append("\nDevice Version: ")
                .append(Build.VERSION.RELEASE)
                .append("\n")
                .append(stars)
                .append("\n\n")
                .append(getString(R.string.insert_error_report))
                .append("\n\n");
        return body.toString();
    }
}
