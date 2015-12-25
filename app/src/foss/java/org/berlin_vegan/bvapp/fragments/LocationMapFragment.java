package org.berlin_vegan.bvapp.fragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.berlin_vegan.bvapp.R;
import org.berlin_vegan.bvapp.data.Location;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


/**
 * Alternative implementation --- using for OpenStreetMap instead of Google Maps (play services)
 */
public class LocationMapFragment extends Fragment {
    protected MapView mMapView;
    protected ResourceProxy mResourceProxy;

    protected ItemizedIconOverlay mLocationOverlay;
    protected ArrayList<OverlayItem> mOverlayItemList;
    protected OverlayItem mMarkerItem;
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
        mMapView = new MapView(inflater.getContext(), mResourceProxy);

        mMapView.getController().setInvertedTiles(false);

        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);

        mOverlayItemList = new ArrayList<OverlayItem>();
        mLocationOverlay = new ItemizedIconOverlay(getContext(), mOverlayItemList, null);
        mMapView.getOverlays().add(mLocationOverlay);

        return mMapView;
    }

    public void setLocation(Location location) {

        IMapController mapController = mMapView.getController();
        mapController.setZoom(16);
        GeoPoint gPoint = new GeoPoint(location.getLatCoord(), location.getLongCoord());
        mapController.setCenter(gPoint);

        mMarkerItem = new OverlayItem(location.getName(), location.getVegan().toString(), gPoint);
        Drawable marker = getResources().getDrawable(R.mipmap.ic_place_white_24dp);
        marker.setColorFilter(getResources().getColor(R.color.theme_primary), PorterDuff.Mode.SRC_ATOP);
        mMarkerItem.setMarker(marker);
        mLocationOverlay.addItem(mMarkerItem);

        mLocation = location;
    }

}
