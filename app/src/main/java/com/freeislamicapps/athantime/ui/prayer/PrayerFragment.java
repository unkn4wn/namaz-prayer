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
import java.util.Objects;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrayerFragment#} factory method to
 * create an instance of this fragment.
 */
public class PrayerFragment extends Fragment {
    public static LocalDate selectedDate;


    ViewPager2 viewPager;
    TextView monthDayText;
    FragmentPrayerBinding binding;
    ImageButton previousDay, nextDay;

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
        String month = today.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String dayOfMonth = String.valueOf(today.getDayOfMonth());
        String monthAndDay = month + " " + dayOfMonth;

        monthDayText.setText(monthAndDay);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),getLifecycle());

         viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.setCurrentItem(1000, false);
        viewPager.post(new Runnable() {
            public void run() {
                viewPager.setCurrentItem(1000, false);
            }
        });




        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // If I swipe right Date changes to August 30 and vice versa it changes to August 28
                LocalDate pagerdate = LocalDate.now();
                LocalDate days = pagerdate.plusDays(position - 1000);
                String month2 = days.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
                String dayOfMonth2 = String.valueOf(days.getDayOfMonth());
                String monthAndDay2 = month2 + " " + dayOfMonth2;
                monthDayText.setText(monthAndDay2);
            }
        });

        viewPager.setOffscreenPageLimit(1);


        return binding.getRoot();
    }

}
