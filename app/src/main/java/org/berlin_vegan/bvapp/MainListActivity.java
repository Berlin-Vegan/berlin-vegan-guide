package org.berlin_vegan.bvapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainListActivity extends BaseActivity {

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //fast scroll


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<GastroLocation> gastroLocations = createList();
        GastroLocationAdapter gastroLocationAdapter = new GastroLocationAdapter(getApplicationContext(),
                gastroLocations);
        recyclerView.setAdapter(gastroLocationAdapter);
        locationListener = new GastroLocationListener(gastroLocations);

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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void removeLocationUpdates() {
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
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

        public GastroLocationListener(List<GastroLocation> gastroLocations) {
            this.gastroLocations = gastroLocations;
        }

        @Override
        public void onLocationChanged(Location location) {
            Location locationFromList = new Location("");
            float distanceInMeters;
            float distanceInKiloMeters;
            float distanceInMiles;
            float distanceRoundOnePlace;
            //remove to preserve battery
            removeLocationUpdates();

            for (int i = 0; i < gastroLocations.size(); i++) {
                GastroLocation gastroLocation = gastroLocations.get(i);
                locationFromList.setLatitude(gastroLocation.getLatCoord());
                locationFromList.setLongitude(gastroLocation.getLongCoord());
                distanceInMeters = locationFromList.distanceTo(location);
                distanceInKiloMeters = distanceInMeters / 1000;
                distanceInMiles = distanceInKiloMeters * (float) 0.621371192;
                // 1. explicit cast to float necessary, otherwise we always get x.0 values
                // 2. Math.round(1.234 * 10) / 10 = Math.round(12.34) / 10 = 12 / 10 = 1.2
                if (Locale.getDefault().getLanguage().equals(Locale.GERMAN.getLanguage())) {
                    distanceRoundOnePlace = (float) Math.round(distanceInKiloMeters * 10) / 10;
                } else {
                    distanceRoundOnePlace = (float) Math.round(distanceInMiles * 10) / 10;
                }
                gastroLocation.setDistToCurLoc(distanceRoundOnePlace);
            }
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
