package org.berlin_vegan.bvapp.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocations;
import org.berlin_vegan.bvapp.fragments.GastroDetailsFragment;

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
                        GastroDetailsFragment.OMNIVORE_VEGAN_DECLARED,
                        GastroDetailsFragment.VEGETARIAN_VEGAN_DECLARED,
                        GastroDetailsFragment.VEGAN);
                break;
            case VEGETARIAN_VEGAN:
                gastroLocations.showFiltersResult(
                        GastroDetailsFragment.VEGETARIAN_VEGAN_DECLARED,
                        GastroDetailsFragment.VEGAN);
                break;
            case VEGAN_ONLY:
                gastroLocations.showFiltersResult(
                        GastroDetailsFragment.VEGAN);
                break;
            case FAVORITES:
                gastroLocations.showFavorites();
                break;
            default:
                break;
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mMainListActivity).edit();
        editor.putInt(GastroLocations.KEY_FILTER, selected);
        editor.apply();
        return true;
    }
}
