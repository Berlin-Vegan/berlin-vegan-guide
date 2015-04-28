package org.berlin_vegan.bvapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;

public class MainListActivity extends BaseActivity {

    private static final String TAG = "MainListActivity";

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GastroLocationAdapter mGastroLocationAdapter;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocationFromJson;
    private Location mLocationFound;
    private Dialog mDialog;
    private SharedPreferences mSharedPreferences;
    private List<GastroLocation> mGastroLocations;
    RecyclerView mRecyclerView;
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // very important for the runnable further below
                mLocationFromJson = null;
                removeLocationUpdates();
                requestLocationUpdates();
                receiveCurrentLocation();
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        receiveCurrentLocation();

        List<GastroLocation> gastroLocations = getGastroJson();
        mGastroLocationAdapter = new GastroLocationAdapter(getApplicationContext(), gastroLocations);
        mLocationListener = new GastroLocationListener(this, gastroLocations);

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
    private void receiveCurrentLocation() {
        // runnable to determine when the first GPS fix was received.
        Runnable waitForGpsFix = new Runnable() {
            @Override
            public void run() {
                final long startTimeMillis = System.currentTimeMillis();
                final int waitTimeMillis = 20 * 1000;
                while (mLocationFromJson == null) {
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
                mDialog.dismiss();
                MainListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        };
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mDialog = UiUtils.showMaterialProgressDialog(this, getString(R.string.please_wait),
                    getString(R.string.retrieving_gps_data));
        }
        Thread t = new Thread(waitForGpsFix);
        t.start();
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

    public final ListCallbackSingleChoice mButtonCallback = new ListCallbackSingleChoice() {

        List<GastroLocation> filteredList = new ArrayList<GastroLocation>();

        @Override
        public boolean onSelection(MaterialDialog materialDialog, View view, int selected, CharSequence charSequence) {
            mGastroLocations = getGastroJson();
            switch (selected) {

                case 0:
                    //show all
                    mGastroLocationAdapter = null;
                    mGastroLocationAdapter = new GastroLocationAdapter(getApplicationContext(), mGastroLocations);
                    break;
                case 1:


                    if (mGastroLocations != null && mGastroLocations.size() > 0) {
                        filteredList.clear();
                        for (GastroLocation gastro : mGastroLocations) {
                            //vegan declared
                            if (gastro.getVegan() == 5) {
                                filteredList.add(gastro);
                            }
                        }
                        if (filteredList.size() > 0) {
                            mGastroLocations = filteredList;
                            mGastroLocationAdapter = null;
                            mGastroLocationAdapter = new GastroLocationAdapter(getApplicationContext(), mGastroLocations);

                        }
                    }


                    break;
                default:
                    break;
            }

            mGastroLocationAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mGastroLocationAdapter);
            if (mLocationFound != null) {
                Log.e(TAG, "locationfound");
                sortGastroLocations();
            }
            materialDialog.dismiss();
            return true;
        }
    };
    /*
    TODO
    1. Add more filter options
    2. save state of checkbox - either globally with preference or just for the session and clear preference on app resume
     */

    private void filterList() {
        UiUtils.showMaterialDialogCheckboxes(MainListActivity.this, getString(R.string.filter_title_dialog),
                getResources().getStringArray(R.array.filter_checkboxes), -1, mButtonCallback);

    }
    private void requestLocationUpdates() {
        final int minTime = 3 * 60 * 1000; // e.g. 5 * 60 * 1000 (5 minutes)
        final int minDistance = 100;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        }
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
        }
    }

    private void removeLocationUpdates() {
        if (mLocationManager != null)
            mLocationManager.removeUpdates(mLocationListener);
    }

    static List<GastroLocation> createList(final InputStream inputStream) {
        final InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<ArrayList<GastroLocation>>() {
        }.getType();
        final List<GastroLocation> locationList = new Gson().fromJson(reader, listType);
        return locationList;
    }

    public void sortGastroLocations() {
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
    private class GastroLocationListener implements LocationListener {


        public GastroLocationListener(Context context, List<GastroLocation> gastroLocations) {
            mGastroLocations = gastroLocations;


        }

        @Override
        public void onLocationChanged(Location location) {

            //remove to preserve battery
            removeLocationUpdates();
            mLocationFound = location;
            if (mLocationFound != null)
                sortGastroLocations();
            mGastroLocationAdapter.notifyDataSetChanged();
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
