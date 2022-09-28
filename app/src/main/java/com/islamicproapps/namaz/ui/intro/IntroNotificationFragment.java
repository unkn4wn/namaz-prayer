package com.islamicproapps.namaz.ui.intro;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

import com.islamicproapps.namaz.MainActivity;
import com.islamicproapps.namaz.R;
import com.islamicproapps.namaz.alarm.AlarmStart;
import com.islamicproapps.namaz.helper.SharedPreferencesHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.Calendar;

/**
 * Intro Fragment
 * The class IntroNotificationFragment will be shown during first start of the app
 * enable and disable notifications for specific prayers
 */
public class IntroNotificationFragment extends Fragment {
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

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

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
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                    } else {
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

        //Autosize below Api 26
        TextView titleLocation = view.findViewById(R.id.title_notification);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleLocation,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

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

        SharedPreferencesHelper.storeValue(requireContext(), "Fajr_Sound", fajrSound);
        SharedPreferencesHelper.storeValue(requireContext(), "Sunrise_Sound", sunriseSound);
        SharedPreferencesHelper.storeValue(requireContext(), "Dhuhr_Sound", dhuhrSound);
        SharedPreferencesHelper.storeValue(requireContext(), "Asr_Sound", asrSound);
        SharedPreferencesHelper.storeValue(requireContext(), "Maghrib_Sound", maghribSound);
        SharedPreferencesHelper.storeValue(requireContext(), "Ishaa_Sound", ishaaSound);

        ImageView fajrSoundIcon = view.findViewById(R.id.fajrSoundIcon);
        ImageView sunriseSoundIcon = view.findViewById(R.id.sunriseSoundIcon);
        ImageView dhuhrSoundIcon = view.findViewById(R.id.dhuhrSoundIcon);
        ImageView asrSoundIcon = view.findViewById(R.id.asrSoundIcon);
        ImageView maghribSoundIcon = view.findViewById(R.id.maghribSoundIcon);
        ImageView ishaaSoundIcon = view.findViewById(R.id.ishaaSoundIcon);

        soundOnIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.symbol_notification);
        soundOffIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.symbol_volumeoff);

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
        fajrSoundCard.setOnClickListener(view1 -> {
            notificationClick("Fajr_Sound", fajrSound, fajrSoundIcon, fajrSoundCard, fajrSoundText);
            fajrSound = !fajrSound;
        });

        MaterialCardView sunriseSoundCard = view.findViewById(R.id.sunriseSoundCard);
        sunriseSoundCard.setOnClickListener(view12 -> {
            notificationClick("Sunrise_Sound", sunriseSound, sunriseSoundIcon, sunriseSoundCard, sunriseSoundText);
            sunriseSound = !sunriseSound;
        });

        MaterialCardView dhuhrSoundCard = view.findViewById(R.id.dhuhrSoundCard);
        dhuhrSoundCard.setOnClickListener(view13 -> {
            notificationClick("Dhuhr_Sound", dhuhrSound, dhuhrSoundIcon, dhuhrSoundCard, dhuhrSoundText);
            dhuhrSound = !dhuhrSound;
        });

        MaterialCardView asrSoundCard = view.findViewById(R.id.asrSoundCard);
        asrSoundCard.setOnClickListener(view14 -> {
            notificationClick("Asr_Sound", asrSound, asrSoundIcon, asrSoundCard, asrSoundText);
            asrSound = !asrSound;
        });

        MaterialCardView maghribSoundCard = view.findViewById(R.id.maghribSoundCard);
        maghribSoundCard.setOnClickListener(view15 -> {
            notificationClick("Maghrib_Sound", maghribSound, maghribSoundIcon, maghribSoundCard, maghribSoundText);
            maghribSound = !maghribSound;
        });

        MaterialCardView ishaaSoundCard = view.findViewById(R.id.ishaaSoundCard);
        ishaaSoundCard.setOnClickListener(view16 -> {
            notificationClick("Ishaa_Sound", ishaaSound, ishaaSoundIcon, ishaaSoundCard, ishaaSoundText);
            ishaaSound = !ishaaSound;
        });

        Button calculateButton = view.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(view17 -> {
            setAlarm();
            SharedPreferencesHelper.storeValue(requireContext(), "firstStart", false);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });




        return view;
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Prayer Times";
            String description = "This notification channel is necessary to provide accurate prayer times for you";
            int importance = NotificationManager.IMPORTANCE_HIGH;
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
        SharedPreferencesHelper.storeValue(requireContext(), savedKey, !prayerSound);
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
    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(requireActivity(), AlarmStart.class);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(requireActivity(), 20, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(requireActivity(), 20, intent, 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }
}