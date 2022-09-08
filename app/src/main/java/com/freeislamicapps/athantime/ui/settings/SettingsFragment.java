package com.freeislamicapps.athantime.ui.settings;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.freeislamicapps.athantime.AlarmStart;
import com.freeislamicapps.athantime.BuildConfig;
import com.freeislamicapps.athantime.MainActivity;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.HttpDataHandler;
import com.freeislamicapps.athantime.ui.intro.LocationFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


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
        highLatsAdjustmentText = view.findViewById(R.id.highlatsadjustmentText);

        styleText = view.findViewById(R.id.styleText);


        MaterialCardView cardViewmethod = view.findViewById(R.id.cardViewMethod);
        cardViewmethod.setOnClickListener(new View.OnClickListener() {
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

        MaterialCardView cardViewStyle = view.findViewById(R.id.cardViewStyle);
        cardViewStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogStyle();
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        currentLocation.setText(loadData("longitude"));
        methodText.setText(loadData("Method"));
        asrCalculationText.setText(loadData("AsrCalculation"));
        highLatsAdjustmentText.setText(loadData("HighLatsAdjustment"));
        styleText.setText(loadData("Style"));



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

    private void showDialogStyle() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_style);

        LinearLayout cardViewAutomatic = dialog.findViewById(R.id.cardviewAutomatic);
        LinearLayout cardViewLightmode = dialog.findViewById(R.id.cardViewLightmode);
        LinearLayout cardViewDarkmode = dialog.findViewById(R.id.cardViewDarkmode);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        cardViewAutomatic.setOnClickListener(v -> {
            dialog.dismiss();
            styleText.setText("Automatic (System settings)");
            saveData("Style", "Automatic (System settings)");
        });

        cardViewLightmode.setOnClickListener(v -> {
            dialog.dismiss();
            styleText.setText("Always light mode");
            saveData("Style", "Always light mode");
        });

        cardViewDarkmode.setOnClickListener(v -> {
            dialog.dismiss();
            styleText.setText("Always dark mode");
            saveData("Style", "Always dark mode");
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

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    if(Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                        if(Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION))) {
                            getCurrentLocation();
                        }
                        else {
                            Snackbar.make(requireContext(), requireView(),"Please allow to retrieve your exact location to provide accurate prayer times",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Snackbar.make(requireContext(), requireView(),"Please allow to retrieve your location",Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                CircularProgressIndicator progressBar = requireActivity().findViewById(R.id.progressbar);
                TextView progressBarText = requireActivity().findViewById(R.id.progressbarText);
                progressBar.setVisibility(View.VISIBLE);
                progressBarText.setVisibility(View.VISIBLE);
                LocationServices.getFusedLocationProviderClient(requireActivity())
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(requireActivity())
                                        .removeLocationUpdates(this);


                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int index = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(index).getLatitude();
                                    double longitude = locationResult.getLocations().get(index).getLongitude();
                                    currentLocation.setText("Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                                    saveData("latitude", String.valueOf(latitude));
                                    saveData("longitude", String.valueOf(longitude));

                                    Handler handler = new Handler();

                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            HttpDataHandler httpDataHandler = new HttpDataHandler();
                                            String startUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=";
                                            String apikey = BuildConfig.MAPS_API_KEY;
                                            String response = httpDataHandler.getHTTPData(startUrl+apikey);
                                            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                                            Log.d("address",response);

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.GONE);
                                                    progressBarText.setVisibility(View.GONE);
                                                }
                                            });
                                        }

                                    };
                                    Thread thread = new Thread(runnable);
                                    thread.start();


                                }
                            }
                        }, Looper.getMainLooper());

            }
        }
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


    }
}