package org.berlin_vegan.bvapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

class GastroListCallbackSingleChoice implements MaterialDialog.ListCallbackSingleChoice {

    private static final String KEY_FILTER = "key_filter";

    private static final int OMNIVORE_VEGETARIAN_VEGAN = 0;
    private static final int VEGETARIAN_VEGAN = 1;
    private static final int VEGAN_ONLY = 2;

    private MainListActivity mMainListActivity;
    private List<GastroLocation> mAllGastroLocations;
    private List<GastroLocation> mFilteredList = new ArrayList<>();
    private String mQueryFilter;

    public GastroListCallbackSingleChoice(MainListActivity mainListActivity) {
        mMainListActivity = mainListActivity;
    }

    @Override
    public boolean onSelection(MaterialDialog materialDialog, View view,
                               int selected, CharSequence charSequence) {
        switch (selected) {
            case OMNIVORE_VEGETARIAN_VEGAN:
                showFiltersResult(GastroDetailsFragment.OMNIVORE_VEGAN_DECLARED, GastroDetailsFragment.VEGETARIAN_VEGAN_DECLARED, GastroDetailsFragment.VEGAN);
                break;
            case VEGETARIAN_VEGAN:
                showFiltersResult(GastroDetailsFragment.VEGETARIAN_VEGAN_DECLARED, GastroDetailsFragment.VEGAN);
                break;
            case VEGAN_ONLY:
                showFiltersResult(GastroDetailsFragment.VEGAN);
                break;
            default:
                break;
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mMainListActivity).edit();
        editor.putInt(KEY_FILTER, selected);
        editor.commit();
        return true;
    }

    void setAllGastroLocations(List<GastroLocation> allGastroLocations) {
        mAllGastroLocations = allGastroLocations;
    }

    void initializeFilterList() {
        showFiltersResult(GastroDetailsFragment.OMNIVORE_VEGAN_DECLARED, GastroDetailsFragment.VEGETARIAN_VEGAN_DECLARED, GastroDetailsFragment.VEGAN);
    }

    void showFiltersResult(int... types) {
        if (mAllGastroLocations != null && mAllGastroLocations.size() > 0) {
            mFilteredList.clear();
            for (GastroLocation gastro : mAllGastroLocations) {
                // e.g. omnivore, vegetarian and vegan, which at least declare vegan, are locations
                // with type 2, 4, and 5
                for (int type : types) {
                    if (gastro.getVegan() == type) {
                        mFilteredList.add(gastro);
                    }
                }
            }
            if (mFilteredList.size() > 0) {
                mMainListActivity.updateCardView(mFilteredList);
            }
        }
    }

    public void processQueryFilter(String query) {
        mQueryFilter = query;
        List<GastroLocation> queryFilterList = new ArrayList<>();
        for (GastroLocation gastro : mFilteredList) {
            final String gastroName = gastro.getName().toUpperCase();
            final String gastroComment = gastro.getCommentWithoutSoftHyphens().toUpperCase();
            final String queryFilter = mQueryFilter.toUpperCase();
            if (gastroName.contains(queryFilter) || gastroComment.contains(queryFilter)) {
                queryFilterList.add(gastro);
            }
        }
        mMainListActivity.updateCardView(queryFilterList);
    }

    public void resetQueryFilter() {
        mQueryFilter = "";
    }
}
