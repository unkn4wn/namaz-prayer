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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrayerFragment#newInstance} factory method to
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
        adapter.addFragment(new TabTodayFragment(), "Today");
        adapter.addFragment(new TabTomorrowFragment(), "Tomorrow");
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
