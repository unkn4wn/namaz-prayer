package com.freeislamicapps.athantime.PrayerTimes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeSet;

public class PrayTimesMain {
    SharedPreferences sharedPreferences;
    private PrayTimes prayTimes;
    private Context context;
    private  String fajr, sunrise, dhuhr, asr, maghrib, ishaa, midnight, lastThird;
    Times currentAsr;
    ArrayList<String> prayerTimesList;

    public PrayTimesMain(LocalDate date, Context context) {
        prayTimes = new PrayTimes();
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        String latitudestr = sharedPreferences.getString("latitude", "0.0");
        String longitudestr = sharedPreferences.getString("longitude", "0.0");

        prayTimes.setCoordinates(Double.parseDouble(latitudestr), Double.parseDouble(longitudestr), 0.0);

        prayTimes.setMethod(getCurrentMethod());
        prayTimes.setHighLatsAdjustment(getCurrentHighlatsadjustment());

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        prayTimes.setDate(year, month, day);

        //TODO
        // Log.d("prayadj",String.valueOf(prayTimes.getTimeZone()));

        fajr = prayTimes.getTime(Times.Fajr);
        sunrise = prayTimes.getTime(Times.Sunrise);
        dhuhr = prayTimes.getTime(Times.Dhuhr);
        if (sharedPreferences.getString("AsrCalculation", "Shafi").equals("Shafi, Hanbali, Maliki")) {
            asr = prayTimes.getTime(Times.AsrShafi);
            currentAsr = Times.AsrShafi;
        } else {
            asr = prayTimes.getTime(Times.AsrHanafi);
            currentAsr = Times.AsrHanafi;
        }
        maghrib = prayTimes.getTime(Times.Maghrib);
        ishaa = prayTimes.getTime(Times.Ishaa);

        prayerTimesList = new ArrayList<>();
        prayerTimesList.add(fajr);
        prayerTimesList.add(sunrise);
        prayerTimesList.add(dhuhr);
        prayerTimesList.add(asr);
        prayerTimesList.add(maghrib);
        prayerTimesList.add(ishaa);

    }

    public PrayTimes getPrayTimes() {
        return prayTimes;
    }

    private HighLatsAdjustment getCurrentHighlatsadjustment() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
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

    public String getFajr() {
        return fajr;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public String getIshaa() {
        return ishaa;
    }


    public Calendar getCalendarFromPrayerTime(Calendar cal, String prayerTime) {
        String[] time = prayerTime.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));


        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public LocalTime getLocalTimeFromPrayerTime(String prayerTime) {
        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter();
        return LocalTime.parse(prayerTime, parseFormat);
    }

    public String getNextPrayer(LocalTime localTime) {
        // create tree set object
        TreeSet<LocalTime> treeadd = new TreeSet<LocalTime>();

        treeadd.add(getLocalTimeFromPrayerTime(getFajr()));
        treeadd.add(getLocalTimeFromPrayerTime(getSunrise()));
        treeadd.add(getLocalTimeFromPrayerTime(getDhuhr()));
        treeadd.add(getLocalTimeFromPrayerTime(getAsr()));
        treeadd.add(getLocalTimeFromPrayerTime(getMaghrib()));
        treeadd.add(getLocalTimeFromPrayerTime(getIshaa()));

        String hours = String.valueOf(localTime.getHour());
        String minutes = String.valueOf(localTime.getMinute());

        if (hours.length() == 1) {
            hours = 0 + hours;
        }

        if (minutes.length() == 1) {
            minutes = 0 + minutes;
        }

        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter();
        LocalTime newlocalTime = LocalTime.parse(hours + ":" + minutes, parseFormat);


        LocalTime value;
        // getting the floor value for 25
        // using floor() method
        value = treeadd.ceiling(newlocalTime);

        PrayTimes prayTimesTomorrow = prayTimes;
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        int year = tomorrow.getYear();
        int month = tomorrow.getMonthValue();
        int day = tomorrow.getDayOfMonth();
        prayTimesTomorrow.setDate(year,month,day);

        if (value == null) {
            value = getLocalTimeFromPrayerTime(prayTimesTomorrow.getTime(Times.Fajr));
        }

        Log.d("nextpraer",value.toString());
      return value.toString();
    }

    public ArrayList<String> getPrayerTimesList() {


        return prayerTimesList;
    }
}
