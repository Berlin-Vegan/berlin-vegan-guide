package org.berlin_vegan.bvapp.helpers;

import com.afollestad.materialdialogs.MaterialDialog;

import org.berlin_vegan.bvapp.activities.LocationListActivity;
import org.berlin_vegan.bvapp.data.GastroLocationFilter;
import org.berlin_vegan.bvapp.data.Locations;
import org.berlin_vegan.bvapp.data.Preferences;
import org.berlin_vegan.bvapp.views.GastroFilterView;

/**
 * Processes the selection from {@code GastroFilterView}
 */
public class GastroLocationFilterCallback extends MaterialDialog.ButtonCallback {
    private final LocationListActivity mLocationListActivity;

    public GastroLocationFilterCallback(LocationListActivity locationListActivity) {
        mLocationListActivity = locationListActivity;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        final GastroFilterView filterView = (GastroFilterView) dialog.getCustomView();
        final Locations locations = mLocationListActivity.getLocations();
        if (filterView != null) {
            final GastroLocationFilter filter = filterView.getCurrentFilter();
            locations.showFiltersResult(filter);
            Preferences.saveGastroFilter(mLocationListActivity, filter);
        }
    }
}
