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
        updateCardViewForSelection(selected);
        mEditor = PreferenceManager.getDefaultSharedPreferences(mMainListActivity).edit();
        mEditor.putInt(KEY_FILTER, selected);
        mEditor.commit();
        return true;
    }

    void updateCardViewForSelection(int selected) {
        switch (selected) {
            case SHOW_ALL:
                mMainListActivity.updateCardView(mAllGastroLocations);
                break;
            case COMPLETELY_VEGAN:
                if (mAllGastroLocations != null && mAllGastroLocations.size() > 0) {
                    mFilteredList.clear();
                    for (GastroLocation gastro : mAllGastroLocations) {
                        if (gastro.getVegan() == 5) {
                            mFilteredList.add(gastro);
                        }
                    }
                    if (mFilteredList.size() > 0) {
                        mMainListActivity.updateCardView(mFilteredList);
                    }
                }
                break;
            default:
                break;
        }
    }

    void setAllGastroLocations(List<GastroLocation> allGastroLocations) {
        mAllGastroLocations = allGastroLocations;
    }
}
