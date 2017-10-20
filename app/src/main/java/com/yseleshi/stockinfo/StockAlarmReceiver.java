package com.yseleshi.stockinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class StockAlarmReceiver extends BroadcastReceiver {


    public final static String TAG = "StockAlarmReceiver";

    public static final String ACTION_UPDATE_EARTHQUAKE_ALARM = "com.yseleshi.stock.ACTION_UPDATE_EARTHQUAKE_ALARM";

    public StockAlarmReceiver() {     }


    @Override
    public void onReceive(Context context, Intent intent) {


        // TODO: This method is called when the BroadcastReceiver is receiving
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(context);
        String minimumPercentChange = prefs.getString(Constants.PREF_MIN_PERCENT_CHANGE, "3");
        Log.d(TAG, "onReceive minimumPercentChange: " + minimumPercentChange);
        String color_choice = prefs.getString(Constants.PREF_COLOR_CHOICE, "black");
        StockService.startRefreshStocks(context, minimumPercentChange, color_choice);


    }
}
