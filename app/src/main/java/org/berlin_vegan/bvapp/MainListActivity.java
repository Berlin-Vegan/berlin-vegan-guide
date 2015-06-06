package org.berlin_vegan.bvapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainListActivity extends BaseActivity {

    private static final String TAG = "MainListActivity";

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private static final String HTTP_GASTRO_LOCATIONS_JSON =
            "http://www.berlin-vegan.de/app/data/" + GASTRO_LOCATIONS_JSON;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GastroLocationAdapter mGastroLocationAdapter;
    private LocationManager mLocationManager;
    private GastroLocationListener mGastroLocationListener;
    private Location mLocationFound;
    private Dialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private boolean mUseLocalCopy;
    private GastroLocations mGastroLocations;
    private final GastroListCallbackSingleChoice mButtonCallback = new GastroListCallbackSingleChoice(this);

    // --------------------------------------------------------------------
    // life cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        setTitle(getString(R.string.app_name) + " " + getString(R.string.guide));

        mContext = this;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_list_activity_swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            setupSwipeRefresh();
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // start a thread to retrieve the json from the server and to wait for the geo location
        RetrieveGastroLocations retrieveGastroLocations = new RetrieveGastroLocations(this);
        retrieveGastroLocations.execute();

        mGastroLocationAdapter = new GastroLocationAdapter(this);
        mGastroLocations = new GastroLocations(this);
        mGastroLocationListener = new GastroLocationListener(this, mGastroLocations);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_list_recycler_view);
        if (mRecyclerView != null) {
            setupRecyclerView(linearLayoutManager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdates();
        if (mGastroLocations.isFavoritesCurrentlyShown()) {
            // update the list, because the user may have added or removed a favorite in {@code GastroActivity}
            mGastroLocations.showFavorites();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.remove(GastroLocations.KEY_FILTER);
        editor.apply();
    }

    // --------------------------------------------------------------------
    // menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_list_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.clear();

        inflater.inflate(R.menu.menu_main_list_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        initializeSearch(menuItem);

        super.onPrepareOptionsMenu(menu);
        return true;
    }

    private void initializeSearch(MenuItem searchViewItem) {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mGastroLocations != null) {
                    mGastroLocations.processQueryFilter(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return onQueryTextSubmit(newText);
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchViewItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if (mGastroLocations != null) {
                    mGastroLocations.resetQueryFilter();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                int selected = mSharedPreferences.getInt(GastroLocations.KEY_FILTER, 0);
                UiUtils.showMaterialDialogCheckboxes(MainListActivity.this, getString(R.string.filter_title_dialog),
                        getString(R.string.filter_content_dialog),
                        getResources().getStringArray(R.array.filter_checkboxes), selected, mButtonCallback);
                break;
            case R.id.action_settings:
                final Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.action_about:
                if (mContext != null) {
                    UiUtils.showMaterialAboutDialog(mContext, getResources().getString(R.string.action_about));
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --------------------------------------------------------------------
    // setups

    private void setupRecyclerView(final LinearLayoutManager linearLayoutManager) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mGastroLocationAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mSwipeRefreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        // TODO: fast scroll
    }

    private void setupSwipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        // refreshes the gps fix. re-sorts the card view
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // very important for the runnable further below
                mLocationFound = null;
                removeLocationUpdates();
                requestLocationUpdates();
                // runnable to determine when the first GPS fix was received.
                final Runnable waitForGpsFix = new Runnable() {
                    @Override
                    public void run() {
                        waitForGpsFix();
                        mGastroLocations.updateLocationAdapter(mLocationFound);
                    }
                };
                Thread t = new Thread(waitForGpsFix);
                t.start();
            }
        });
    }

    // --------------------------------------------------------------------
    // location handling

    private void requestLocationUpdates() {
        final int minTime = 3 * 60 * 1000; // e.g. 5 * 60 * 1000 (5 minutes)
        final int minDistance = 100;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mGastroLocationListener);
        }
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mGastroLocationListener);
        }
    }

    void removeLocationUpdates() {
        if (mLocationManager != null)
            mLocationManager.removeUpdates(mGastroLocationListener);
    }

    private void waitForGpsFix() {
        final long startTimeMillis = System.currentTimeMillis();
        final int waitTimeMillis = 20 * 1000;
        while (mLocationFound == null) {
            // wait for first GPS fix (do nothing)
            if ((System.currentTimeMillis() - startTimeMillis) > waitTimeMillis) {
                MainListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UiUtils.showMaterialDialog(MainListActivity.this, getString(R.string.error),
                                getString(R.string.no_gps_data));
                    }
                });
                break;
            }
        }
        MainListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public GastroLocations getGastroLocations() {
        return mGastroLocations;
    }

    public GastroLocationAdapter getGastroLocationAdapter() {
        return mGastroLocationAdapter;
    }

    void setLocationFound(Location locationFound) {
        mLocationFound = locationFound;
    }

    static List<GastroLocation> createList(final InputStream inputStream) {
        final InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
        Type listType = new TypeToken<ArrayList<GastroLocation>>() {
        }.getType();
        final List<GastroLocation> locationList = new Gson().fromJson(reader, listType);
        return locationList;
    }

    private class RetrieveGastroLocations extends AsyncTask<Void, Void, Void> {
        private MainListActivity mMainListActivity;

        public RetrieveGastroLocations(MainListActivity mainListActivity) {
            mMainListActivity = mainListActivity;
        }

        @Override
        protected void onPreExecute() {
            MainListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mProgressDialog = UiUtils.showMaterialProgressDialog(mMainListActivity, getString(R.string.please_wait),
                                getString(R.string.retrieving_data));
                    }
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream;
            List<GastroLocation> gastroLocations = null;
            try {
                // fetch json file from server
                final URL url = new URL(HTTP_GASTRO_LOCATIONS_JSON);
                final URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(5 * 1000);
                urlConnection.setReadTimeout(5 * 1000);
                inputStream = urlConnection.getInputStream();
                mUseLocalCopy = false;
                gastroLocations = createList(inputStream);
            } catch (IOException e) {
                Log.e(TAG, "fetching json file from server failed", e);
            }
            if (gastroLocations == null) {
                // get local json file as fall back
                mUseLocalCopy = true;
                inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
                gastroLocations = createList(inputStream);
            }
            if (mUseLocalCopy) {
                Log.i(TAG, "fall back: use local copy of database file");
            } else {
                Log.i(TAG, "retrieving database from server successful");
            }
            Log.d(TAG, "read " + gastroLocations.size() + " entries");
            mGastroLocations.set(gastroLocations);
            waitForGpsFix();
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mGastroLocations.updateLocationAdapter();
        }
    }
}
