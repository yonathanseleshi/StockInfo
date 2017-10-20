package com.yseleshi.stockinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dell laptop on 8/30/2017.
 */

public class Constants {

    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_PERCENT_CHANGE = "PREF_MIN_PERCENT_CHANGE";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    public static final String PREF_COLOR_CHOICE = "PREF_COLOR_CHOICE";

    public static void savePrefs(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key, value);
        ed.apply();
    }
}
