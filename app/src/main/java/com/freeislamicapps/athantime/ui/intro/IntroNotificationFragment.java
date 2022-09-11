package com.freeislamicapps.athantime.ui.intro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeislamicapps.athantime.MainActivity;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.freeislamicapps.athantime.ui.settings.SettingsFragment;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroNotificationFragment extends Fragment {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CHANNEL_1_ID = "prayerChannel";

    TextView fajrSoundText;
    TextView sunriseSoundText;
    TextView dhuhrSoundText;
    TextView asrSoundText;
    TextView maghribSoundText;
    TextView ishaaSoundText;


    boolean fajrSound;
    boolean sunriseSound;
    boolean dhuhrSound;
    boolean asrSound;
    boolean maghribSound;
    boolean ishaaSound;

    Drawable soundOnIcon;
    Drawable soundOffIcon;

    int colorBackground;
    int colorPrimary;
    int colorOnSurface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.intro_fragment_notification, container, false);

        createNotificationChannel(requireContext());

        fajrSoundText = view.findViewById(R.id.fajrSoundText);
        sunriseSoundText = view.findViewById(R.id.sunriseSoundText);
        dhuhrSoundText = view.findViewById(R.id.dhuhrSoundText);
        asrSoundText = view.findViewById(R.id.asrSoundText);
        maghribSoundText = view.findViewById(R.id.maghribSoundText);
        ishaaSoundText = view.findViewById(R.id.ishaaSoundText);

        fajrSound = true;
        sunriseSound = false;
        dhuhrSound = true;
        asrSound = true;
        maghribSound = true;
        ishaaSound = true;

        SharedPreferencesHelper.storeValue(requireContext(),"Fajr_Sound",fajrSound);
        SharedPreferencesHelper.storeValue(requireContext(),"Sunrise_Sound",sunriseSound);
        SharedPreferencesHelper.storeValue(requireContext(),"Dhuhr_Sound",dhuhrSound);
        SharedPreferencesHelper.storeValue(requireContext(),"Asr_Sound",asrSound);
        SharedPreferencesHelper.storeValue(requireContext(),"Maghrib_Sound",maghribSound);
        SharedPreferencesHelper.storeValue(requireContext(),"Ishaa_Sound",ishaaSound);

        ImageView fajrSoundIcon = view.findViewById(R.id.fajrSoundIcon);
        ImageView sunriseSoundIcon = view.findViewById(R.id.sunriseSoundIcon);
        ImageView dhuhrSoundIcon = view.findViewById(R.id.dhuhrSoundIcon);
        ImageView asrSoundIcon = view.findViewById(R.id.asrSoundIcon);
        ImageView maghribSoundIcon = view.findViewById(R.id.maghribSoundIcon);
        ImageView ishaaSoundIcon = view.findViewById(R.id.ishaaSoundIcon);

        soundOnIcon = requireContext().getDrawable(R.drawable.symbol_notification);
        soundOffIcon = requireContext().getDrawable(R.drawable.symbol_volumeoff);

        TypedValue typedValueBackground = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValueBackground, true);
        colorBackground = typedValueBackground.data;

        TypedValue typedValuePrimary = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValuePrimary, true);
        colorPrimary = typedValuePrimary.data;

        TypedValue typedValueColorOnSurface = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValueColorOnSurface, true);
        colorOnSurface = typedValueColorOnSurface.data;


        MaterialCardView fajrSoundCard = view.findViewById(R.id.fajrSoundCard);
        fajrSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClick("Fajr_Sound", fajrSound, fajrSoundIcon, fajrSoundCard, fajrSoundText);
                fajrSound = !fajrSound;
            }
        });

        MaterialCardView sunriseSoundCard = view.findViewById(R.id.sunriseSoundCard);
        sunriseSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClick("Sunrise_Sound", sunriseSound, sunriseSoundIcon, sunriseSoundCard, sunriseSoundText);
                sunriseSound = !sunriseSound;
            }
        });

        MaterialCardView dhuhrSoundCard = view.findViewById(R.id.dhuhrSoundCard);
        dhuhrSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClick("Dhuhr_Sound", dhuhrSound, dhuhrSoundIcon, dhuhrSoundCard, dhuhrSoundText);
                dhuhrSound = !dhuhrSound;
            }
        });

        MaterialCardView asrSoundCard = view.findViewById(R.id.asrSoundCard);
        asrSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClick("Asr_Sound", asrSound, asrSoundIcon, asrSoundCard, asrSoundText);
                asrSound = !asrSound;
            }
        });

        MaterialCardView maghribSoundCard = view.findViewById(R.id.maghribSoundCard);
        maghribSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClick("Maghrib_Sound", maghribSound, maghribSoundIcon, maghribSoundCard, maghribSoundText);
                maghribSound = !maghribSound;
            }
        });

        MaterialCardView ishaaSoundCard = view.findViewById(R.id.ishaaSoundCard);
        ishaaSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClick("Ishaa_Sound", ishaaSound, ishaaSoundIcon, ishaaSoundCard, ishaaSoundText);
                ishaaSound = !ishaaSound;
            }
        });

        Button calculateButton = view.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper.storeValue(requireContext(),"firstStart",false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_1_ID;
            String description = CHANNEL_1_ID;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notificationClick(String savedKey, Boolean prayerSound, ImageView icon, MaterialCardView cardView, TextView textView) {
        createNotificationChannel(requireContext());
        SharedPreferencesHelper.storeValue(requireContext(),savedKey,!prayerSound);
        if (prayerSound) {
            icon.setImageDrawable(soundOffIcon);
            cardView.setCardBackgroundColor(colorBackground);
            textView.setTextColor(colorOnSurface);
        } else {
            icon.setImageDrawable(soundOnIcon);
            cardView.setCardBackgroundColor(colorPrimary);
            textView.setTextColor(getResources().getColor(R.color.white));
        }
    }
}