package org.berlin_vegan.bvapp.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;


public class Preferences {
    private static final String KEY_UNITS = "key_units";
    private static final String KEY_GASTRO_FILTER = "key_gastro_filter";
    private static final String KEY_GASTRO_LAST_MODIFIED = "key_gastro_last_modified";
    static final String KEY_FAVORITES = "key_favorites";

    public static boolean isMetricUnit(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Preferences.KEY_UNITS, true);
    }


    public static void removeGastroFilter(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(Preferences.KEY_GASTRO_FILTER);
        editor.apply();

    }


    public static void saveGastroFilter(Context context, GastroLocationFilter filter) {
        String stringFilter = new Gson().toJson(filter);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Preferences.KEY_GASTRO_FILTER, stringFilter);
        editor.apply();
    }

    public static GastroLocationFilter getGastroFilter(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String filterJson = prefs.getString(Preferences.KEY_GASTRO_FILTER, null);
        if (filterJson == null) {
            return new GastroLocationFilter();
        }
        return new Gson().fromJson(filterJson, GastroLocationFilter.class);
    }


    public static void saveFavorites(Context context, Set<String> favoriteIDs) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putStringSet(KEY_FAVORITES, favoriteIDs);
        editor.apply();
    }

    public static Set<String> getFavorites(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getStringSet(Preferences.KEY_FAVORITES, new HashSet<String>());
    }

    /**
     * return last modified date of the gastro location database, in milliseconds
     *
     * @param context
     * @return date in milliseconds or 0 if not set
     */
    public static long getGastroLastModified(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(Preferences.KEY_GASTRO_LAST_MODIFIED, 0);
    }

    public static void saveGastroLastModified(Context context, long lastModified) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(Preferences.KEY_GASTRO_LAST_MODIFIED, lastModified);
        editor.apply();
    }
}
