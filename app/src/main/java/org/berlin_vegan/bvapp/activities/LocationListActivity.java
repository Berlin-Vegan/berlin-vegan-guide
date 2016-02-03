package org.berlin_vegan.bvapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.berlin_vegan.bvapp.MainApplication;
import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.adapters.LocationAdapter;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.Location;
import org.berlin_vegan.bvapp.data.Locations;
import org.berlin_vegan.bvapp.data.Preferences;
import org.berlin_vegan.bvapp.data.ShoppingLocation;
import org.berlin_vegan.bvapp.helpers.DividerItemDecoration;
import org.berlin_vegan.bvapp.helpers.GastroLocationFilterCallback;
import org.berlin_vegan.bvapp.helpers.UiUtils;
import org.berlin_vegan.bvapp.listeners.CustomLocationListener;
import org.berlin_vegan.bvapp.views.GastroFilterView;
import org.berlin_vegan.bvapp.views.LocationRecycleView;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point of the program.
 */
public class LocationListActivity extends BaseActivity {

    private static final String TAG = "LocationListActivity";

    private static final String JSON_BASE_URL = "http://www.berlin-vegan.de/app/data/";
    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private static final String SHOPPING_LOCATIONS_JSON = "ShoppingLocations.json";

    private static final String HTTP_GASTRO_LOCATIONS_JSON = JSON_BASE_URL + GASTRO_LOCATIONS_JSON;
    private static final String HTTP_SHOPPING_LOCATIONS_JSON = JSON_BASE_URL + SHOPPING_LOCATIONS_JSON;
    private ActionBarDrawerToggle drawerToggle;

    private Context mContext;
    private LocationRecycleView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LocationAdapter mLocationAdapter;
    private LocationManager mLocationManager;
    private CustomLocationListener mLocationListener;
    // the GPS/Network Location
    private android.location.Location mGpsLocationFound;
    private boolean mGpsProviderAvailable = false;
    private Dialog mProgressDialog;

    private Locations mLocations;

    private final GastroLocationFilterCallback mButtonCallback = new GastroLocationFilterCallback(this);
    //NavDrawer
    private DrawerLayout mDrawer;

    // --------------------------------------------------------------------
    // life cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list_activity);
        setTitle(getString(R.string.app_name));

