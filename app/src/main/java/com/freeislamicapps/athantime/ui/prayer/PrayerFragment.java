package com.freeislamicapps.athantime.ui.prayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.freeislamicapps.athantime.MainActivity;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.databinding.FragmentPrayerBinding;
import com.freeislamicapps.athantime.databinding.FragmentTodayBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.apache.commons.lang3.time.CalendarUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrayerFragment#} factory method to
 * create an instance of this fragment.
 */
public class PrayerFragment extends Fragment {
    public static LocalDate selectedDate;

    View myFragment;

    ViewPager2 viewPager;
    TabLayout tabLayout;
    TextView monthDayText;
    FragmentPrayerBinding binding;
    LocalDate today;
    LocalDate currentDate;
    ImageButton previousDay,nextDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrayerBinding.inflate(inflater, container, false);

        monthDayText = binding.monthDayText;

        previousDay = binding.previousDay;
        nextDay = binding.nextDay;

        LocalDate today = LocalDate.now();
        selectedDate = today;
        String month = today.getMonth().getDisplayName(TextStyle.FULL,Locale.getDefault());
        String dayOfMonth = String.valueOf(today.getDayOfMonth());
        String monthAndDay = month + " " + dayOfMonth;

        monthDayText.setText(monthAndDay);
        Log.d("stopper","First");

        MainActivity.updateSettings();

        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrayerFragment.selectedDate = PrayerFragment.selectedDate.minusDays(1);
                setDayView();
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrayerFragment.selectedDate = PrayerFragment.selectedDate.plusDays(1);
                setDayView();
            }
        });



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(requireActivity());
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.setCurrentItem(1000, false);
        viewPager.post(new Runnable() {
            public void run() {
                viewPager.setCurrentItem(1000, false);
                sectionsPagerAdapter.notifyDataSetChanged();
            }
        });

        viewPager.setOffscreenPageLimit(2);




        return binding.getRoot();
    }


    private void setDayView() {
        String month = selectedDate.getMonth().getDisplayName(TextStyle.FULL,Locale.getDefault());
        String dayOfMonth = String.valueOf(selectedDate.getDayOfMonth());
        String monthAndDay = month + " " + dayOfMonth;

        monthDayText.setText(monthAndDay);

        /*
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
        */
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public TextView getMonthDayText() {
       return monthDayText;
    }

}
