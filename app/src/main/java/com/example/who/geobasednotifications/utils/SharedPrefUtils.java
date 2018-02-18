package com.example.who.geobasednotifications.utils;

import android.content.SharedPreferences;

import com.example.who.geobasednotifications.App;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefUtils {

    private static final String SHARED_PREF = "SHARED_PREF";
    private static final String MARKER_INSIDE_CIRCLE = "MARKER_INSIDE_CIRCLE";

    private static SharedPreferences getSharedPref() {
        return App.getAppContext().getSharedPreferences(SHARED_PREF,
                MODE_PRIVATE);
    }

    public static void setMarkerIsInside(boolean isInside) {
        getSharedPref().edit().putBoolean(MARKER_INSIDE_CIRCLE, isInside).apply();
    }

    public static boolean getMarkerIsInside() {
        return getSharedPref().getBoolean(MARKER_INSIDE_CIRCLE, false);
    }

}
