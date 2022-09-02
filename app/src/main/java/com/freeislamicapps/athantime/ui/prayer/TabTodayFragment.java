package com.freeislamicapps.athantime.ui.prayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.databinding.FragmentTodayBinding;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

public class TabTodayFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    static String index;

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

    public static TabTodayFragment newInstance(String date) {
        TabTodayFragment fragment = new TabTodayFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NUMBER, date);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tabTodayViewModel =
                new ViewModelProvider(this).get(TabTodayViewModel.class);

        index = "default";
        if (getArguments() != null) {
            index = getArguments().getString(ARG_SECTION_NUMBER);
        }

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
        Drawable fajrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_fajr);
        Drawable dhuhrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_dhuhr);
        Drawable asrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_asr);
        Drawable maghribDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_maghrib);

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


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void scrollToViewTop(ScrollView scrollView, View childView) {
        long delay = 100; //delay to let finish with possible modifications to ScrollView
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(0, childView.getTop());
            }
        }, delay);
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
            if (Integer.parseInt(hours) >= 0) {
                nextDay = true;
            }
        }

        TypedValue typedValueBackground = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValueBackground, true);
        int colorBackground = typedValueBackground.data;

        TypedValue typedValuePrimary = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValuePrimary, true);
        int colorPrimary = typedValuePrimary.data;

        TypedValue typedValueSurface = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValueSurface, true);
        int colorSurface = typedValueSurface.data;


        for (int i = 0; i < 6; i++) {
            materialCardViewArrayList.get(i).setCardBackgroundColor(colorBackground);
        }

        for (int i = 0; i < 6; i++) {
            if (value.compareTo(tabTodayViewModel.getPrayerTimesList().getValue().get(i)) == 0 && !nextDay) {

                // Set background image according to current prayer
                titleBackgroundImage.setImageDrawable(backgroundCardViewArrayList.get(i));

                if(LocalDate.parse(index).compareTo(LocalDate.now())==0) {
                    // highlight current prayer time
                    currentPrayerCard = materialCardViewArrayList.get(i);
                    currentPrayerCard.setCardBackgroundColor(colorSurface);
                    currentPrayerCard.setRadius(1f);
                    currentPrayerCard.setStrokeWidth(2);

                    // scroll to current prayer time
                    scrollToViewTop(scrollView, currentPrayerCard);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void saveData(String savedKey, Boolean savedValue) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SettingsFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(savedKey, savedValue);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        fajrSound.setChecked(sharedPreferences.getBoolean("Fajr_Sound",false));
       sunriseSound.setChecked(sharedPreferences.getBoolean("Sunrise_Sound",false));
        dhuhrSound.setChecked(sharedPreferences.getBoolean("Dhuhr_Sound",false));
        asrSound.setChecked(sharedPreferences.getBoolean("Asr_Sound",false));
        maghribSound.setChecked(sharedPreferences.getBoolean("Maghrib_Sound",false));
        ishaaSound.setChecked(sharedPreferences.getBoolean("Ishaa_Sound",false));
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
}