package com.freeislamicapps.athantime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesMain;

import java.time.LocalDate;
import java.util.Calendar;

public class AlarmStart extends BroadcastReceiver {
    PrayTimesMain prayTimesMain;
    public static int requestcode;


    @Override
    public void onReceive(Context context, Intent intent) {

       // scheduleAlarm(context);
        schedulenewAlarm(context);
    }

    private void scheduleAlarm(Context context) {
         prayTimesMain = new PrayTimesMain(LocalDate.now(),context);
        Calendar calendar = Calendar.getInstance();
       // calendar = prayTimesMain.getCalendarFromPrayerTime(calendar,prayTimesMain.getLastThird(),true);

        Log.d("calendar",calendar.getTime().toString());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmStart.class);

        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    }

    private void schedulenewAlarm(Context context) {
        prayTimesMain = new PrayTimesMain(LocalDate.now(),context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int i = 0;i<prayTimesMain.getPrayerTimesList().size();i++) {

            int requestcode = i;
            Intent intent = new Intent(context, AlarmNotifications.class);
            intent.putExtra("PrayerAlarm", requestcode);

            PendingIntent pendingIntent = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_MUTABLE);


            } else {
                pendingIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, prayTimesMain.getCalendarFromPrayerTime(Calendar.getInstance(),prayTimesMain.getPrayerTimesList().get(i)).getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, prayTimesMain.getCalendarFromPrayerTime(Calendar.getInstance(),prayTimesMain.getPrayerTimesList().get(i)).getTimeInMillis(), pendingIntent);
            }
            Log.d("fajr2",String.valueOf(prayTimesMain.getCalendarFromPrayerTime(Calendar.getInstance(),prayTimesMain.getPrayerTimesList().get(i)).getTime()));
        }
    }
}
