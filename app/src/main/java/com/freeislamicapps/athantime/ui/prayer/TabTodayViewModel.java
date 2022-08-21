package com.freeislamicapps.athantime.ui.prayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.freeislamicapps.athantime.PrayerTimes.HighLatsAdjustment;
import com.freeislamicapps.athantime.PrayerTimes.Method;
import com.freeislamicapps.athantime.PrayerTimes.PrayTimes;
import com.freeislamicapps.athantime.PrayerTimes.PrayTimesMain;
import com.freeislamicapps.athantime.PrayerTimes.Times;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;

public class TabTodayViewModel extends AndroidViewModel {

    private final MutableLiveData<String> fajrTime;
    private final MutableLiveData<String> sunriseTime;
    private final MutableLiveData<String> dhuhrTime;
    private final MutableLiveData<String> asrTime;
    private final MutableLiveData<String> maghribTime;
    private final MutableLiveData<String> ishaaTime;

    private final MutableLiveData<Boolean> fajrSound;
    private final MutableLiveData<Boolean> sunriseSound;
    private final MutableLiveData<Boolean> dhuhrSound;
    private final MutableLiveData<Boolean> asrSound;
    private final MutableLiveData<Boolean> maghribSound;
    private final MutableLiveData<Boolean> ishaaSound;

    private final MutableLiveData<ArrayList<LocalTime>> prayerTimesLocalTime;
    ArrayList<String> prayerTimesLocalTimeString;

    SharedPreferences sharedPreferences;


    public TabTodayViewModel(Application application) {
        super(application);
        fajrTime = new MutableLiveData<>();
        sunriseTime = new MutableLiveData<>();
        dhuhrTime = new MutableLiveData<>();
        asrTime = new MutableLiveData<>();
        maghribTime = new MutableLiveData<>();
        ishaaTime = new MutableLiveData<>();

        fajrSound = new MutableLiveData<>();
        sunriseSound = new MutableLiveData<>();
        dhuhrSound = new MutableLiveData<>();
        asrSound = new MutableLiveData<>();
        maghribSound = new MutableLiveData<>();
        ishaaSound = new MutableLiveData<>();

        prayerTimesLocalTime = new MutableLiveData<>();

    }

    public void init() {
        PrayTimesMain prayTimesMain = new PrayTimesMain(LocalDate.now(), getApplication().getApplicationContext());
        PrayTimes prayTimes = prayTimesMain.getPrayTimes();
        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);

        //TODO
        // Log.d("prayadj",String.valueOf(prayTimes.getTimeZone()));

        fajrTime.setValue(prayTimes.getTime(Times.Fajr));
        sunriseTime.setValue(prayTimes.getTime(Times.Sunrise));
        dhuhrTime.setValue(prayTimes.getTime(Times.Dhuhr));
        if (sharedPreferences.getString("AsrCalculation", "Shafi").equals("Shafi, Hanbali, Maliki")) {
            asrTime.setValue(prayTimes.getTime(Times.AsrShafi));
        } else {
            asrTime.setValue(prayTimes.getTime(Times.AsrHanafi));
        }
        maghribTime.setValue(prayTimes.getTime(Times.Maghrib));
        ishaaTime.setValue(prayTimes.getTime(Times.Ishaa));


        if (sharedPreferences.getString("AsrCalculation", "Shafi").equals("Shafi, Hanbali, Maliki")) {
            prayerTimesLocalTimeString = new ArrayList<String>(Arrays.asList(prayTimes.getTime(Times.Fajr),prayTimes.getTime(Times.Sunrise),prayTimes.getTime(Times.Dhuhr),prayTimes.getTime(Times.AsrShafi),prayTimes.getTime(Times.Maghrib),prayTimes.getTime(Times.Ishaa),prayTimes.getTime(Times.Midnight),prayTimes.getTime(Times.LastThird)));
        } else {
            prayerTimesLocalTimeString = new ArrayList<String>(Arrays.asList(prayTimes.getTime(Times.Fajr),prayTimes.getTime(Times.Sunrise),prayTimes.getTime(Times.Dhuhr),prayTimes.getTime(Times.AsrHanafi),prayTimes.getTime(Times.Maghrib),prayTimes.getTime(Times.Ishaa),prayTimes.getTime(Times.Midnight),prayTimes.getTime(Times.LastThird)));
        }

        prayerTimesLocalTime.setValue(stringToLocalTime(prayerTimesLocalTimeString));

        fajrSound.setValue(sharedPreferences.getBoolean("Fajr_Sound",false));
        sunriseSound.setValue(sharedPreferences.getBoolean("Sunrise_Sound",false));
        dhuhrSound.setValue(sharedPreferences.getBoolean("Dhuhr_Sound",false));
        asrSound.setValue(sharedPreferences.getBoolean("Asr_Sound",false));
        maghribSound.setValue(sharedPreferences.getBoolean("Maghrib_Sound",false));
        ishaaSound.setValue(sharedPreferences.getBoolean("Ishaa_Sound",false));


    }

    private HighLatsAdjustment getCurrentHighlatsadjustment() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
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

    public Method getCurrentMethod() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
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


    public LiveData<String> getFajrTime() {
        return fajrTime;
    }

    public LiveData<String> getSunriseTime() {
        return sunriseTime;
    }

    public LiveData<String> getDhuhrTime() {
        return dhuhrTime;
    }

    public LiveData<String> getAsrTime() {
        return asrTime;
    }

    public LiveData<String> getMaghribTime() {
        return maghribTime;
    }

    public LiveData<String> getIshaaTime() {
        return ishaaTime;
    }

    public LiveData<Boolean> getFajrSound() { return fajrSound;}
    public LiveData<Boolean> getSunriseSound() { return sunriseSound;}
    public LiveData<Boolean> getDhuhrSound() { return dhuhrSound;}
    public LiveData<Boolean> getAsrSound() { return asrSound;}
    public LiveData<Boolean> getMaghribSound() { return maghribSound;}
    public LiveData<Boolean> getIshaaSound() { return ishaaSound;}



    private ArrayList<LocalTime> stringToLocalTime(ArrayList<String> stringArrayList) {
        ArrayList<LocalTime> localTimeArrayList = new ArrayList<>();
        for (int i = 0;i<stringArrayList.size();i++) {
            DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter();
            LocalTime localTime = LocalTime.parse(stringArrayList.get(i), parseFormat);
            localTimeArrayList.add(localTime);
        }
        return localTimeArrayList;
    }


    public LiveData<ArrayList<LocalTime>> getPrayerTimesList() {
        return prayerTimesLocalTime;
    }

}
