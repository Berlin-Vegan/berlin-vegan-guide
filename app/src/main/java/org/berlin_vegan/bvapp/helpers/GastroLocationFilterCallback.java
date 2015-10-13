package org.berlin_vegan.bvapp.helpers;

import com.afollestad.materialdialogs.MaterialDialog;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocationFilter;
import org.berlin_vegan.bvapp.data.GastroLocations;
import org.berlin_vegan.bvapp.data.Preferences;
import org.berlin_vegan.bvapp.views.GastroFilterView;

/**
 * Processes the selection from {@code GastroFilterView}
 */
public class GastroLocationFilterCallback extends MaterialDialog.ButtonCallback {
    private final MainListActivity mMainListActivity;

    public GastroLocationFilterCallback(MainListActivity mainListActivity) {
        mMainListActivity = mainListActivity;
    }
    @Override
    public void onPositive(MaterialDialog dialog) {
        final GastroFilterView filterView = (GastroFilterView) dialog.getCustomView();
        final GastroLocations gastroLocations = mMainListActivity.getGastroLocations();
        if (filterView != null) {
            final GastroLocationFilter filter = filterView.getCurrentFilter();
            gastroLocations.showFiltersResult(filter);
            Preferences.saveGastroFilter(mMainListActivity, filter);
        }
    }
}
