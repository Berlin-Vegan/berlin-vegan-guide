package org.berlin_vegan.bvapp.fragments.LocationsOverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.berlin_vegan.bvapp.activities.LocationDetailActivity;
import org.berlin_vegan.bvapp.activities.LocationsOverviewActivity;
import org.berlin_vegan.bvapp.data.Location;
import org.berlin_vegan.bvapp.data.Locations;
import org.berlin_vegan.bvapp.helpers.UiUtils;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

/**
 * Created by micu on 02/02/16.
 */

public class LocationMapOverviewFragment extends Fragment {

    protected MapView mMapView;
    protected ResourceProxy mResourceProxy;

    protected ItemizedIconOverlay<LocationOverlayItem> mLocationOverlay;
    protected ArrayList<LocationOverlayItem> mOverlayItemList;

    public LocationMapOverviewFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
        mMapView = new MapView(inflater.getContext(), mResourceProxy);

        mMapView.getController().setInvertedTiles(false);

        mMapView.setTileSource(UiUtils.GOOGLE_MAPS_TILE);
        mMapView.setMultiTouchControls(true);

        mOverlayItemList = new ArrayList<>();

        // inner class seems HACKy here ....
        OnItemGestureListener<LocationOverlayItem> myOnItemGestureListener
                = new OnItemGestureListener<LocationOverlayItem>() {

            @Override
            public boolean onItemLongPress(int arg0, LocationOverlayItem arg1) {
                // TODO
                return false;
            }

            @Override
            public boolean onItemSingleTapUp(int index, LocationOverlayItem item) {
                final Intent intent = new Intent(getContext(), LocationDetailActivity.class);
                intent.putExtra(LocationDetailActivity.EXTRA_LOCATION, item.getCorrespondingLocation());
                startActivity(intent);
                return true;
            }
        };

        mLocationOverlay = new ItemizedIconOverlay<>(getContext(), mOverlayItemList, myOnItemGestureListener);
        mMapView.getOverlays().add(mLocationOverlay);

        IMapController mapController = mMapView.getController();
        mapController.setZoom(10);

        // set Center of the map to Alex
        GeoPoint gPoint = new GeoPoint(52.521918, 13.413215);
        mapController.setCenter(gPoint);

        Locations locations = ((LocationsOverviewActivity) getActivity()).getLocations();

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            gPoint = new GeoPoint(location.getLatCoord(), location.getLongCoord());
            LocationOverlayItem mMarkerItem = new LocationOverlayItem(location.getName(), location.getVegan().toString(), gPoint, location);
            mLocationOverlay.addItem(mMarkerItem);
        }

        return mMapView;
    }

    // inner class seems HACKy here ....
    class LocationOverlayItem extends OverlayItem {
        private Location mCorrespondingLocation;

        public LocationOverlayItem(final String aTitle, final String aSnippet, final IGeoPoint aGeoPoint, Location correspondingLocation) {
            super(aTitle, aSnippet, aGeoPoint);

            mCorrespondingLocation = correspondingLocation;
        }

        public Location getCorrespondingLocation() {
            return mCorrespondingLocation;
        }
    }
}
