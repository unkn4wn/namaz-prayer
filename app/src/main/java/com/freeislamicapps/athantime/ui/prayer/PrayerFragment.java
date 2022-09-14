package com.freeislamicapps.athantime.ui.prayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.freeislamicapps.athantime.databinding.FragmentPrayerBinding;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

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
    TextView cityText;

    TabTodayPagerAdapter tabTodayPagerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrayerBinding.inflate(inflater, container, false);

        monthDayText = binding.monthDayText;

        previousDay = binding.previousDay;
        nextDay = binding.nextDay;

        cityText = binding.cityText;

        System.out.println("JIDJASIJDISA");


        LocalDate today = LocalDate.now();
        selectedDate = today;
        String month = today.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String dayOfMonth = String.valueOf(today.getDayOfMonth());
        String monthAndDay = month + " " + dayOfMonth;

        monthDayText.setText(monthAndDay);


        tabTodayPagerAdapter = new TabTodayPagerAdapter(getChildFragmentManager(), getLifecycle());


        viewPager = binding.viewPager;
        viewPager.setAdapter(tabTodayPagerAdapter);

        viewPager.setCurrentItem(1000, false);
        viewPager.post(() -> {
            viewPager.setCurrentItem(1000, false);
            tabTodayPagerAdapter.notifyDataSetChanged();
        });
        tabTodayPagerAdapter.createFragment(1000);


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

    @Override
    public void onStop() {
        super.onStop();

    }
}
