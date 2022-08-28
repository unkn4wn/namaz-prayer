package com.freeislamicapps.athantime.ui.prayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freeislamicapps.athantime.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrayerFragment#} factory method to
 * create an instance of this fragment.
 */
public class PrayerFragment extends Fragment {

    View myFragment;

    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_prayer, container, false);


        viewPager = myFragment.findViewById(R.id.view_Pager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);




        Adapter adapter = new Adapter(this.requireActivity());
        String yesterdayAsCalendar = LocalDate.now().minusDays(1).getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.getDefault());
        String todayAsCalendar = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.getDefault());
        String tomorrowAsCalendar = LocalDate.now().plusDays(1).getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.getDefault());

        adapter.addFragment(new TabTodayFragment(), yesterdayAsCalendar);
        adapter.addFragment(new TabTodayFragment(), todayAsCalendar);
        adapter.addFragment(new TabTomorrowFragment(), tomorrowAsCalendar);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);


        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getTitles().get(position));
            }
        }).attach();


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                new TabTodayFragment();
                PrayerFragment.super.onPause();
            }
        });


        return myFragment;
    }


}
