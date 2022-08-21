package com.freeislamicapps.athantime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesMain;
import com.freeislamicapps.athantime.ui.prayer.PrayerFragment;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;
import com.google.android.gms.location.Priority;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

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
        PrayTimesMain prayTimesMain = new PrayTimesMain(LocalDate.now(),context);


        if(time.equals(prayTimesMain.getPrayerTimesList().get(requestcode))) {
            if(switchPrayer.get(requestcode)) {
                currentPrayer = getCurrentPrayer(requestcode);
                createNotificationChannel(context);
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
                .setSmallIcon(R.drawable.cloud1)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        notificationManagerCompat.notify(1,notification);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_1_ID;
            String description = CHANNEL_1_ID;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
