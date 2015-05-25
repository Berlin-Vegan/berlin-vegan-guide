package org.berlin_vegan.bvapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

class GastroLocationListener implements LocationListener {

    private static final String TAG = "GastroLocationListener";

    private MainListActivity mMainListActivity;
    private GastroLocations mGastroLocations;

    public GastroLocationListener(MainListActivity mainListActivity, GastroLocations gastroLocations) {
        mMainListActivity = mainListActivity;
        mGastroLocations = gastroLocations;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "location found: " + location.toString());
        //remove to preserve battery
        mMainListActivity.removeLocationUpdates();
        mMainListActivity.setLocationFound(location);
        mGastroLocations.updateLocationAdapter(location);
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
