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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.freeislamicapps.athantime.PrayerTimes.HighLatsAdjustment;
import com.freeislamicapps.athantime.PrayerTimes.Method;
import com.freeislamicapps.athantime.PrayerTimes.Midnight;
import com.freeislamicapps.athantime.PrayerTimes.PrayTimes;
import com.freeislamicapps.athantime.PrayerTimes.Times;
import com.freeislamicapps.athantime.databinding.ActivityMainBinding;
import com.freeislamicapps.athantime.ui.intro.IntroActivity;
import com.freeislamicapps.athantime.ui.intro.LocationFragment;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
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

        sharedPreferences = this.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart",true);

        if (firstStart) {
            startFirstTime();
        }

        //Create notification channel and ask for permission first time
       // createNotificationChannel(this);

        setAlarm();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setSelectedItemId(R.id.navigation_prayer);





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
        Toast.makeText(this,"FIRSSTTIME",Toast.LENGTH_SHORT).show();


        //Default values
        saveDataString("AsrCalculation", "Shafi, Hanbali, Maliki");
        saveDataString("Method", "Islamic Society of North America (ISNA)");
        saveDataString("HighLatsAdjustment", "Angle-Based");
        saveDataString("Style","Automatic (System settings)");

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


    public void saveDataBoolean(String savedKey, Boolean savedValue) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(savedKey, savedValue);
        editor.apply();
    }

    public void saveDataString(String savedKey, String savedValue) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedKey, savedValue);
        editor.apply();
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