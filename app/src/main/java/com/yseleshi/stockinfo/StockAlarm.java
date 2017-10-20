package com.yseleshi.stockinfo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by dell laptop on 9/2/2017.
 */

public class StockAlarm {


    private final String TAG = "StockService";
    Context context;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;


    public StockAlarm(Context context) {

        this.context = context;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // set up the intent to fire when the alarm goes off.
         String ALARM_ACTION = StockAlarmReceiver.ACTION_UPDATE_EARTHQUAKE_ALARM;
        Intent intentToFire = new Intent(ALARM_ACTION);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intentToFire, 0);

    }

    public void setUpAlarms() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int updateFreq = Integer.parseInt(prefs.getString(Constants.PREF_UPDATE_FREQ, "60"));
        boolean autoUpdateChecked = prefs.getBoolean(Constants.PREF_AUTO_UPDATE, false);

        if (autoUpdateChecked) {
            long updateInterval = updateFreq * 60 * 1000;
            // Will not go off if the device is in deep sleep but will still go off if screen
            // is dim or off if device not asleep
            int alarmType = AlarmManager.ELAPSED_REALTIME;


            long timeToRefresh = SystemClock.elapsedRealtime() + updateInterval;
            Log.d(TAG, "setUpAlarms:  updateFreq " + updateFreq + " timeToRefresh: " + timeToRefresh
                    + " alarmType: " + alarmType);

            // Cancel old alarm
            cancelAlarms();

            // Set the alarm.
            alarmManager.setInexactRepeating(alarmType, timeToRefresh, updateInterval, alarmIntent);
        }
        else {
            Log.d(TAG, "setUpAlarms: ! autoUpdateChecked , cancel alarm");
            cancelAlarms();

        }
    }


    public void cancelAlarms(){
        alarmManager.cancel(alarmIntent);
    }
}
