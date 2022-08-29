package com.freeislamicapps.athantime.ui.prayer;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {


    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        LocalDate pagerdate = LocalDate.now();
        LocalDate days = pagerdate.plusDays(position - 1000);
        return TabTodayFragment.newInstance(days.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}