package com.freeislamicapps.athantime.ui.prayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.freeislamicapps.athantime.R;

import java.time.LocalTime;

public class ForegroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service","Service is running..");
                            try {
                                //TODO Notifications
                                Thread.sleep(2000);
                                String hours = (String.valueOf(LocalTime.now().getHour()));
                                String minutes = (String.valueOf(LocalTime.now().getMinute()));

                                if(hours.length()==1) {
                                    hours = 0+hours;
                                }

                                if(minutes.length()==1) {
                                    minutes = 0+minutes;
                                }
                                String time = hours + ":" + minutes;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

            final String CHANNELID = "Forecground Service ID";
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    CHANNELID,
                    CHANNELID,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
            NotificationCompat.Builder notification = null;

            notification = new NotificationCompat.Builder(this, CHANNELID)
                    .setContentText("Service is running")
                    .setContentTitle("Service enabled")
                    .setSmallIcon(R.drawable.ic_launcher_background);


            startForeground(1001, notification.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
