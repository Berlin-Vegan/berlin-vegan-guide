package org.berlin_vegan.bvapp.data;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import org.berlin_vegan.bvapp.activities.MainListActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * class, that holds an object of all gastro locations, filtered gastro locations, and the gastro locations,
 * which are currently presented to the user. Latter includes searching through the gastro locations lists.
 */
public class GastroLocations {
    private static final String KEY_FAVORITES = "key_favorites";
    public static final String KEY_UNITS = "key_units";
    public static final String KEY_FILTER = "key_filter";

    private final MainListActivity mMainListActivity;
    private static SharedPreferences sSharedPreferences;
    private Location mLocationFound;
    /**
     * holds all locations. used to create the filtered lists
     */
    private List<GastroLocation> mAll = new ArrayList<>();
    /**
     * holds filtered locations. can be searched with {@code mQueryFilter}
     */
    private List<GastroLocation> mFiltered = new ArrayList<>();
    /**
     * holds favorite locations
     */
    private final List<GastroLocation> mFavorites = new ArrayList<>();
    private static Set<String> sFavoriteIDs = new HashSet<>();
    private boolean mFavoritesCurrentlyShown;
    /**
     * holds the locations, that are presented to the user in {@code MainListActivity}
     */
    private List<GastroLocation> mShown = new ArrayList<>();
    private String mQueryFilter;

    public GastroLocations(MainListActivity mainListActivity) {
        mMainListActivity = mainListActivity;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mMainListActivity);
        sFavoriteIDs = sSharedPreferences.getStringSet(KEY_FAVORITES, new HashSet<String>());
    }

    private void sortByDistance() {
        if (mLocationFound == null) {
            return;
        }
        Location locationFromJson = new Location("DummyProvider");
        float distanceInMeters;
        float distanceInKiloMeters;
        float distanceInMiles;
        float distanceRoundOnePlace;
        for (int i = 0; i < mShown.size(); i++) {
            GastroLocation gastroLocation = mShown.get(i);
            locationFromJson.setLatitude(gastroLocation.getLatCoord());
            locationFromJson.setLongitude(gastroLocation.getLongCoord());
            distanceInMeters = locationFromJson.distanceTo(mLocationFound);
            distanceInKiloMeters = distanceInMeters / 1000;
            distanceInMiles = distanceInKiloMeters * (float) 0.621371192;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mMainListActivity);
            // 1. explicit cast to float necessary, otherwise we always get x.0 values
            // 2. Math.round(1.234 * 10) / 10 = Math.round(12.34) / 10 = 12 / 10 = 1.2
            if (sharedPreferences.getBoolean(KEY_UNITS, true)) {
                distanceRoundOnePlace = (float) Math.round(distanceInKiloMeters * 10) / 10;
            } else {
                distanceRoundOnePlace = (float) Math.round(distanceInMiles * 10) / 10;
            }
            gastroLocation.setDistToCurLoc(distanceRoundOnePlace);
        }
        Collections.sort(mShown);
    }

    public void showFiltersResult(int... types) {
        mFavoritesCurrentlyShown = false;
        if (mAll != null && !mAll.isEmpty()) {
            mFiltered.clear();
            for (GastroLocation gastro : mAll) {
                // e.g. omnivore, vegetarian and vegan, which at least declare vegan, are locations
                // with type 2, 4, and 5
                for (int type : types) {
                    if (gastro.getVegan() == type) {
                        mFiltered.add(gastro);
                    }
                }
            }
            if (!mFiltered.isEmpty()) {
                mShown = new ArrayList<>(mFiltered);
                updateLocationAdapter();
            }
        }
    }

    // --------------------------------------------------------------------
    // favorites

    public static boolean containsFavorite(String id) {
        return sFavoriteIDs.contains(id);
    }

    public static void addFavorite(String id) {
        sFavoriteIDs.add(id);
        commitFavoritesPreferences();
    }

    public static void removeFavorite(String id) {
        sFavoriteIDs.remove(id);
        commitFavoritesPreferences();
    }

    private static void commitFavoritesPreferences() {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putStringSet(KEY_FAVORITES, sFavoriteIDs);
        editor.apply();
    }

    public void showFavorites() {
        mFavoritesCurrentlyShown = true;
        mFavorites.clear();
        for (GastroLocation gastro : mAll) {
            if (sFavoriteIDs.contains(gastro.getId())) {
                mFavorites.add(gastro);
            }
        }
        mShown = new ArrayList<>(mFavorites);
        updateLocationAdapter();
    }

    // --------------------------------------------------------------------
    // query

    public void processQueryFilter(String query) {
        mFavoritesCurrentlyShown = false;
        mQueryFilter = query;
        final List<GastroLocation> queryFilteredList = new ArrayList<>();
        for (GastroLocation gastro : mFiltered) {
            final String gastroName = gastro.getName().toUpperCase();
            final String gastroComment = gastro.getCommentWithoutSoftHyphens().toUpperCase();
            final String queryFilter = mQueryFilter.toUpperCase();
            if (gastroName.contains(queryFilter) || gastroComment.contains(queryFilter)) {
                queryFilteredList.add(gastro);
            }
        }
        mShown = new ArrayList<>(queryFilteredList);
        updateLocationAdapter();
    }

    public void resetQueryFilter() {
        mQueryFilter = "";
    }

    // --------------------------------------------------------------------
    // updating

    public void updateLocationAdapter() {
        sortByDistance();
        mMainListActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainListActivity.getGastroLocationAdapter().notifyDataSetChanged();
            }
        });
    }

    public void updateLocationAdapter(Location locationFound) {
        mLocationFound = locationFound;
        updateLocationAdapter();
    }

    // --------------------------------------------------------------------
    // getters & setters

    public void set(List<GastroLocation> gastroLocations) {
        mAll = mAll.isEmpty() ? gastroLocations : throw_();
        mShown = new ArrayList<>(mAll);
        updateLocationAdapter();
        // has to be set after {@code updateLocationAdapter()} so that {@code mShown} is already sorted
        mFiltered = new ArrayList<>(mShown);
    }

    private List<GastroLocation> throw_() {
        throw new RuntimeException("gastro locations are already set");
    }

    public int size() {
        return mShown.size();
    }

    public GastroLocation get(int i) {
        return mShown.get(i);
    }

    public boolean isFavoritesCurrentlyShown() {
        return mFavoritesCurrentlyShown;
    }
}