        mContext = this;

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.location_list_activity_swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            setupSwipeRefresh();
        }


        // start a thread to retrieve the json from the server and to wait for the geo location
        RetrieveLocations retrieveLocations = new RetrieveLocations(this);
        retrieveLocations.execute();

        mLocationAdapter = new LocationAdapter(this);
        mLocations = new Locations(this);
        mLocationListener = new CustomLocationListener(this, mLocations);

        mRecyclerView = (LocationRecycleView) findViewById(R.id.location_list_recycler_view);
        if (mRecyclerView != null) {
            setupRecyclerView(mRecyclerView, mSwipeRefreshLayout, mLocations);
        }

        //NavDrawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //find our drawer view
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.getMenu().getItem(0).setChecked(true);
        setupDrawerContent(nvDrawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.gastro_details_miscellaneous_content_catering, R.string.gastro_details_miscellaneous_content_catering);
        mDrawer.setDrawerListener(drawerToggle);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_gastro:
                applyShownDataType(Locations.DATA_TYPE.GASTRO);
                menuItem.setChecked(true);
                break;

            case R.id.nav_shopping:
                applyShownDataType(Locations.DATA_TYPE.SHOPPING);
                menuItem.setChecked(true);
                break;

            case R.id.nav_fav:
                applyShownDataType(Locations.DATA_TYPE.FAVORITE);
                menuItem.setChecked(true);
                break;

            case R.id.nav_rate:
                //set this to false in foss FDroid
                UiUtils.rateApp(this,true);
                break;
            case R.id.nav_pref:
                final Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;

            case R.id.nav_mapview:
                final Intent mapview = new Intent(this, LocationMapActivity.class);
                startActivity(mapview);
                break;

            case R.id.nav_about:
                if (mContext != null) {
                    UiUtils.showMaterialAboutDialog(mContext, getResources().getString(R.string.action_about));
                }

            default:
                break;
        }
        mDrawer.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestGpsLocationUpdates();
        if (mLocations.getDataType() == Locations.DATA_TYPE.FAVORITE) {
            // update the list, because the user may have added or removed a favorite in {@code GastroActivity}
            mLocations.showFavorites();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeGpsLocationUpdates();
    }


    // --------------------------------------------------------------------
    // menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_list_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        menu.clear();

        inflater.inflate(R.menu.menu_location_list_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        if (mLocations.getDataType() == Locations.DATA_TYPE.GASTRO || mLocations.getDataType() == Locations.DATA_TYPE.SHOPPING) {
            initializeSearch(menuItem);
        } else {
            menuItem.setVisible(false); // hide for favorite
        }

        menuItem = menu.findItem(R.id.action_filter);
        if (mLocations.getDataType() == Locations.DATA_TYPE.FAVORITE || mLocations.getDataType() == Locations.DATA_TYPE.SHOPPING) { // at the moment no filter for shopping and favorite
            menuItem.setVisible(false);
        }
        return true;
    }

    private void initializeSearch(MenuItem searchViewItem) {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mLocations != null) {
                    mLocations.processQueryFilter(query);
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
                mLocations.setSearchState(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if (mLocations != null) {
                    mLocations.setSearchState(false);
                    mLocations.resetQueryFilter();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) { // delegate the touch to ActionBarDrawerToggle
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_filter:
                final GastroFilterView gastroFilterView = new GastroFilterView(LocationListActivity.this);
                gastroFilterView.init(getLocations(), Preferences.getGastroFilter(this));
                UiUtils.showMaterialDialogCustomView(LocationListActivity.this,
                        getString(R.string.gastro_filter_title_dialog),
                        gastroFilterView,
                        mButtonCallback);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle saveInstanceState) {
        super.onPostCreate(saveInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void applyShownDataType(Locations.DATA_TYPE dataType) {
        mLocations.setDataType(dataType);
        if (dataType == Locations.DATA_TYPE.FAVORITE) {
            mLocations.showFavorites();
        } else if (dataType == Locations.DATA_TYPE.SHOPPING) {
            mLocations.showShoppingLocations();
        } else {
            mLocations.showGastroLocations();
        }
        mRecyclerView.scrollToPosition(0);
        invalidateOptionsMenu();
    }

    // --------------------------------------------------------------------
    // setups

    private void setupRecyclerView(LocationRecycleView recyclerView, final SwipeRefreshLayout swipeRefreshLayout, Locations locations) {
        recyclerView.setLocations(locations);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(mLocationAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                swipeRefreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        recyclerView.setEmptyViews(findViewById(R.id.location_list_empty_favorites_textview), findViewById(R.id.location_list_empty_search_textview));
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
                mGpsLocationFound = null;
                removeGpsLocationUpdates();
                requestGpsLocationUpdates();
                // runnable to determine when the first GPS fix was received.
                final Runnable waitForGpsFix = new Runnable() {
                    @Override
                    public void run() {
                        waitForGpsFix();
                        mLocations.updateLocationAdapter(mGpsLocationFound);
                    }
                };
                Thread t = new Thread(waitForGpsFix);
                t.start();
            }
        });
    }

    // --------------------------------------------------------------------
    // location handling

    private void requestGpsLocationUpdates() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = mLocationManager.getBestProvider(criteria, true);
        if (provider != null) {
            mGpsProviderAvailable = true;
            mLocationManager.requestSingleUpdate(criteria, mLocationListener, null);
        } else {
            mGpsProviderAvailable = false;
        }
    }

    public void removeGpsLocationUpdates() {
        if (mLocationManager != null)
            mLocationManager.removeUpdates(mLocationListener);
    }

    private void waitForGpsFix() {
        final long startTimeMillis = System.currentTimeMillis();
        final int waitTimeMillis = 20 * 1000;
        while (mGpsLocationFound == null) {
            // wait for first GPS fix (do nothing)
            if (((System.currentTimeMillis() - startTimeMillis) > waitTimeMillis) || !mGpsProviderAvailable) {
                if (!LocationListActivity.this.isFinishing()) {
                    LocationListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UiUtils.showMaterialDialog(LocationListActivity.this, getString(R.string.error),
                                    getString(R.string.no_gps_data));
                        }
                    });
                }
                break;
            }
        }
        LocationListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public Locations getLocations() {
        return mLocations;
    }

    public LocationAdapter getLocationAdapter() {
        return mLocationAdapter;
    }

    public void setLocationFound(android.location.Location locationFound) {
        mGpsLocationFound = locationFound;
    }

    public static List<Location> createList(final InputStream inputStream, Type type) {
        final InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
        return new Gson().fromJson(reader, type);
    }


    private class RetrieveLocations extends AsyncTask<Void, Void, Void> {
        public static final int TIMEOUT_MILLIS = 5 * 1000;
        private final LocationListActivity mLocationListActivity;
        private final Type gastroTokenType = new TypeToken<ArrayList<GastroLocation>>() {
        }.getType();
        private final Type shoppingTokenType = new TypeToken<ArrayList<ShoppingLocation>>() {
        }.getType();

        public RetrieveLocations(LocationListActivity locationListActivity) {
            mLocationListActivity = locationListActivity;
        }

        @Override
        protected void onPreExecute() {
            LocationListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mProgressDialog = UiUtils.showMaterialProgressDialog(mLocationListActivity, getString(R.string.please_wait),
                                getString(R.string.retrieving_data));
                    }
                }
            });
        }


        @Override
        protected void onPostExecute(Void param) {
            applyShownDataType(Locations.DATA_TYPE.GASTRO); // todo, gastro is default ?
            mLocations.updateLocationAdapter();
        }

        // todo remove duplicated code
        @Override
        protected Void doInBackground(Void... params) {
            List<Location> gastroLocations = getGastroLocationsFromServer();
            if (gastroLocations == null) { // not modified, timeout or parsing problem, so use cached version if available
                gastroLocations = getLocationsFromCache(GASTRO_LOCATIONS_JSON, gastroTokenType);
            }
            if (gastroLocations == null) { // use included json file as fall back
                gastroLocations = getLocationsFromBundle(GASTRO_LOCATIONS_JSON, gastroTokenType);
            }
            Log.d(TAG, "read " + gastroLocations.size() + " entries");

            List<Location> shoppingLocations = getShoppingLocationsFromServer();
            if (shoppingLocations == null) { // not modified, timeout or parsing problem, so use cached version if available
                shoppingLocations = getLocationsFromCache(SHOPPING_LOCATIONS_JSON, shoppingTokenType);
            }
            if (shoppingLocations == null) { // use included json file as fall back
                shoppingLocations = getLocationsFromBundle(SHOPPING_LOCATIONS_JSON, gastroTokenType);
            }
            Log.d(TAG, "read " + shoppingLocations.size() + " entries");

            gastroLocations.addAll(shoppingLocations); // merge both lists

            mLocations.set(gastroLocations); // todo fix NullPointer
            waitForGpsFix();
            return null;
        }

        // todo merge with getGastroLocationsFromServer, remove code duplication
        @Nullable
        private List<Location> getShoppingLocationsFromServer() {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            List<Location> locations = null;
            try {
                // fetch json file from server
                final URL url = new URL(HTTP_SHOPPING_LOCATIONS_JSON);
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
                urlConnection.setReadTimeout(TIMEOUT_MILLIS);
                if (Preferences.getShoppingLastModified(mLocationListActivity) != 0) {
                    urlConnection.setIfModifiedSince(Preferences.getShoppingLastModified(mLocationListActivity));
                }
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_NOT_MODIFIED) { // modified, try to parse and if successfully store a cached version
                    inputStream = urlConnection.getInputStream();
                    locations = createList(inputStream, shoppingTokenType);
                    final long lastModified = urlConnection.getLastModified();
                    if (lastModified != 0) { //valid timestamp, store local cache version
                        fileOutputStream = mLocationListActivity.openFileOutput(SHOPPING_LOCATIONS_JSON, Context.MODE_PRIVATE);
                        final String gastroStr = new Gson().toJson(locations);
                        if (!TextUtils.isEmpty(gastroStr)) {
                            fileOutputStream.write(gastroStr.getBytes());
                            fileOutputStream.close();
                            Preferences.saveShoppingLastModified(mLocationListActivity, lastModified);
                        }
                    }
                    Log.i(TAG, "retrieving shopping database from server successful");
                }
            } catch (IOException e) {
                Log.e(TAG, "fetching json file from server failed", e);
            } catch (RuntimeException e) {
                // is thrown if a JsonParseException occurs
                Log.e(TAG, "parsing the json file failed", e);
                locations = null;
            } finally {
                closeStream(inputStream);
                closeStream(fileOutputStream);
            }
            return locations;
        }

        // todo merge with getShoppingLocationsFromServer, remove code duplication
        @Nullable
        private List<Location> getGastroLocationsFromServer() {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            List<Location> gastroLocations = null;
            try {
                // fetch json file from server
                final URL url = new URL(HTTP_GASTRO_LOCATIONS_JSON);
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
                urlConnection.setReadTimeout(TIMEOUT_MILLIS);
                if (Preferences.getGastroLastModified(mLocationListActivity) != 0) {
                    urlConnection.setIfModifiedSince(Preferences.getGastroLastModified(mLocationListActivity));
                }
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_NOT_MODIFIED) { // modified, try to parse and if successfully store a cached version
                    inputStream = urlConnection.getInputStream();
                    gastroLocations = createList(inputStream, gastroTokenType);
                    final long lastModified = urlConnection.getLastModified();
                    if (lastModified != 0) { //valid timestamp, store local cache version
                        fileOutputStream = mLocationListActivity.openFileOutput(GASTRO_LOCATIONS_JSON, Context.MODE_PRIVATE);
                        final String gastroStr = new Gson().toJson(gastroLocations);
                        if (!TextUtils.isEmpty(gastroStr)) {
                            fileOutputStream.write(gastroStr.getBytes());
                            fileOutputStream.close();
                            Preferences.saveGastroLastModified(mLocationListActivity, lastModified);
                        }
                    }
                    Log.i(TAG, "retrieving gastro database from server successful");
                }
            } catch (IOException e) {
                Log.e(TAG, "fetching json file from server failed", e);
            } catch (RuntimeException e) {
                // is thrown if a JsonParseException occurs
                Log.e(TAG, "parsing the json file failed", e);
                gastroLocations = null;
            } finally {
                closeStream(inputStream);
                closeStream(fileOutputStream);
            }
            return gastroLocations;
        }

        @Nullable
        private List<Location> getLocationsFromCache(String fileName, Type tokenType) {
            List<Location> locations;
            FileInputStream fileInputStream = null;
            try { // try cached version
                fileInputStream = mLocationListActivity.openFileInput(fileName);
                locations = createList(fileInputStream, tokenType);
                Log.i(TAG, "use cached version of database file");
            } catch (RuntimeException | IOException e) {
                Log.e(TAG, "parsing the cached json file failed", e);
                locations = null;
            } finally {
                closeStream(fileInputStream);
            }
            return locations;
        }

        private List<Location> getLocationsFromBundle(String locationsJson, Type tokenType) {
            List<Location> locations;
            InputStream inputStream;
            inputStream = MainApplication.class.getResourceAsStream(locationsJson);
            locations = createList(inputStream, tokenType);
            closeStream(inputStream);
            Log.i(TAG, "fall back: use bundled copy of database file");
            return locations;
        }

        private void closeStream(Closeable closeable) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "closing stream failed", e);
            }
        }
    }

}
