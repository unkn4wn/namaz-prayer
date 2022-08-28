package com.freeislamicapps.athantime.ui.prayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.databinding.FragmentTodayBinding;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import jakarta.validation.constraints.NotNull;

public class TabTodayFragment extends Fragment {
    private long timeCountInMilliSeconds = 1 * 60000;
    String minutes;


    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private CountDownTimer countDownTimer;

    private FragmentTodayBinding binding;

    TextView fajrTime, sunriseTime, dhuhrTime, asrTime, maghribTime, ishaaTime;
    MaterialCardView fajrCard, sunriseCard, dhuhrCard, asrCard, maghribCard, ishaaCard;
    TabTodayViewModel tabTodayViewModel;
    MaterialCardView currentPrayerCard;
    ImageView titleBackgroundImage;
    ArrayList<MaterialCardView> materialCardViewArrayList;
    ArrayList<Drawable> backgroundCardViewArrayList;
    ScrollView scrollView;
    SwitchMaterial fajrSound, sunriseSound, dhuhrSound, asrSound, maghribSound, ishaaSound;
    SharedPreferences sharedPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tabTodayViewModel =
                new ViewModelProvider(this).get(TabTodayViewModel.class);

        binding = FragmentTodayBinding.inflate(inflater, container, false);


        binding = FragmentTodayBinding.inflate(inflater, container, false);

        fajrTime = binding.fajrtime;
        sunriseTime = binding.sunrisetime;
        dhuhrTime = binding.dhuhrtime;
        asrTime = binding.asrtime;
        maghribTime = binding.maghribtime;
        ishaaTime = binding.ishaatime;

        fajrCard = binding.fajrCard;
        sunriseCard = binding.sunriseCard;
        dhuhrCard = binding.dhuhrCard;
        asrCard = binding.asrCard;
        maghribCard = binding.maghribCard;
        ishaaCard = binding.ishaaCard;

        fajrSound = binding.fajrsound;
        sunriseSound = binding.sunrisesound;
        dhuhrSound = binding.dhuhrsound;
        asrSound = binding.asrsound;
        maghribSound = binding.maghribsound;
        ishaaSound = binding.ishaasound;

