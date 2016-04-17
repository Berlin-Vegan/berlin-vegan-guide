package org.berlin_vegan.bvapp.listeners;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import org.berlin_vegan.bvapp.activities.LocationsOverviewActivity;
import org.berlin_vegan.bvapp.data.Locations;

public class CustomLocationListener implements LocationListener {

    private static final String TAG = "CustomLocationListener";

    private final LocationsOverviewActivity mLocationListActivity;
    private final Locations mLocations;

    public CustomLocationListener(LocationsOverviewActivity locationListActivity, Locations locations) {
        mLocationListActivity = locationListActivity;
        mLocations = locations;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "location found: " + location.toString());
        //remove to preserve battery
        mLocationListActivity.removeGpsLocationUpdates();
        mLocationListActivity.setLocationFound(location);
        mLocations.updateLocationAdapter(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // nothing to do
    }

    @Override
    public void onProviderEnabled(String provider) {
        // nothing to do
    }

    @Override
    public void onProviderDisabled(String provider) {
        // nothing to do
    }
}
