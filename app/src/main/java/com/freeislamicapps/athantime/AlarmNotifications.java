package com.freeislamicapps.athantime;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesCalculator;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AlarmNotifications extends BroadcastReceiver {
    String currentPrayer;
    public static final String CHANNEL_1_ID = "prayerChannel";
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        int requestcode = intent.getIntExtra("PrayerAlarm",2);
        ArrayList<Boolean> switchPrayer = new ArrayList<>();
        switchPrayer.add(sharedPreferences.getBoolean("Fajr_Sound",false));
        switchPrayer.add(sharedPreferences.getBoolean("Sunrise_Sound",false));
        switchPrayer.add(sharedPreferences.getBoolean("Dhuhr_Sound",false));
        switchPrayer.add(sharedPreferences.getBoolean("Asr_Sound",false));
        switchPrayer.add(sharedPreferences.getBoolean("Maghrib_Sound",false));
        switchPrayer.add(sharedPreferences.getBoolean("Ishaa_Sound",false));

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
        PrayTimesCalculator prayTimesCalculator = new PrayTimesCalculator(LocalDate.now(),context);


        if(time.equals(prayTimesCalculator.getPrayerTimesList().get(requestcode))) {
            if(switchPrayer.get(requestcode)) {
                currentPrayer = getCurrentPrayer(requestcode);
                showNotification(context);
            }
        }


    }

    private String getCurrentPrayer(int requestcode) {
        switch (requestcode) {
            case 0:
                return "Fajr";
            case 1:
                return "Sunrise";
            case 2:
                return "Dhuhr";
            case 3:
                return "Asr";
            case 4:
                return "Maghrib";
            default:
                return "Ishaa";
        }
    }

    private void showNotification(Context context) {
        String contentTitle = "Reminder";
        String contentText = "Its Time for " + currentPrayer + "!";
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.symbol_arrow_left)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        notificationManagerCompat.notify(1,notification);
    }


}