        materialCardViewArrayList = new ArrayList<>(Arrays.asList(fajrCard, sunriseCard, dhuhrCard, asrCard, maghribCard, ishaaCard));
        Drawable fajrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_fajrmaybe);
        Drawable dhuhrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_dhuhrmaybe);
        Drawable asrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_asrmaybe);
        Drawable maghribDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_maghribmaybe);

        backgroundCardViewArrayList = new ArrayList<>(Arrays.asList(fajrDrawable, fajrDrawable, dhuhrDrawable, asrDrawable, maghribDrawable, maghribDrawable));
        scrollView = binding.scrollViewToday;

        titleBackgroundImage = binding.titleBackgroundImage;


        sharedPreferences = requireActivity().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);

        fajrSound.setOnCheckedChangeListener((compoundButton, b) -> {
            saveData("Fajr_Sound", b);
        });
        sunriseSound.setOnCheckedChangeListener((compoundButton, b) -> {
            saveData("Sunrise_Sound", b);
        });
        dhuhrSound.setOnCheckedChangeListener((compoundButton, b) -> {
            saveData("Dhuhr_Sound", b);
        });
        asrSound.setOnCheckedChangeListener((compoundButton, b) -> {
            saveData("Asr_Sound", b);
        });
        maghribSound.setOnCheckedChangeListener((compoundButton, b) -> {
            saveData("Maghrib_Sound", b);
        });
        ishaaSound.setOnCheckedChangeListener((compoundButton, b) -> {
            saveData("Ishaa_Sound", b);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            onStart();
        } else {
            onStop();
        }
    }

    public void scrollToViewTop(ScrollView scrollView, View childView) {
        long delay = 100; //delay to let finish with possible modifications to ScrollView
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(0, childView.getTop());
            }
        }, delay);
    }

    public void scrollToViewBottom(ScrollView scrollView, View childView) {
        long delay = 100; //delay to let finish with possible modifications to ScrollView
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(0, childView.getBottom());
            }
        }, delay);
    }


    /**
     * method to reset count down timer
     */
    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }


    /**
     * method to start and stop count down timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {

            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        int time = 0;
        // fetching value from edit text and type cast to integer
        time = Integer.parseInt(minutes);

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // making edit text editable
                // editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    public void getCurrentPrayer() {
        // create tree set object
        TreeSet<LocalTime> treeadd = new TreeSet<LocalTime>();

        for (int i = 0; i < 6; i++) {
            treeadd.add(Objects.requireNonNull(tabTodayViewModel.getPrayerTimesList().getValue()).get(i));
        }

        String hours = (String.valueOf(LocalTime.now().getHour()));
        String minutes = (String.valueOf(LocalTime.now().getMinute()));

        if (hours.length() == 1) {
            hours = 0 + hours;
        }

        if (minutes.length() == 1) {
            minutes = 0 + minutes;
        }

        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter();
        LocalTime localTime = LocalTime.parse(hours + ":" + minutes, parseFormat);


        LocalTime value;
        // getting the floor value for 25
        // using floor() method
        value = treeadd.floor(localTime);
        boolean nextDay = false;

        if (value == null) {
            value = treeadd.last();
            if(Integer.parseInt(hours)>=0) {
                nextDay = true;
            }
        }

        for (int i = 0; i < 6; i++) {
            materialCardViewArrayList.get(i).setCardBackgroundColor(getResources().getColor(R.color.testcolor));
        }

        for (int i = 0; i < 6; i++) {
            if (value.compareTo(tabTodayViewModel.getPrayerTimesList().getValue().get(i)) == 0 && !nextDay) {
                // Set title background
                titleBackgroundImage.setImageDrawable(backgroundCardViewArrayList.get(i));
                currentPrayerCard = materialCardViewArrayList.get(i);
                currentPrayerCard.setCardBackgroundColor(getResources().getColor(R.color.bottom_navigation));
                currentPrayerCard.setRadius(1f);
                currentPrayerCard.setStrokeWidth(2);
                scrollToViewTop(scrollView, currentPrayerCard);
            }
        }


    }

    @NotNull
    private static Comparator<LocalDateTime> getClosestDateComparator(LocalDateTime now) {
        return (o1, o2) -> {
            long modul1 = Math.abs(Duration.between(now, o1).toDays());
            long modul2 = Math.abs(Duration.between(now, o2).toDays());
            return Long.compare(modul1, modul2);
        };
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        tabTodayViewModel.init();
        tabTodayViewModel.getFajrTime().observe(getViewLifecycleOwner(), fajrTime::setText);
        tabTodayViewModel.getSunriseTime().observe(getViewLifecycleOwner(), sunriseTime::setText);
        tabTodayViewModel.getDhuhrTime().observe(getViewLifecycleOwner(), dhuhrTime::setText);
        tabTodayViewModel.getAsrTime().observe(getViewLifecycleOwner(), asrTime::setText);
        tabTodayViewModel.getMaghribTime().observe(getViewLifecycleOwner(), maghribTime::setText);
        tabTodayViewModel.getIshaaTime().observe(getViewLifecycleOwner(), ishaaTime::setText);

        tabTodayViewModel.getFajrSound().observe(getViewLifecycleOwner(), fajrSound::setChecked);
        tabTodayViewModel.getSunriseSound().observe(getViewLifecycleOwner(), sunriseSound::setChecked);
        tabTodayViewModel.getDhuhrSound().observe(getViewLifecycleOwner(), dhuhrSound::setChecked);
        tabTodayViewModel.getAsrSound().observe(getViewLifecycleOwner(), asrSound::setChecked);
        tabTodayViewModel.getMaghribSound().observe(getViewLifecycleOwner(), maghribSound::setChecked);
        tabTodayViewModel.getIshaaSound().observe(getViewLifecycleOwner(), ishaaSound::setChecked);

        getCurrentPrayer();
    }

    public void saveData(String savedKey, Boolean savedValue) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(savedKey, savedValue);
        editor.apply();
    }
}