package com.freeislamicapps.athantime.alarm;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesCalculator;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AlarmNotifications extends BroadcastReceiver {
    String currentPrayer;
    public static final String CHANNEL_1_ID = "prayerChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("PrayerAlarm", 2);
        ArrayList<Boolean> switchPrayer = new ArrayList<>();
        switchPrayer.add(SharedPreferencesHelper.getValue(context,"Fajr_Sound", false));
        switchPrayer.add(SharedPreferencesHelper.getValue(context,"Sunrise_Sound", false));
        switchPrayer.add(SharedPreferencesHelper.getValue(context,"Dhuhr_Sound", false));
        switchPrayer.add(SharedPreferencesHelper.getValue(context,"Asr_Sound", false));
        switchPrayer.add(SharedPreferencesHelper.getValue(context,"Maghrib_Sound", false));
        switchPrayer.add(SharedPreferencesHelper.getValue(context,"Ishaa_Sound", false));

        Log.d("Received", String.valueOf(requestCode));

        String hours = (String.valueOf(LocalTime.now().getHour()));
        String minutes = (String.valueOf(LocalTime.now().getMinute()));

        if (hours.length() == 1) {
            hours = 0 + hours;
        }

        if (minutes.length() == 1) {
            minutes = 0 + minutes;
        }

        String time = hours + ":" + minutes;
        PrayTimesCalculator prayTimesCalculator = new PrayTimesCalculator(LocalDate.now(), context);

        if (time.equals(prayTimesCalculator.getPrayerTimesList().get(requestCode))) {
            System.out.println("AFTER FIRST IF");
            if (switchPrayer.get(requestCode)) {
                System.out.println("AFTER SECOND IF");
                currentPrayer = getCurrentPrayer(context,requestCode);

                showNotification(context);
            }
        }
        System.out.println("NOTIFICAITO AFTER SHOWED");


    }

    private String getCurrentPrayer(Context context,int requestCode) {
        switch (requestCode) {
            case 0:
                return context.getResources().getString(R.string.fajr);
            case 1:
                return context.getResources().getString(R.string.sunrise);
            case 2:
                return context.getResources().getString(R.string.dhuhr);
            case 3:
                return context.getResources().getString(R.string.asr);
            case 4:
                return context.getResources().getString(R.string.maghrib);
            default:
                return context.getResources().getString(R.string.ishaa);
        }
    }

    private void showNotification(Context context) {
        String contentTitle = context.getResources().getString(R.string.reminder);
        String contentText = currentPrayer + " "+  context.getResources().getString(R.string.time)+"!";
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground2)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        notificationManagerCompat.notify(1, notification);
    }


}
