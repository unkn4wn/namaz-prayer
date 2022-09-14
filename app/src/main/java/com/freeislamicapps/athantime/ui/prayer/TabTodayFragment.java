package com.freeislamicapps.athantime.ui.prayer;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.databinding.FragmentTodayBinding;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
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
    TextView fajrText, sunriseText, dhuhrText, asrText, maghribText, ishaaText;
    MaterialCardView fajrCard, sunriseCard, dhuhrCard, asrCard, maghribCard, ishaaCard;
    MaterialDivider fajrDivider, sunriseDivider, dhuhrDivider, asrDivider, maghribDivider, ishaaDivider;
    TabTodayViewModel tabTodayViewModel;
    MaterialCardView currentPrayerCard;
    ImageView titleBackgroundImage;
    ArrayList<MaterialCardView> materialCardViewArrayList;
    ArrayList<MaterialDivider> materialDividerArrayList;
    ArrayList<TextView> textViewArrayList;
    ArrayList<ImageView> imageViewArrayList;
    ArrayList<Drawable> backgroundCardViewArrayList;
    ArrayList<Boolean> booleanArrayList;
    ScrollView scrollView;
    Boolean fajrSound, sunriseSound, dhuhrSound, asrSound, maghribSound, ishaaSound;
    ImageView fajrSoundIcon, sunriseSoundIcon, dhuhrSoundIcon, asrSoundIcon, maghribSoundIcon, ishaaSoundIcon;
    Drawable soundOnIcon, soundOffIcon;


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

        fajrTime = binding.fajrtime;
        sunriseTime = binding.sunrisetime;
        dhuhrTime = binding.dhuhrtime;
        asrTime = binding.asrtime;
        maghribTime = binding.maghribtime;
        ishaaTime = binding.ishaatime;

        fajrText = binding.fajrtext;
        sunriseText = binding.sunrisetext;
        dhuhrText = binding.dhuhrtext;
        asrText = binding.asrtext;
        maghribText = binding.maghribtext;
        ishaaText = binding.ishaatext;

        fajrDivider = binding.fajrDivider;
        sunriseDivider = binding.sunriseDivider;
        dhuhrDivider = binding.dhuhrDivider;
        asrDivider = binding.asrDivider;
        maghribDivider = binding.maghribDivider;

        fajrCard = binding.fajrCard;
        sunriseCard = binding.sunriseCard;
        dhuhrCard = binding.dhuhrCard;
        asrCard = binding.asrCard;
        maghribCard = binding.maghribCard;
        ishaaCard = binding.ishaaCard;

        fajrSoundIcon = binding.fajrSoundIcon;
        sunriseSoundIcon = binding.sunriseSoundIcon;
        dhuhrSoundIcon = binding.dhuhrSoundIcon;
        asrSoundIcon = binding.asrSoundIcon;
        maghribSoundIcon = binding.maghribSoundIcon;
        ishaaSoundIcon = binding.ishaaSoundIcon;


        materialCardViewArrayList = new ArrayList<>(Arrays.asList(fajrCard, sunriseCard, dhuhrCard, asrCard, maghribCard, ishaaCard));
        materialDividerArrayList = new ArrayList<>(Arrays.asList(fajrDivider, sunriseDivider, dhuhrDivider, asrDivider, maghribDivider));
        textViewArrayList = new ArrayList<>(Arrays.asList(fajrText, sunriseText, dhuhrText, asrText, maghribText, ishaaText, fajrTime, sunriseTime, dhuhrTime, asrTime, maghribTime, ishaaTime));
        imageViewArrayList = new ArrayList<>(Arrays.asList(fajrSoundIcon, sunriseSoundIcon, dhuhrSoundIcon, asrSoundIcon, maghribSoundIcon, ishaaSoundIcon));
        Drawable fajrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_fajr);
        Drawable dhuhrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_dhuhr);
        Drawable asrDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_asr);
        Drawable maghribDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.background_maghrib);

        backgroundCardViewArrayList = new ArrayList<>(Arrays.asList(fajrDrawable, fajrDrawable, dhuhrDrawable, asrDrawable, maghribDrawable, maghribDrawable));
        scrollView = binding.scrollViewToday;

        titleBackgroundImage = binding.titleBackgroundImage;

        soundOnIcon = requireContext().getDrawable(R.drawable.symbol_notification);
        soundOffIcon = requireContext().getDrawable(R.drawable.symbol_notification_off);


        tabTodayViewModel.init();
        tabTodayViewModel.getFajrTime().observe(getViewLifecycleOwner(), fajrTime::setText);
        tabTodayViewModel.getSunriseTime().observe(getViewLifecycleOwner(), sunriseTime::setText);
        tabTodayViewModel.getDhuhrTime().observe(getViewLifecycleOwner(), dhuhrTime::setText);
        tabTodayViewModel.getAsrTime().observe(getViewLifecycleOwner(), asrTime::setText);
        tabTodayViewModel.getMaghribTime().observe(getViewLifecycleOwner(), maghribTime::setText);
        tabTodayViewModel.getIshaaTime().observe(getViewLifecycleOwner(), ishaaTime::setText);

        fajrSound = SharedPreferencesHelper.getValue(requireContext(), "Fajr_Sound", true);
        sunriseSound = SharedPreferencesHelper.getValue(requireContext(), "Sunrise_Sound", false);
        dhuhrSound = SharedPreferencesHelper.getValue(requireContext(), "Dhuhr_Sound", true);
        asrSound = SharedPreferencesHelper.getValue(requireContext(), "Asr_Sound", true);
        maghribSound = SharedPreferencesHelper.getValue(requireContext(), "Maghrib_Sound", true);
        ishaaSound = SharedPreferencesHelper.getValue(requireContext(), "Ishaa_Sound", true);

        booleanArrayList = new ArrayList<>(Arrays.asList(fajrSound,sunriseSound,dhuhrSound,asrSound,maghribSound,ishaaSound));


        initNotificationIcon(fajrSound, fajrSoundIcon);
        initNotificationIcon(sunriseSound, sunriseSoundIcon);
        initNotificationIcon(dhuhrSound, dhuhrSoundIcon);
        initNotificationIcon(asrSound, asrSoundIcon);
        initNotificationIcon(maghribSound, maghribSoundIcon);
        initNotificationIcon(ishaaSound, ishaaSoundIcon);
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

        TypedValue typedValueOnSurface = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValueOnSurface, true);
        int colorOnSurface = typedValueOnSurface.data;

        TypedValue typedValueSurface = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValueSurface, true);
        int colorSurface = typedValueSurface.data;


        for (int i = 0; i < 6; i++) {
            materialCardViewArrayList.get(i).setCardBackgroundColor(colorBackground);
            textViewArrayList.get(i).setTextColor(colorOnSurface);
        }

        for (int i = 0; i < 6; i++) {
            if (value.compareTo(tabTodayViewModel.getPrayerTimesList().getValue().get(i)) == 0 && !nextDay) {

                // Set background image according to current prayer
                titleBackgroundImage.setImageDrawable(backgroundCardViewArrayList.get(i));

                if (LocalDate.parse(index).compareTo(LocalDate.now()) == 0) {
                    // highlight current prayer time
                    currentPrayerCard = materialCardViewArrayList.get(i);
                    //set color white for current prayer time text
                    textViewArrayList.get(i).setTextColor(getResources().getColor(R.color.white));
                    textViewArrayList.get(i + 6).setTextColor(getResources().getColor(R.color.white));

                    if(booleanArrayList.get(i)) {
                        imageViewArrayList.get(i).setImageDrawable(getResources().getDrawable(R.drawable.symbol_notification_selected));
                    } else {
                        imageViewArrayList.get(i).setImageDrawable(getResources().getDrawable(R.drawable.symbol_notification_off_selected));
                    }
                    currentPrayerCard.setCardBackgroundColor(colorPrimary);
                    currentPrayerCard.setRadius(30f);
                    if (i == 0 || i == 5) {
                        currentPrayerCard.setRadius(0f);
                    }
                    currentPrayerCard.setStrokeWidth(0);

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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initNotificationIcon(Boolean prayerSound, ImageView prayerSoundIcon) {
        if (prayerSound) {
            prayerSoundIcon.setImageDrawable(soundOnIcon);
        } else {
            prayerSoundIcon.setImageDrawable(soundOffIcon);
        }
    }
}