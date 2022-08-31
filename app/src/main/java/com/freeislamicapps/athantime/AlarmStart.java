package com.freeislamicapps.athantime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesCalculator;

import java.time.LocalDate;
import java.util.Calendar;

public class AlarmStart extends BroadcastReceiver {
    PrayTimesCalculator prayTimesCalculator;
    public static int requestcode;


    @Override
    public void onReceive(Context context, Intent intent) {

       setAlarm(context);
        schedulenewAlarm(context);
    }

    private void setAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,1);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.DAY_OF_YEAR,1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmStart.class);

        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context,10,intent,PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context,10,intent,0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }

        Log.d("Calendar",calendar.getTime().toString());

    }

    private void schedulenewAlarm(Context context) {
        prayTimesCalculator = new PrayTimesCalculator(LocalDate.now(),context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int i = 0; i< prayTimesCalculator.getPrayerTimesList().size(); i++) {

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
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, prayTimesCalculator.getCalendarFromPrayerTime(Calendar.getInstance(), prayTimesCalculator.getPrayerTimesList().get(i)).getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, prayTimesCalculator.getCalendarFromPrayerTime(Calendar.getInstance(), prayTimesCalculator.getPrayerTimesList().get(i)).getTimeInMillis(), pendingIntent);
            }
            Log.d("fajr2",String.valueOf(prayTimesCalculator.getCalendarFromPrayerTime(Calendar.getInstance(), prayTimesCalculator.getPrayerTimesList().get(i)).getTime()));
        }
    }
}
