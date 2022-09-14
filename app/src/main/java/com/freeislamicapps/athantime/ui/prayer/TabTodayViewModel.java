package com.freeislamicapps.athantime.ui.prayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.freeislamicapps.athantime.PrayerTimes.PrayTimesCalculator;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

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
    Context context;


    public TabTodayViewModel(Application application) {
        super(application);
        context = getApplication().getApplicationContext();
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
        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        //TODO
        // Log.d("prayadj",String.valueOf(MainActivity.prayTimes.getTimeZone()));
        PrayTimesCalculator prayTimesCalculator = new PrayTimesCalculator(LocalDate.parse(TabTodayFragment.index), getApplication().getApplicationContext());


        fajrTime.setValue(prayTimesCalculator.getFajr());
        sunriseTime.setValue(prayTimesCalculator.getSunrise());
        dhuhrTime.setValue(prayTimesCalculator.getDhuhr());
        asrTime.setValue(prayTimesCalculator.getAsr());
        maghribTime.setValue(prayTimesCalculator.getMaghrib());
        ishaaTime.setValue(prayTimesCalculator.getIshaa());

        prayerTimesLocalTimeString = prayTimesCalculator.getPrayerTimesList();

        prayerTimesLocalTime.setValue(stringToLocalTime(prayerTimesLocalTimeString));

        fajrSound.setValue(SharedPreferencesHelper.getValue(context, "Fajr_Sound", false));
        sunriseSound.setValue(SharedPreferencesHelper.getValue(context, "Sunrise_Sound", false));
        dhuhrSound.setValue(SharedPreferencesHelper.getValue(context, "Dhuhr_Sound", false));
        asrSound.setValue(SharedPreferencesHelper.getValue(context, "Asr_Sound", false));
        maghribSound.setValue(SharedPreferencesHelper.getValue(context, "Maghrib_Sound", false));
        ishaaSound.setValue(SharedPreferencesHelper.getValue(context, "Ishaa_Sound", false));


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


    private ArrayList<LocalTime> stringToLocalTime(ArrayList<String> stringArrayList) {
        ArrayList<LocalTime> localTimeArrayList = new ArrayList<>();
        for (int i = 0; i < stringArrayList.size(); i++) {
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
