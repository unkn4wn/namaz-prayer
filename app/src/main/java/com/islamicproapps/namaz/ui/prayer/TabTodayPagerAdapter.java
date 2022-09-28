package com.islamicproapps.namaz.ui.prayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabTodayPagerAdapter extends FragmentStateAdapter {


    public TabTodayPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public int getItemCount() {
        // I have 2000 items to "pretend" to have unlimited items because nobody will swipe 1000 times to the left or right side
        return 2000;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        LocalDate pagerdate = LocalDate.now();
        LocalDate days = pagerdate.plusDays(position - 1000);
        //I am passing the date to my Fragment because the time is set according to the date
        return TabTodayFragment.newInstance(days.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}