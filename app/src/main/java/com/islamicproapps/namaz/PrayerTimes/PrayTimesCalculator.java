package com.islamicproapps.namaz.PrayerTimes;

import android.content.Context;

import com.islamicproapps.namaz.helper.SharedPreferencesHelper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

public class PrayTimesCalculator {
    private final PrayTimes prayTimes;
    private final Context context;
    private final String fajr, sunrise, dhuhr, asr, maghrib, ishaa;
    ArrayList<String> prayerTimesList;


    public PrayTimesCalculator(LocalDate date, Context context) {
        prayTimes = new PrayTimes();
        this.context = context;

        double latitude = SharedPreferencesHelper.getValue(context, "latitude", 52.0);
        double longitude = SharedPreferencesHelper.getValue(context, "longitude", 9.0);

        prayTimes.setCoordinates(latitude, longitude, 0.0);
        prayTimes.setMethod(getCurrentMethod());
        prayTimes.setHighLatsAdjustment(getCurrentHighlatsadjustment());

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        prayTimes.setDate(year, month, day);

        int fajrManualTunes = SharedPreferencesHelper.getValue(context,"FajrManualTunes",0);
        int sunriseManualTunes = SharedPreferencesHelper.getValue(context,"SunriseManualTunes",0);
        int dhuhrManualTunes = SharedPreferencesHelper.getValue(context,"DhuhrManualTunes",0);
        int asrManualTunes = SharedPreferencesHelper.getValue(context,"AsrManualTunes",0);
        int maghribManualTunes = SharedPreferencesHelper.getValue(context,"MaghribManualTunes",0);
        int ishaaManualTunes = SharedPreferencesHelper.getValue(context,"IshaaManualTunes",0);

        prayTimes.tuneFajr(prayTimes.getFajrAngle(),prayTimes.getFajrMinuteAdjust()+fajrManualTunes);
        prayTimes.tuneSunrise(prayTimes.getSunriseMinuteAdjust()+sunriseManualTunes);
        prayTimes.tuneDhuhr(prayTimes.getDhuhrMinuteAdjust()+dhuhrManualTunes);
        prayTimes.tuneAsrHanafi(prayTimes.getAsrHanafiMinuteAdjust()+asrManualTunes);
        prayTimes.tuneAsrShafi(prayTimes.getAsrShafiMinuteAdjust()+asrManualTunes);
        prayTimes.tuneMaghrib(prayTimes.getMaghribAngle(),prayTimes.getMaghribMinuteAdjust()+maghribManualTunes);
        prayTimes.tuneIshaa(prayTimes.getIshaaAngle(),prayTimes.getIshaaMinuteAdjust()+ishaaManualTunes);

        //TODO
        // Log.d("prayadj",String.valueOf(prayTimes.getTimeZone()));

        fajr = prayTimes.getTime(Times.Fajr);
        sunrise = prayTimes.getTime(Times.Sunrise);
        dhuhr = prayTimes.getTime(Times.Dhuhr);
        String asrCalculation = SharedPreferencesHelper.getValue(context, "AsrCalculation", "Shafi, Hanbali, Maliki");
        if (asrCalculation.equals("Shafi, Hanbali, Maliki")) {
            asr = prayTimes.getTime(Times.AsrShafi);
        } else {
            asr = prayTimes.getTime(Times.AsrHanafi);
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

    private HighLatsAdjustment getCurrentHighlatsadjustment() {
        String highLatadjustment = SharedPreferencesHelper.getValue(context, "HighLatsAdjustment", "Angle-Based");
        switch (highLatadjustment) {
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
        String method = SharedPreferencesHelper.getValue(context, "Method", "Islamic Society of North America (ISNA)");
        switch (method) {
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
        TreeSet<LocalTime> treeadd = new TreeSet<>();

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
        prayTimesTomorrow.setDate(year, month, day);

        if (value == null) {
            value = getLocalTimeFromPrayerTime(prayTimesTomorrow.getTime(Times.Fajr));
        }

        return value.toString();
    }

    public ArrayList<String> getPrayerTimesList() {
        return prayerTimesList;
    }
}
