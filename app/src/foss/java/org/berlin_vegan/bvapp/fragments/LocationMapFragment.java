package org.berlin_vegan.bvapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.berlin_vegan.bvapp.data.Location;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DirectedLocationOverlay;
import org.osmdroid.views.overlay.Overlay;

/**
 * Alternative implementation --- using for OpenStreetMap instead of Google Maps (play services)
 */
public class LocationMapFragment extends Fragment {
    private Location mLocation;

    protected MapView mMapView;
    protected ResourceProxy mResourceProxy;
    protected Overlay mLocationOverlay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
        mMapView = new MapView(inflater.getContext(), mResourceProxy);

        mMapView.getController().setInvertedTiles(false);

        mLocationOverlay = new DirectedLocationOverlay(getContext());
        mMapView.getOverlays().add(mLocationOverlay);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);

        return mMapView;
    }

    public void setLocation(Location location) {

        IMapController mapController = mMapView.getController();
        mapController.setZoom(20);
        GeoPoint startPoint = new GeoPoint(location.getLatCoord(), location.getLongCoord());
        mapController.setCenter(startPoint);
        ((DirectedLocationOverlay) mLocationOverlay).setLocation(startPoint);

        mLocation = location;
    }

}
