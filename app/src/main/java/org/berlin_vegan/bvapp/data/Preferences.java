package org.berlin_vegan.bvapp.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;


public class Preferences {
    private static final String KEY_UNITS = "key_units";
    private static final String KEY_FILTER = "key_filter";
    private static final String KEY_GASTRO_LAST_MODIFIED = "key_gastro_last_modified";
    static final String KEY_FAVORITES = "key_favorites";

    public static boolean isMetricUnit(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Preferences.KEY_UNITS, true);
    }

    public static int getGastroFilter(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(Preferences.KEY_FILTER, 0);
    }

    public static void removeGastroFilter(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(Preferences.KEY_FILTER);
        editor.apply();

    }

    public static void saveGastroFilter(Context context, int selected) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(Preferences.KEY_FILTER, selected);
        editor.apply();
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
