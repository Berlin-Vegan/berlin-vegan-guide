package org.berlin_vegan.bvapp.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.GastroLocationPicture;
import org.berlin_vegan.bvapp.data.GastroLocations;
import org.berlin_vegan.bvapp.fragments.GastroDescriptionFragment;
import org.berlin_vegan.bvapp.fragments.GastroDetailsFragment;
import org.berlin_vegan.bvapp.helpers.DividerFragment;

import java.util.List;

/**
 * Activity for the detail view of a gastro location.
 */
public class GastroActivity extends BaseActivity {

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

        GastroDescriptionFragment gastroDescriptionFragment = new GastroDescriptionFragment();
        getFragmentManager().beginTransaction().add(linearLayout.getId(), gastroDescriptionFragment).commit();

        DividerFragment dividerFragment = new DividerFragment();
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
        collapsingToolbar.setTitle(title);

        final List<GastroLocationPicture> pictures = mGastroLocation.getPictures();
        if (pictures == null || pictures.isEmpty()) {
            return;
        }

        final ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        Picasso.with(this)
                .load(pictures.get(0).getUrl())
                .into(backdrop);

        // otherwise the backdrop is not fully visible
        final int transparent = getResources().getColor(android.R.color.transparent);
        toolbar.setBackgroundColor(transparent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(transparent);
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
                    GastroLocations.addFavorite(mGastroLocation.getId(), mSharedPreferences);
                } else {
                    item.setIcon(getResources().getDrawable(R.mipmap.ic_star_outline_white_24dp));
                    GastroLocations.removeFavorite(mGastroLocation.getId(), mSharedPreferences);
                }
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
