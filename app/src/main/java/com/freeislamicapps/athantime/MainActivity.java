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
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.freeislamicapps.athantime.PrayerTimes.HighLatsAdjustment;
import com.freeislamicapps.athantime.PrayerTimes.Method;
import com.freeislamicapps.athantime.PrayerTimes.PrayTimes;
import com.freeislamicapps.athantime.PrayerTimes.PrayTimesMain;
import com.freeislamicapps.athantime.databinding.ActivityMainBinding;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static PrayTimes prayTimes;

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

        createPrayerTimes();


        setAlarm();

        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_qiblah, R.id.navigation_prayer, R.id.navigation_settings2)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //  NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
       // DynamicColors.applyToActivitiesIfAvailable(this.getApplication());

        //   String filename = "prayertimes";
        //   String jsonFromFile = JsonHelper.getJSONFromFile(filename,this);
        //  TabTodayFragment.adhanResponse = new Gson().fromJson(jsonFromFile, AdhanResponse.class);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

        } else {

        }
    }

    private void startFirstTime() {
        Toast.makeText(this,"FIRSSTTIME",Toast.LENGTH_SHORT).show();
        saveDataBoolean("firstStart",false);

        //Default values
        saveDataString("AsrCalculation", "Shafi, Hanbali, Maliki");
        saveDataString("Method", "Islamic Society of North America (ISNA)");
        saveDataString("HighLatsAdjustment", "Angle-Based");

        //Create notification channel and ask for permission first time
        createNotificationChannel(this);
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

    private void createPrayerTimes() {
        prayTimes = new PrayTimes();
        String latitudestr = sharedPreferences.getString("latitude", "0.0");
        String longitudestr = sharedPreferences.getString("longitude", "0.0");

        prayTimes.setCoordinates(Double.parseDouble(latitudestr), Double.parseDouble(longitudestr), 0.0);

        prayTimes.setMethod(getCurrentMethod());
        prayTimes.setHighLatsAdjustment(getCurrentHighlatsadjustment());

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfYear();
        prayTimes.setDate(year, month, day);
    }

    private static HighLatsAdjustment getCurrentHighlatsadjustment() {
        switch (sharedPreferences.getString("HighLatsAdjustment", "Angle-Based")) {
            case "None":
                return HighLatsAdjustment.None;
            case "Middle of the night":
                return HighLatsAdjustment.NightMiddle;
            case "One-Seventh of the Night":
                return HighLatsAdjustment.OneSeventh;
            default:
                return HighLatsAdjustment.AngleBased;
        }
    }

    private static Method getCurrentMethod() {
        switch (sharedPreferences.getString("Method", "Islamic Society of North America (ISNA)")) {
            case "Egyptian General Authority of Survey":
                return Method.Egypt;
            case "Institute of Geophysics, University of Tehran":
                return Method.Tehran;
            case "Muslim World League":
                return Method.MWL;
            case "Umm Al-Qura University, Makkah":
                return Method.Makkah;
            case "Union des organisations islamiques de France":
                return Method.UOIF;
            case "University of Islamic Sciences, Karachi":
                return Method.Karachi;
            default:
                return Method.ISNA;
        }
    }

    public static void updateSettings() {
        String latitudestr = sharedPreferences.getString("latitude", "0.0");
        String longitudestr = sharedPreferences.getString("longitude", "0.0");
        prayTimes.setCoordinates(Double.parseDouble(latitudestr), Double.parseDouble(longitudestr), 0.0);
        prayTimes.setMethod(getCurrentMethod());
        prayTimes.setHighLatsAdjustment(getCurrentHighlatsadjustment());
    }

}