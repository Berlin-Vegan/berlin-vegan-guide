package org.berlin_vegan.bvapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

class GastroListCallbackSingleChoice implements MaterialDialog.ListCallbackSingleChoice {

    private static final String KEY_FILTER = "key_filter";

    private static final int SHOW_ALL = 0;
    private static final int COMPLETELY_VEGAN = 1;
    private static final int VEGAN_VEGETARIAN = 2;

    private MainListActivity mMainListActivity;
    private SharedPreferences.Editor mEditor;
    private List<GastroLocation> mAllGastroLocations;
    private List<GastroLocation> mFilteredList = new ArrayList<>();

    public GastroListCallbackSingleChoice(MainListActivity mainListActivity, List<GastroLocation> allGastroLocations) {
        mMainListActivity = mainListActivity;
        setAllGastroLocations(allGastroLocations);
    }

    @Override
    public boolean onSelection(MaterialDialog materialDialog, View view,
                               int selected, CharSequence charSequence) {
        switch (selected) {
            case SHOW_ALL:
                mMainListActivity.updateCardView(mAllGastroLocations);
                break;
            case COMPLETELY_VEGAN:
                showFiltersResult(GastroDetailsFragment.VEGAN);
                break;
            case VEGAN_VEGETARIAN:
                showFiltersResult(GastroDetailsFragment.VEGETARIAN, GastroDetailsFragment.VEGETARIAN_VEGAN, GastroDetailsFragment.VEGAN);
                break;
            default:
                break;
        }
        mEditor = PreferenceManager.getDefaultSharedPreferences(mMainListActivity).edit();
        mEditor.putInt(KEY_FILTER, selected);
        mEditor.commit();
        return true;
    }

    void setAllGastroLocations(List<GastroLocation> allGastroLocations) {
        mAllGastroLocations = allGastroLocations;
    }

    void showFiltersResult(int... type) {
        if (mAllGastroLocations != null && mAllGastroLocations.size() > 0) {
            mFilteredList.clear();
            for (GastroLocation gastro : mAllGastroLocations) {

                if (type.length > 1) {
                    for (int t : type) {
                        if (gastro.getVegan() == t) {
                            mFilteredList.add(gastro);
                        }
                    }
                } else {
                    if (gastro.getVegan() == type[0]) {
                        mFilteredList.add(gastro);
                    }
                }
            }
            if (mFilteredList.size() > 0) {
                mMainListActivity.updateCardView(mFilteredList);
            }
        }
    }
}
