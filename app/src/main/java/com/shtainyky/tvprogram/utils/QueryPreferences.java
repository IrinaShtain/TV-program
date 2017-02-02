package com.shtainyky.tvprogram.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
    private static final String PREF_FIRST_INSTALLATION = "firstInstallation";
    private static final String PREF_CHANNELS_LOADED = "CHANNEL_LOADED";
    private static final String PREF_CATEGORIES_LOADED = "CATEGORIES_LOADED";
    private static final String PREF_PROGRAMS_LOADED = "PROGRAMS_LOADED";
    private static final String PREF_SERVICE_WORKING = "PREF_SERVICE_WORKED";
    private static final String PREF_FIRST_TIME_SERVICE = "PREF_FIRST_TIME_SERVICE";
    private static final String PREF_DAY_UPDATING = "PREF_DAY_UPDATING";

    public static boolean getShouldUpdateDayProgram(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_DAY_UPDATING, false);
    }

    public static void setShouldUpdateDayProgram(Context context, boolean isFirstInstallation) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_DAY_UPDATING, isFirstInstallation)
                .apply();
    }

    public static boolean getStoredFirstTimeService(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_FIRST_TIME_SERVICE, true);
    }

    public static void setStoredFirstTimeService(Context context, boolean isFirstInstallation) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_FIRST_TIME_SERVICE, isFirstInstallation)
                .apply();
    }

    public static int getCountLoadedDays(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_SERVICE_WORKING, 0);
    }

    public static void setCountLoadedDays(Context context, int count) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_SERVICE_WORKING, count)
                .apply();
    }

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
