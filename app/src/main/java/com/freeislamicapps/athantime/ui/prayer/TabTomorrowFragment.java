package com.freeislamicapps.athantime.ui.prayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.databinding.FragmentTomorrowBinding;

public class TabTomorrowFragment extends Fragment {
    private FragmentTomorrowBinding binding;

    TextView fajrTime, sunriseTime, dhuhrTime, asrTime, maghribTime, ishaaTime;
    TabTomorrowViewModel TabTomorrowViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TabTomorrowViewModel =
                new ViewModelProvider(this).get(TabTomorrowViewModel.class);

        binding = FragmentTomorrowBinding.inflate(inflater, container, false);


        binding = FragmentTomorrowBinding.inflate(inflater, container, false);

        fajrTime = binding.fajrtime;
        sunriseTime = binding.sunrisetime;
        dhuhrTime = binding.dhuhrtime;
        asrTime = binding.asrtime;
        maghribTime = binding.maghribtime;
        ishaaTime = binding.ishaatime;

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
            TabTomorrowViewModel.init();
            TabTomorrowViewModel.getFajrTime().observe(getViewLifecycleOwner(), fajrTime::setText);
            TabTomorrowViewModel.getSunriseTime().observe(getViewLifecycleOwner(), sunriseTime::setText);
            TabTomorrowViewModel.getDhuhrTime().observe(getViewLifecycleOwner(), dhuhrTime::setText);
            TabTomorrowViewModel.getAsrTime().observe(getViewLifecycleOwner(), asrTime::setText);
            TabTomorrowViewModel.getMaghribTime().observe(getViewLifecycleOwner(), maghribTime::setText);
            TabTomorrowViewModel.getIshaaTime().observe(getViewLifecycleOwner(), ishaaTime::setText);
        }
    }
}