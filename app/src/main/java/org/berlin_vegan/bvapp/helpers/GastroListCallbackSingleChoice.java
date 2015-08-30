package org.berlin_vegan.bvapp.helpers;

import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.GastroLocations;
import org.berlin_vegan.bvapp.data.Preferences;

/**
 * Processes the selection in {@code UiUtils.showMaterialDialogCheckboxes(...)}.
 */
public class GastroListCallbackSingleChoice implements MaterialDialog.ListCallbackSingleChoice {

    private static final int OMNIVORE_VEGETARIAN_VEGAN = 0;
    private static final int VEGETARIAN_VEGAN = 1;
    private static final int VEGAN_ONLY = 2;
    private static final int FAVORITES = 3;

    private final MainListActivity mMainListActivity;

    public GastroListCallbackSingleChoice(MainListActivity mainListActivity) {
        mMainListActivity = mainListActivity;
    }

    @Override
    public boolean onSelection(MaterialDialog materialDialog, View view,
                               int selected, CharSequence charSequence) {
        final GastroLocations gastroLocations = mMainListActivity.getGastroLocations();
        switch (selected) {
            case OMNIVORE_VEGETARIAN_VEGAN:
                gastroLocations.showFiltersResult(
                        GastroLocation.OMNIVORE_VEGAN_DECLARED,
                        GastroLocation.VEGETARIAN_VEGAN_DECLARED,
                        GastroLocation.VEGAN);
                break;
            case VEGETARIAN_VEGAN:
                gastroLocations.showFiltersResult(
                        GastroLocation.VEGETARIAN_VEGAN_DECLARED,
                        GastroLocation.VEGAN);
                break;
            case VEGAN_ONLY:
                gastroLocations.showFiltersResult(
                        GastroLocation.VEGAN);
                break;
            case FAVORITES:
                gastroLocations.showFavorites();
                break;
            default:
                break;
        }
        Preferences.saveGastroFilter(mMainListActivity, selected);
        return true;
    }
}
