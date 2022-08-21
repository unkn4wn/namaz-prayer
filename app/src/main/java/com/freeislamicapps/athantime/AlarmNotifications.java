package com.freeislamicapps.athantime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesMain;
import com.freeislamicapps.athantime.ui.prayer.PrayerFragment;

import java.time.LocalTime;
import java.util.Calendar;

public class AlarmNotifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int requestcode = intent.getIntExtra("PrayerAlarm",2);

        Log.d("Received","HEALS");
        Log.d("Received",String.valueOf(requestcode));

        String hours = (String.valueOf(LocalTime.now().getHour()));
        String minutes = (String.valueOf(LocalTime.now().getMinute()));

        if(hours.length()==1) {
            hours = 0+hours;
        }

        if(minutes.length()==1) {
            minutes = 0 + minutes;
        }

        String time = hours + ":" + minutes;


    }
}
