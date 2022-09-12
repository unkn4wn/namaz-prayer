package com.freeislamicapps.athantime;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.freeislamicapps.athantime.alarm.AlarmStart;
import com.freeislamicapps.athantime.databinding.ActivityMainBinding;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.freeislamicapps.athantime.ui.intro.IntroActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private String filename,filepath,fileContent;

    public static SharedPreferences sharedPreferences;
    public static final String CHANNEL_1_ID = "prayerChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean firstStart = SharedPreferencesHelper.getValue(this,"firstStart",true);

        if (firstStart) {
            startFirstTime();
        }

        //Create notification channel and ask for permission first time
       // createNotificationChannel(this);

        setAlarm();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setSelectedItemId(R.id.navigation_settings2);





        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_qiblah, R.id.navigation_prayer, R.id.navigation_settings2)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);


       // DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

        } else {

        }
    }

    private void startFirstTime() {

        //Default values
        SharedPreferencesHelper.storeValue(this,"AsrCalculation","Shafi, Hanbali, Maliki");
        SharedPreferencesHelper.storeValue(this,"Method","Islamic Society of North America (ISNA)");
        SharedPreferencesHelper.storeValue(this,"HighLatsAdjustment","Angle-Based");
        SharedPreferencesHelper.storeValue(this,"Style","Automatic (System settings)");
        SharedPreferencesHelper.storeValue(this,"latitude","52.0");
        SharedPreferencesHelper.storeValue(this,"longitude","9.0");

        Intent intent = new Intent(MainActivity.this,IntroActivity.class);
        startActivity(intent);


        setAlarm();
    }


    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,1);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmStart.class);

        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this,20,intent,PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this,20,intent,0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }

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


    @Override
    protected void onStop() {
        super.onStop();
       setAlarm();
    }
}