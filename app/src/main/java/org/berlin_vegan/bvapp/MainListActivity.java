package org.berlin_vegan.bvapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainListActivity extends BaseActivity {

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private GastroLocationAdapter mGastroLocationAdapter;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocationFromList;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        setTitle(getString(R.string.app_name) + " " + getString(R.string.guide));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //fast scroll


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        initLocation();
        List<GastroLocation> gastroLocations = createList();
        mGastroLocationAdapter = new GastroLocationAdapter(getApplicationContext(), gastroLocations);
        recyclerView.setAdapter(mGastroLocationAdapter);
        mLocationListener = new GastroLocationListener(this, gastroLocations);
    }

    private void initLocation() {
        // runnable to determine when the first GPS fix was received.
        Runnable showProgressDialog = new Runnable() {
            @Override
            public void run() {
                final long startTimeMillis = System.currentTimeMillis();
                final int waitTimeMillis = 20 * 1000;
                while (mLocationFromList == null) {
                    // wait for first GPS fix (do nothing)
                    if ((System.currentTimeMillis() - startTimeMillis) > waitTimeMillis) {
                        MainListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.showMaterialDialog(MainListActivity.this, getString(R.string.error), getString(R.string.no_gps_data));
                            }
                        });
                        break;
                    }
                }
                mDialog.dismiss();
            }
        };
        mDialog = UiUtils.showMaterialProgressDialog(this, getString(R.string.please_wait), getString(R.string.retrieving_gps_data));
        Thread t = new Thread(showProgressDialog);
        t.start();
    }

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

    private List<GastroLocation> createList() {
        final InputStream inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
        final InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<ArrayList<GastroLocation>>() {
        }.getType();
        final List<GastroLocation> locationList = new Gson().fromJson(reader, listType);
        return locationList;
    }

    private class GastroLocationListener implements LocationListener {

        private List<GastroLocation> gastroLocations;
        private Context context;
        private SharedPreferences sharedPreferences;

        public GastroLocationListener(Context context, List<GastroLocation> gastroLocations) {
            this.gastroLocations = gastroLocations;
            this.context = context;
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLocationFromList = new Location("");
            float distanceInMeters;
            float distanceInKiloMeters;
            float distanceInMiles;
            float distanceRoundOnePlace;
            //remove to preserve battery
            removeLocationUpdates();

            for (int i = 0; i < gastroLocations.size(); i++) {
                GastroLocation gastroLocation = gastroLocations.get(i);
                mLocationFromList.setLatitude(gastroLocation.getLatCoord());
                mLocationFromList.setLongitude(gastroLocation.getLongCoord());
                distanceInMeters = mLocationFromList.distanceTo(location);
                distanceInKiloMeters = distanceInMeters / 1000;
                distanceInMiles = distanceInKiloMeters * (float) 0.621371192;
                // 1. explicit cast to float necessary, otherwise we always get x.0 values
                // 2. Math.round(1.234 * 10) / 10 = Math.round(12.34) / 10 = 12 / 10 = 1.2
                if (sharedPreferences.getBoolean("key_units", true)) {
                    distanceRoundOnePlace = (float) Math.round(distanceInKiloMeters * 10) / 10;
                } else {
                    distanceRoundOnePlace = (float) Math.round(distanceInMiles * 10) / 10;
                }
                gastroLocation.setDistToCurLoc(distanceRoundOnePlace);
            }
            Collections.sort(gastroLocations);
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
