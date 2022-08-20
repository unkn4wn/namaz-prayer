package com.freeislamicapps.athantime.ui.prayer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.databinding.FragmentTodayBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;

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

    TextView fajrTime, sunriseTime, dhuhrTime, asrTime, maghribTime, ishaaTime, midnightTime,lastThirdTime;
    MaterialCardView fajrCard,sunriseCard,dhuhrCard,asrCard,maghribCard,ishaaCard,midnightCard,lastThirdCard;
    TabTodayViewModel tabTodayViewModel;
    MaterialCardView currentPrayerCard;
    ArrayList<MaterialCardView> materialCardViewArrayList;
    ScrollView scrollView;



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
        midnightTime = binding.midnightTime;
        lastThirdTime = binding.lastThirdTime;

        fajrCard = binding.fajrCard;
        sunriseCard = binding.sunriseCard;
        dhuhrCard = binding.dhuhrCard;
        asrCard = binding.asrCard;
        maghribCard = binding.maghribCard;
        ishaaCard = binding.ishaaCard;
        midnightCard = binding.midnightCard;
        lastThirdCard = binding.lastThirdCard;

        materialCardViewArrayList = new ArrayList<>(Arrays.asList(fajrCard,sunriseCard,dhuhrCard,asrCard,maghribCard,ishaaCard,midnightCard,lastThirdCard));
        scrollView = binding.scrollViewToday;



        TypedValue typedValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true);
        int color = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorSurface, Color.BLACK);




         minutes = String.valueOf(2);


        // method call to initialize the views
        progressBarCircle = (ProgressBar) binding.progressBarCircle;
        textViewTime = (TextView) binding.textViewTime;
        // method call to initialize the listeners
      startStop();



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
        }
        else {
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

      for (int i =0;i<8;i++) {
          treeadd.add(Objects.requireNonNull(tabTodayViewModel.getPrayerTimesList().getValue()).get(i));
      }

        String hours = (String.valueOf(LocalTime.now().getHour()));
        String minutes = (String.valueOf(LocalTime.now().getMinute()));

        if(hours.length()==1) {
            hours = 0+hours;
        }

        if(minutes.length()==1) {
            minutes = 0+minutes;
        }

        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm").toFormatter();
        LocalTime localTime = LocalTime.parse(hours+":"+minutes, parseFormat);


        LocalTime value;
            // getting the floor value for 25
            // using floor() method
             value = treeadd.floor(localTime);

             if(value==null) {
                 value = treeadd.last();
             }

        for (int i = 0; i < 8; i++) {
            materialCardViewArrayList.get(i).setCardBackgroundColor(getResources().getColor(R.color.testcolor));
        }

        for (int i =0;i<8;i++) {
        if(value.compareTo(tabTodayViewModel.getPrayerTimesList().getValue().get(i))==0) {
            currentPrayerCard  = materialCardViewArrayList.get(i);
            currentPrayerCard.setCardBackgroundColor(getResources().getColor(R.color.bottom_navigation));
            currentPrayerCard.setRadius(1f);
            currentPrayerCard.setStrokeWidth(2);
            scrollToViewTop(scrollView,currentPrayerCard);
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
        tabTodayViewModel.getMidnightTime().observe(getViewLifecycleOwner(),midnightTime::setText);
        tabTodayViewModel.getLastThirdTime().observe(getViewLifecycleOwner(),lastThirdTime::setText);

        getCurrentPrayer();
    }
}