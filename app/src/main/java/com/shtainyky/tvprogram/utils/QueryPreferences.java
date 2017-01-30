package com.shtainyky.tvprogram.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
    private static final String PREF_FIRST_INSTALLATION = "firstInstallation";
    private static final String PREF_CHANNELS_LOADED = "CHANNEL_LOADED";
    private static final String PREF_CATEGORIES_LOADED = "CATEGORIES_LOADED";
    private static final String PREF_PROGRAMS_LOADED = "PROGRAMS_LOADED";

    public static boolean getStoredFirstInstallation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_FIRST_INSTALLATION, true);
    }

    public static void setStoredFirstInstallation(Context context, boolean isFirstInstallation) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_FIRST_INSTALLATION, isFirstInstallation)
                .apply();
    }
    public static boolean areChannelsLoaded(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_CHANNELS_LOADED, false);
    }

    public static void setChannelLoaded(Context context, boolean isLoaded) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_CHANNELS_LOADED, isLoaded)
                .apply();
    }
    public static boolean areCategoriesLoaded(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_CATEGORIES_LOADED, false);
    }

    public static void setCategoryLoaded(Context context, boolean isLoaded) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_CATEGORIES_LOADED, isLoaded)
                .apply();
    }
    public static boolean areProgramLoaded(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_PROGRAMS_LOADED, false);
    }

    public static void setProgramLoaded(Context context, boolean isLoaded) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_PROGRAMS_LOADED, isLoaded)
                .apply();
    }
}
