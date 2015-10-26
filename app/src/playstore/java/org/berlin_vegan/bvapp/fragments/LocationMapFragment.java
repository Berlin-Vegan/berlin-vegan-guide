package org.berlin_vegan.bvapp.fragments;


import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.berlin_vegan.bvapp.data.Location;

public class LocationMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = new LatLng(mLocation.getLatCoord(),mLocation.getLongCoord());

        //map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        map.addMarker(new MarkerOptions()
                .title(mLocation.getName())
                .position(latLng)).showInfoWindow();
    }

    public void setLocation(Location location) {
        mLocation = location;
    }
}
