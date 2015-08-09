package org.berlin_vegan.bvapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.fragments.GastroDescriptionFragment;
import org.berlin_vegan.bvapp.fragments.GastroDetailsFragment;
import org.berlin_vegan.bvapp.fragments.GastroHeadFragment;
import org.berlin_vegan.bvapp.fragments.GastroMapFragment;
import org.berlin_vegan.bvapp.fragments.GastroActionsFragment;
import org.berlin_vegan.bvapp.helpers.DividerFragment;

/**
 * Activity for the detail view of a gastro location.
 */
public class GastroActivity extends BaseActivity{

    public static final String EXTRA_GASTRO_LOCATION = "GASTRO_LOCATION";

    private GastroLocation mGastroLocation;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastro_activity);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mGastroLocation = (GastroLocation) extras.getSerializable(EXTRA_GASTRO_LOCATION);
            }
        } else {
            mGastroLocation = (GastroLocation) savedInstanceState.getSerializable(EXTRA_GASTRO_LOCATION);
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        final GastroHeadFragment gastroHeadFragment = new GastroHeadFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), gastroHeadFragment).commit();

        final GastroActionsFragment gastroActionsFragment = new GastroActionsFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), gastroActionsFragment).commit();

        DividerFragment dividerFragment = new DividerFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), dividerFragment).commit();


        GastroDescriptionFragment gastroDescriptionFragment = new GastroDescriptionFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), gastroDescriptionFragment).commit();

        dividerFragment = new DividerFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), dividerFragment).commit();

        GastroDetailsFragment gastroDetailsFragment = new GastroDetailsFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), gastroDetailsFragment).commit();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = getToolbar();
        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mGastroLocation == null) { // TODO: clarify why it is null
            return;
        }

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        final String title = mGastroLocation.getName();
        //collapsingToolbar.setTitle(title);

        // otherwise the backdrop is not fully visible
        final int transparent = getResources().getColor(android.R.color.transparent);
        toolbar.setBackgroundColor(transparent);

        GastroMapFragment mapFragment = (GastroMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.backdrop);
        mapFragment.setLocation(mGastroLocation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gastro_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // respond to the action bar's up button
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXTRA_GASTRO_LOCATION, mGastroLocation);
        super.onSaveInstanceState(savedInstanceState);
    }
}
