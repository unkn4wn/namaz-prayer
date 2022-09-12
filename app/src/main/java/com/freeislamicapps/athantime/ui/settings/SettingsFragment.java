package com.freeislamicapps.athantime.ui.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.freeislamicapps.athantime.ui.prayer.LocationFragment;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.card.MaterialCardView;


public class SettingsFragment extends Fragment implements DialogInterface.OnDismissListener {
    TextView currentLocation;
    TextView methodText, asrCalculationText, highLatsAdjustmentText, styleText;
    public static final String SHARED_PREFS = "sharedPrefs";

    Drawable soundOnIcon;
    Drawable soundOffIcon;
    int colorBackground;
    int colorPrimary;
    int colorOnSurface;

    boolean fajrSound;
    boolean sunriseSound;
    boolean dhuhrSound;
    boolean asrSound;
    boolean maghribSound;
    boolean ishaaSound;


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        Log.d("display2", "VISIBLE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        MaterialCardView cardViewLocation = view.findViewById(R.id.cardViewLocation);
        currentLocation = view.findViewById(R.id.currentlocation);
        methodText = view.findViewById(R.id.methodText);
        asrCalculationText = view.findViewById(R.id.asrcalculationText);
        highLatsAdjustmentText = view.findViewById(R.id.highlatadjustmentText);


        MaterialCardView cardViewMethod = view.findViewById(R.id.cardViewMethod);
        cardViewMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogMethod();
            }
        });

        MaterialCardView cardViewAsrcalculations = view.findViewById(R.id.cardViewAsrcalculation);
        cardViewAsrcalculations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAsrcalculations();
            }
        });

        MaterialCardView cardViewHighLatsAdjustment = view.findViewById(R.id.cardViewHighlatsadjustment);
        cardViewHighLatsAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogHighlatsadjustment();
            }
        });

        MaterialCardView cardViewManualtunes = view.findViewById(R.id.cardviewManualtunes);
        cardViewManualtunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogManualtunes();
            }
        });

        MaterialCardView cardViewNotification = view.findViewById(R.id.cardViewNotification);
        cardViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNotification();
            }
        });

        currentLocation.setText(SharedPreferencesHelper.getValue(requireContext(), "location", ""));
        methodText.setText(SharedPreferencesHelper.getValue(requireContext(), "Method", "Islamic Society of North America (ISNA)"));
        asrCalculationText.setText(SharedPreferencesHelper.getValue(requireContext(), "AsrCalculation", "Shafi, Hanbali, Maliki"));
        highLatsAdjustmentText.setText(SharedPreferencesHelper.getValue(requireContext(), "HighLatsAdjustment", "Angle-Based"));


        cardViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, new LocationFragment());
                transaction.commit(); */
                LocationFragment locationFragment = new LocationFragment();
                locationFragment.show(getChildFragmentManager(), "MyFragment");
            }
        });


        return view;
    }


    private void showDialogHighlatsadjustment() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_highlatadjustment);

        LinearLayout cardViewHighLatsNone = dialog.findViewById(R.id.cardViewHighlatsnone);
        LinearLayout cardViewHighLatsAngle = dialog.findViewById(R.id.cardViewHighlatsangle);
        LinearLayout cardViewHighLatsMiddle = dialog.findViewById(R.id.cardViewHighlatsmiddle);
        LinearLayout cardViewHighLatsSeventh = dialog.findViewById(R.id.cardViewHighlatsseventh);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        cardViewHighLatsNone.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("None");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "None");
        });

        cardViewHighLatsAngle.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("Angle-Based");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "Angle-Based");
        });

        cardViewHighLatsMiddle.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("Middle of the night");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "Middle of the night");
        });

        cardViewHighLatsSeventh.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("One-Seventh of the Night");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "One-Seventh of the Night");
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogAsrcalculations() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_asrcalculation);

        LinearLayout cardViewShafi = dialog.findViewById(R.id.cardViewShafi);
        LinearLayout cardViewHanafi = dialog.findViewById(R.id.cardViewHanafi);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);


        cardViewShafi.setOnClickListener(v -> {
            dialog.dismiss();
            asrCalculationText.setText("Shafi, Hanbali, Maliki");
            SharedPreferencesHelper.storeValue(requireContext(), "AsrCalculation", "Shafi, Hanbali, Maliki");
        });

        cardViewHanafi.setOnClickListener(v -> {
            dialog.dismiss();
            asrCalculationText.setText("Hanafi");
            SharedPreferencesHelper.storeValue(requireContext(), "AsrCalculation", "Hanafi");
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogMethod() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_method);

        LinearLayout cardviewEgypt = dialog.findViewById(R.id.cardViewEgypt);
        LinearLayout cardviewTehran = dialog.findViewById(R.id.cardViewTehran);
        LinearLayout cardViewIsna = dialog.findViewById(R.id.cardViewIsna);
        LinearLayout cardViewMwl = dialog.findViewById(R.id.cardViewMwl);
        LinearLayout cardViewMakkah = dialog.findViewById(R.id.cardViewMakkah);
        LinearLayout cardviewUoif = dialog.findViewById(R.id.cardViewUOIF);
        LinearLayout cardViewKarachi = dialog.findViewById(R.id.cardViewKarachi);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);


        cardviewEgypt.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Egyptian General Authority of Survey");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Egyptian General Authority of Survey");
        });

        cardviewTehran.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Institute of Geophysics, University of Tehran");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Institute of Geophysics, University of Tehran");
        });

        cardViewIsna.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Islamic Society of North America (ISNA)");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Islamic Society of North America (ISNA)");
        });

        cardViewMwl.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Muslim World League (MWL)");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Muslim World League (MWL)");
        });

        cardViewMakkah.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Umm Al-Qura University, Makkah");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Umm Al-Qura University, Makkah");
        });

        cardviewUoif.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Union des organisations islamiques de France");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Union des organisations islamiques de France");
        });

        cardViewKarachi.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("University of Islamic Sciences, Karachi");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "University of Islamic Sciences, Karachi");
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showDialogManualtunes() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_manualtunes);

        TextView fajrMinute = dialog.findViewById(R.id.fajrMinute);
        TextView fajrMinuteText = dialog.findViewById(R.id.fajrMinuteText);
        ImageButton fajrAdd = dialog.findViewById(R.id.fajrAdd);
        ImageButton fajrRemove = dialog.findViewById(R.id.fajrRemove);
        TextView sunriseMinute = dialog.findViewById(R.id.sunriseMinute);
        TextView sunriseMinuteText = dialog.findViewById(R.id.sunriseMinuteText);
        ImageButton sunriseAdd = dialog.findViewById(R.id.sunriseAdd);
        ImageButton sunriseRemove = dialog.findViewById(R.id.sunriseRemove);
        TextView dhuhrMinute = dialog.findViewById(R.id.dhuhrMinute);
        TextView dhuhrMinuteText = dialog.findViewById(R.id.dhuhrMinuteText);
        ImageButton dhuhrAdd = dialog.findViewById(R.id.dhuhrAdd);
        ImageButton dhuhrRemove = dialog.findViewById(R.id.dhuhrRemove);
        TextView asrMinute = dialog.findViewById(R.id.asrMinute);
        TextView asrMinuteText = dialog.findViewById(R.id.asrMinuteText);
        ImageButton asrAdd = dialog.findViewById(R.id.asrAdd);
        ImageButton asrRemove = dialog.findViewById(R.id.asrRemove);
        TextView maghribMinute = dialog.findViewById(R.id.maghribMinute);
        TextView maghribMinuteText = dialog.findViewById(R.id.maghribMinuteText);
        ImageButton maghribAdd = dialog.findViewById(R.id.maghribAdd);
        ImageButton maghribRemove = dialog.findViewById(R.id.maghribRemove);
        TextView ishaaMinute = dialog.findViewById(R.id.ishaaMinute);
        TextView ishaaMinuteText = dialog.findViewById(R.id.ishaaMinuteText);
        ImageButton ishaaAdd = dialog.findViewById(R.id.ishaaAdd);
        ImageButton ishaaRemove = dialog.findViewById(R.id.ishaaRemove);


        displayManualTunes("FajrManualTunes",fajrMinuteText,fajrMinute);
        displayManualTunes("SunriseManualTunes",sunriseMinuteText,sunriseMinute);
        displayManualTunes("DhuhrManualTunes",dhuhrMinuteText,dhuhrMinute);
        displayManualTunes("AsrManualTunes",asrMinuteText,asrMinute);
        displayManualTunes("MaghribManualTunes",maghribMinuteText,maghribMinute);
        displayManualTunes("IshaaManualTunes",ishaaMinuteText,ishaaMinute);



        fajrAdd.setOnClickListener(view -> changeManualTunes("FajrManualTunes",fajrMinuteText,fajrMinute, +1));
        fajrRemove.setOnClickListener(view -> changeManualTunes("FajrManualTunes",fajrMinuteText,fajrMinute,-1));
        sunriseAdd.setOnClickListener(view -> changeManualTunes("SunriseManualTunes",sunriseMinuteText,sunriseMinute, +1));
        sunriseRemove.setOnClickListener(view -> changeManualTunes("SunriseManualTunes",sunriseMinuteText,sunriseMinute,-1));
        dhuhrAdd.setOnClickListener(view -> changeManualTunes("DhuhrManualTunes",dhuhrMinuteText,dhuhrMinute, +1));
        dhuhrRemove.setOnClickListener(view -> changeManualTunes("DhuhrManualTunes",dhuhrMinuteText,dhuhrMinute,-1));
        asrAdd.setOnClickListener(view -> changeManualTunes("AsrManualTunes",asrMinuteText,asrMinute, +1));
        asrRemove.setOnClickListener(view -> changeManualTunes("AsrManualTunes",asrMinuteText,asrMinute,-1));
        maghribAdd.setOnClickListener(view -> changeManualTunes("MaghribManualTunes",maghribMinuteText,maghribMinute, +1));
        maghribRemove.setOnClickListener(view -> changeManualTunes("MaghribManualTunes",maghribMinuteText,maghribMinute,-1));
        ishaaAdd.setOnClickListener(view -> changeManualTunes("IshaaManualTunes",ishaaMinuteText,ishaaMinute, +1));
        ishaaRemove.setOnClickListener(view -> changeManualTunes("IshaaManualTunes",ishaaMinuteText,ishaaMinute,-1));

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void displayManualTunes(String savedKey, TextView prayerMinuteText, TextView prayerMinute) {
        int prayerManualTunes = SharedPreferencesHelper.getValue(requireContext(), savedKey, 0);
        prayerMinute.setText(String.valueOf(prayerManualTunes));
        if (prayerManualTunes == 1 || prayerManualTunes == -1) {
            prayerMinuteText.setText(R.string.minute);
        }
    }

    private void changeManualTunes(String savedKey,TextView prayerMinuteText,TextView prayerMinute,int count) {
        int oldPrayerManualTunes = SharedPreferencesHelper.getValue(requireContext(), savedKey, 0);

        SharedPreferencesHelper.storeValue(requireContext(), savedKey, oldPrayerManualTunes+count);
        int newPrayerManualTunes = oldPrayerManualTunes +count;

        prayerMinuteText.setText(R.string.minutes);
        if (newPrayerManualTunes == 1 || newPrayerManualTunes == -1) {
            prayerMinuteText.setText(R.string.minute);
        }
        prayerMinute.setText(String.valueOf(newPrayerManualTunes));
    }

    private void showDialogNotification() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_notification);

        RelativeLayout relativeLayoutFajr = dialog.findViewById(R.id.relativeLayoutFajr);
        RelativeLayout relativeLayoutSunrise = dialog.findViewById(R.id.relativeLayoutSunrise);
        RelativeLayout relativeLayoutDhuhr = dialog.findViewById(R.id.relativeLayoutDhuhr);
        RelativeLayout relativeLayoutAsr = dialog.findViewById(R.id.relativeLayoutAsr);
        RelativeLayout relativeLayoutMaghrib = dialog.findViewById(R.id.relativeLayoutMaghrib);
        RelativeLayout relativeLayoutIshaa = dialog.findViewById(R.id.relativeLayoutIshaa);

        ImageView fajrSoundIcon = dialog.findViewById(R.id.fajrSoundIcon);
        ImageView sunriseSoundIcon = dialog.findViewById(R.id.sunriseSoundIcon);
        ImageView dhuhrSoundIcon = dialog.findViewById(R.id.dhuhrSoundIcon);
        ImageView asrSoundIcon = dialog.findViewById(R.id.asrSoundIcon);
        ImageView maghribSoundIcon = dialog.findViewById(R.id.maghribSoundIcon);
        ImageView ishaaSoundIcon = dialog.findViewById(R.id.ishaaSoundIcon);

        TextView fajrSoundText = dialog.findViewById(R.id.fajrSoundText);
        TextView sunriseSoundText = dialog.findViewById(R.id.sunriseSoundText);
        TextView dhuhrSoundText = dialog.findViewById(R.id.dhuhrSoundText);
        TextView asrSoundText = dialog.findViewById(R.id.asrSoundText);
        TextView maghribSoundText = dialog.findViewById(R.id.maghribSoundText);
        TextView ishaaSoundText = dialog.findViewById(R.id.ishaaSoundText);

        fajrSound = SharedPreferencesHelper.getValue(requireContext(), "Fajr_Sound", true);
        sunriseSound = SharedPreferencesHelper.getValue(requireContext(), "Sunrise_Sound", false);
        ;
        dhuhrSound = SharedPreferencesHelper.getValue(requireContext(), "Dhuhr_Sound", true);
        ;
        asrSound = SharedPreferencesHelper.getValue(requireContext(), "Asr_Sound", true);
        ;
        maghribSound = SharedPreferencesHelper.getValue(requireContext(), "Maghrib_Sound", true);
        ;
        ishaaSound = SharedPreferencesHelper.getValue(requireContext(), "Ishaa_Sound", true);
        ;

        soundOnIcon = requireContext().getDrawable(R.drawable.symbol_notification_selected);
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

        initNotification(fajrSound, fajrSoundIcon, relativeLayoutFajr, fajrSoundText);
        initNotification(sunriseSound, sunriseSoundIcon, relativeLayoutSunrise, sunriseSoundText);
        initNotification(dhuhrSound, dhuhrSoundIcon, relativeLayoutDhuhr, dhuhrSoundText);
        initNotification(asrSound, asrSoundIcon, relativeLayoutAsr, asrSoundText);
        initNotification(maghribSound, maghribSoundIcon, relativeLayoutMaghrib, maghribSoundText);
        initNotification(ishaaSound, ishaaSoundIcon, relativeLayoutIshaa, ishaaSoundText);


        relativeLayoutFajr.setOnClickListener(view -> {
            notificationClick("Fajr_Sound", fajrSound, fajrSoundIcon, relativeLayoutFajr, fajrSoundText);
            fajrSound = !fajrSound;
        });
        relativeLayoutSunrise.setOnClickListener(view -> {
            notificationClick("Sunrise_Sound", sunriseSound, sunriseSoundIcon, relativeLayoutSunrise, sunriseSoundText);
            sunriseSound = !sunriseSound;
        });
        relativeLayoutDhuhr.setOnClickListener(view -> {
            notificationClick("Dhuhr_Sound", dhuhrSound, dhuhrSoundIcon, relativeLayoutDhuhr, dhuhrSoundText);
            dhuhrSound = !dhuhrSound;
        });
        relativeLayoutAsr.setOnClickListener(view -> {
            notificationClick("Asr_Sound", asrSound, asrSoundIcon, relativeLayoutAsr, asrSoundText);
            asrSound = !asrSound;
        });
        relativeLayoutMaghrib.setOnClickListener(view -> {
            notificationClick("Maghrib_Sound", maghribSound, maghribSoundIcon, relativeLayoutMaghrib, maghribSoundText);
            maghribSound = !maghribSound;
        });
        relativeLayoutIshaa.setOnClickListener(view -> {
            notificationClick("Ishaa_Sound", ishaaSound, ishaaSoundIcon, relativeLayoutIshaa, ishaaSoundText);
            ishaaSound = !ishaaSound;
        });

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void initNotification(Boolean prayerSound, ImageView icon, RelativeLayout relativeLayout, TextView textView) {
        if (prayerSound) {
            icon.setImageDrawable(soundOnIcon);
            relativeLayout.setBackgroundColor(colorPrimary);
            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            icon.setImageDrawable(soundOffIcon);
            relativeLayout.setBackgroundColor(colorBackground);
            textView.setTextColor(colorOnSurface);
        }
    }

    private void notificationClick(String savedKey, Boolean prayerSound, ImageView icon, RelativeLayout relativeLayout, TextView textView) {
        SharedPreferencesHelper.storeValue(requireContext(), savedKey, !prayerSound);
        if (prayerSound) {
            icon.setImageDrawable(soundOffIcon);
            relativeLayout.setBackgroundColor(colorBackground);
            textView.setTextColor(colorOnSurface);
        } else {
            icon.setImageDrawable(soundOnIcon);
            relativeLayout.setBackgroundColor(colorPrimary);
            textView.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("displayed", "SETTINGS FRAGMENT RSUMED");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.d("DISPLAY3", "DISPLAY DISMISSED FINALLY");
        currentLocation.setText(SharedPreferencesHelper.getValue(requireContext(), "location", ""));

    }


}