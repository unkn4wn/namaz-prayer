package com.freeislamicapps.athantime.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.freeislamicapps.athantime.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.material.card.MaterialCardView;


public class SettingsFragment extends Fragment implements DialogInterface.OnDismissListener {
    TextView currentLocation;
    TextView methodText, asrCalculationText, highLatsAdjustmentText, styleText;
    private LocationRequest locationRequest;
    public static final String SHARED_PREFS = "sharedPrefs";


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        Log.d("display2","VISIBLE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);



        MaterialCardView cardViewLocation = view.findViewById(R.id.cardViewLocation);
        currentLocation = view.findViewById(R.id.currentlocation);
        methodText = view.findViewById(R.id.methodsText);
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

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        currentLocation.setText(loadData("location"));
        methodText.setText(loadData("Method"));
        asrCalculationText.setText(loadData("AsrCalculation"));
        highLatsAdjustmentText.setText(loadData("HighLatsAdjustment"));



        cardViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, new MapsFragment());
                transaction.commit(); */
            MapsFragment mapsFragment = new MapsFragment();
            mapsFragment.show(getChildFragmentManager(),"MyFragment");
            }
        });


        return view;
    }


    private void showDialogHighlatsadjustment() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_highlatsadjustment);

        LinearLayout cardViewHighLatsNone = dialog.findViewById(R.id.cardViewHighlatsnone);
        LinearLayout cardViewHighLatsAngle = dialog.findViewById(R.id.cardViewHighlatsangle);
        LinearLayout cardViewHighLatsMiddle = dialog.findViewById(R.id.cardViewHighlatsmiddle);
        LinearLayout cardViewHighLatsSeventh = dialog.findViewById(R.id.cardViewHighlatsseventh);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);


        cardViewHighLatsNone.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("None");
            saveData("HighLatsAdjustment", "None");
        });

        cardViewHighLatsAngle.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("Angle-Based");
            saveData("HighLatsAdjustment", "Angle-Based");
        });

        cardViewHighLatsMiddle.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("Middle of the night");
            saveData("HighLatsAdjustment", "Middle of the night");
        });

        cardViewHighLatsSeventh.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText("One-Seventh of the Night");
            saveData("HighLatsAdjustment", "One-Seventh of the Night");
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
            saveData("AsrCalculation", "Shafi, Hanbali, Maliki");
        });

        cardViewHanafi.setOnClickListener(v -> {
            dialog.dismiss();
            asrCalculationText.setText("Hanafi");
            saveData("AsrCalculation", "Hanafi");
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
        dialog.setContentView(R.layout.bottomsheet_methods);

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
            saveData("Method", "Egyptian General Authority of Survey");
        });

        cardviewTehran.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Institute of Geophysics, University of Tehran");
            saveData("Method", "Institute of Geophysics, University of Tehran");
        });

        cardViewIsna.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Islamic Society of North America (ISNA)");
            saveData("Method", "Islamic Society of North America (ISNA)");
        });

        cardViewMwl.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Muslim World League (MWL)");
            saveData("Method", "Muslim World League (MWL)");
        });

        cardViewMakkah.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Umm Al-Qura University, Makkah");
            saveData("Method", "Umm Al-Qura University, Makkah");
        });

        cardviewUoif.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Union des organisations islamiques de France");
            saveData("Method", "Union des organisations islamiques de France");
        });

        cardViewKarachi.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("University of Islamic Sciences, Karachi");
            saveData("Method", "University of Islamic Sciences, Karachi");
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


    public void saveData(String savedKey, String savedValue) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedKey, savedValue);
        editor.apply();
    }

    public String loadData(String savedKey) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(savedKey, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("displayed","SETTINGS FRAGMENT RSUMED");
    }

    public void updateLocationText() {
        currentLocation.setText("");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.d("DISPLAY3","DISPLAY DISMISSED FINALLY");
        currentLocation.setText(loadData("location"));

    }
}