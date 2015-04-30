package org.berlin_vegan.bvapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainListActivity extends BaseActivity {

    private static final String TAG = "MainListActivity";

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private static final String HTTP_GASTRO_LOCATIONS_JSON =
            "http://www.berlin-vegan.de/app/data/" + GASTRO_LOCATIONS_JSON;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GastroLocationAdapter mGastroLocationAdapter;
    private LocationManager mLocationManager;
    private GastroLocationListener mGastroLocationListener;
    private Location mLocationFromJson;
    private Location mLocationFound;
    private Dialog mDialog;
    private SharedPreferences mSharedPreferences;
    // set an empty list. fill it below in a separate thread. network usage is not allowed on ui thread
    private boolean mUseLocalCopy;
    private List<GastroLocation> mGastroLocations = new ArrayList<>();
    private final GastroListCallbackSingleChoice mButtonCallback = new GastroListCallbackSingleChoice(this, mGastroLocations);

    static List<GastroLocation> createList(final InputStream inputStream) {
        final InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<ArrayList<GastroLocation>>() {
        }.getType();
        final List<GastroLocation> locationList = new Gson().fromJson(reader, listType);
        return locationList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.main_list_activity);
        setTitle(getString(R.string.app_name) + " " + getString(R.string.guide));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_list_activity_swipe_refresh_layout);
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
                        receiveCurrentLocation();
                        sortGastroLocations();
                        MainListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGastroLocationAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
                Thread t = new Thread(waitForGpsFix);
                t.start();
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // start a thread to retrieve the json from the server and to wait for the geo location
        RetrieveGastroLocations retrieveGastroLocations = new RetrieveGastroLocations(this);
        retrieveGastroLocations.execute();
        mGastroLocationAdapter = new GastroLocationAdapter(this, mGastroLocations);
        mGastroLocationListener = new GastroLocationListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mGastroLocationAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mSwipeRefreshLayout.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        //fast scroll


    }

    protected List<GastroLocation> getGastroJson() {
        final InputStream inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
        return createList(inputStream);
    }

    void updateCardView(List<GastroLocation> gastroLocations) {
        sortGastroLocations();
        mGastroLocationAdapter.setGastroLocations(gastroLocations);
        mGastroLocationAdapter.notifyDataSetChanged();
    }

    public void setLocationFound(Location locationFound) {
        mLocationFound = locationFound;
    }

    private class RetrieveGastroLocations extends AsyncTask<Void, Void, List<GastroLocation>> {
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
                        mDialog = UiUtils.showMaterialProgressDialog(mMainListActivity, getString(R.string.please_wait),
                                getString(R.string.retrieving_gps_data));
                    }
                }
            });
        }

        @Override
        protected List<GastroLocation> doInBackground(Void... params) {
            // get local json file as fall back
            mUseLocalCopy = true;
            List<GastroLocation> gastroLocations = getGastroJson();
            try {
                // fetch json file from server
                final URL url = new URL(HTTP_GASTRO_LOCATIONS_JSON);
                InputStream inputStream = url.openStream();
                mUseLocalCopy = false;
                gastroLocations = createList(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mUseLocalCopy) {
                Log.i(TAG, "fall back: use local copy of database file");
            } else {
                Log.i(TAG, "retrieving database from server successful");
            }
            setGastroLocations(gastroLocations);
            mButtonCallback.setAllGastroLocations(gastroLocations);
            receiveCurrentLocation();
            return gastroLocations;
        }

        @Override
        protected void onPostExecute(final List<GastroLocation> gastroLocations) {
            MainListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCardView(gastroLocations);
                }
            });
        }
    }

    private void receiveCurrentLocation() {
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
            MainListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    /*
    TODO
    Save last location - prevent from requestlocationupdates.
     */
    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeLocationUpdates();
    }

    /*
    TODO
    1. Add more filter options
    2. save state of checkbox - either globally with preference or just for the session and clear preference on app resume
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                filterList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filterList() {
        UiUtils.showMaterialDialogCheckboxes(MainListActivity.this, getString(R.string.filter_title_dialog),
                getResources().getStringArray(R.array.filter_checkboxes), -1, mButtonCallback);
    }

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

    private void removeLocationUpdates() {
        if (mLocationManager != null)
            mLocationManager.removeUpdates(mGastroLocationListener);
    }

    public void sortGastroLocations() {
        if (mLocationFound == null) {
            return;
        }

        mLocationFromJson = new Location("");
        float distanceInMeters;
        float distanceInKiloMeters;
        float distanceInMiles;
        float distanceRoundOnePlace;
        for (int i = 0; i < mGastroLocations.size(); i++) {
            GastroLocation gastroLocation = mGastroLocations.get(i);
            mLocationFromJson.setLatitude(gastroLocation.getLatCoord());
            mLocationFromJson.setLongitude(gastroLocation.getLongCoord());
            distanceInMeters = mLocationFromJson.distanceTo(mLocationFound);
            distanceInKiloMeters = distanceInMeters / 1000;
            distanceInMiles = distanceInKiloMeters * (float) 0.621371192;
            // 1. explicit cast to float necessary, otherwise we always get x.0 values
            // 2. Math.round(1.234 * 10) / 10 = Math.round(12.34) / 10 = 12 / 10 = 1.2
            if (mSharedPreferences.getBoolean("key_units", true)) {
                distanceRoundOnePlace = (float) Math.round(distanceInKiloMeters * 10) / 10;
            } else {
                distanceRoundOnePlace = (float) Math.round(distanceInMiles * 10) / 10;
            }
            gastroLocation.setDistToCurLoc(distanceRoundOnePlace);
        }
        Collections.sort(mGastroLocations);
    }

    public GastroLocationAdapter getGastroLocationAdapter() {
        return mGastroLocationAdapter;
    }

    private void setGastroLocations(List<GastroLocation> gastroLocations) {
        mGastroLocations = mGastroLocations.isEmpty() ? gastroLocations : throw_();
    }

    private List<GastroLocation> throw_() {
        throw new RuntimeException("gastro locations are already set");
    }

    private class GastroLocationListener implements LocationListener {

        private static final String TAG = "GastroLocationListener";

        private MainListActivity mMainListActivity;

        public GastroLocationListener(MainListActivity mainListActivity) {
            mMainListActivity = mainListActivity;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "location found: " + location.toString());
            //remove to preserve battery
            mMainListActivity.removeLocationUpdates();
            mMainListActivity.setLocationFound(location);
            mMainListActivity.sortGastroLocations();
            mMainListActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMainListActivity.getGastroLocationAdapter().notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO: handle onStatusChanged
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO: handle onProviderEnabled
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO: handle onProviderDisabled
        }
    }
}
