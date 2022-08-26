package com.freeislamicapps.athantime.ui.settings;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.freeislamicapps.athantime.AlarmStart;
import com.freeislamicapps.athantime.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Calendar;


public class SettingsFragment extends Fragment {
    TextView currentLocation;
    TextView methodText, asrCalculationText, highLatsAdjustmentText;
    private LocationRequest locationRequest;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button refreshLocationBtn = view.findViewById(R.id.refreshlocation2);
        currentLocation = view.findViewById(R.id.currentlocation);
        methodText = view.findViewById(R.id.methodsText);
        asrCalculationText = view.findViewById(R.id.asrcalculationText);
        highLatsAdjustmentText = view.findViewById(R.id.highlatsadjustmentText);

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

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        currentLocation.setText(loadData("longitude"));
        methodText.setText(loadData("Method"));
        asrCalculationText.setText(loadData("AsrCalculation"));
        highLatsAdjustmentText.setText(loadData("HighLatsAdjustment"));


        refreshLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // WHEN Permission is granted
                        getCurrentLocation();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                    }
                }
                else {
                    Toast.makeText(requireContext(),"Please enable Location first",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void showDialogHighlatsadjustment() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_highlatsadjustment);

        MaterialCardView cardViewHighLatsNone = dialog.findViewById(R.id.cardViewHighlatsnone);
        MaterialCardView cardViewHighLatsAngle = dialog.findViewById(R.id.cardViewHighlatsangle);
        MaterialCardView cardViewHighLatsMiddle = dialog.findViewById(R.id.cardViewHighlatsmiddle);
        MaterialCardView cardViewHighLatsSeventh = dialog.findViewById(R.id.cardViewHighlatsseventh);


        cardViewHighLatsNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                highLatsAdjustmentText.setText("None");
                saveData("HighLatsAdjustment", "None");

            }
        });

        cardViewHighLatsAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                highLatsAdjustmentText.setText("Angle-Based");
                saveData("HighLatsAdjustment", "Angle-Based");

            }
        });

        cardViewHighLatsMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                highLatsAdjustmentText.setText("Middle of the night");
                saveData("HighLatsAdjustment", "Middle of the night");
            }
        });

        cardViewHighLatsSeventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                highLatsAdjustmentText.setText("One-Seventh of the Night");
                saveData("HighLatsAdjustment", "One-Seventh of the Night");

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

        MaterialCardView cardViewShafi = dialog.findViewById(R.id.cardViewShafi);
        MaterialCardView cardViewHanafi = dialog.findViewById(R.id.cardViewHanafi);


        cardViewShafi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                asrCalculationText.setText("Shafi, Hanbali, Maliki");
                saveData("AsrCalculation", "Shafi, Hanbali, Maliki");

            }
        });

        cardViewHanafi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                asrCalculationText.setText("Hanafi");
                saveData("AsrCalculation", "Hanafi");

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

        MaterialCardView cardviewEgypt = dialog.findViewById(R.id.cardViewEgypt);
        MaterialCardView cardviewTehran = dialog.findViewById(R.id.cardViewTehran);
        MaterialCardView cardViewIsna = dialog.findViewById(R.id.cardViewIsna);
        MaterialCardView cardViewMwl = dialog.findViewById(R.id.cardViewMwl);
        MaterialCardView cardViewMakkah = dialog.findViewById(R.id.cardViewMakkah);
        MaterialCardView cardviewUoif = dialog.findViewById(R.id.cardViewUOIF);
        MaterialCardView cardViewKarachi = dialog.findViewById(R.id.cardViewKarachi);


        cardviewEgypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("Egyptian General Authority of Survey");
                saveData("Method", "Egyptian General Authority of Survey");

            }
        });

        cardviewTehran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("Institute of Geophysics, University of Tehran");
                saveData("Method", "Institute of Geophysics, University of Tehran");

            }
        });

        cardViewIsna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("Islamic Society of North America (ISNA)");
                saveData("Method", "Islamic Society of North America (ISNA)");

            }
        });

        cardViewMwl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("Muslim World League (MWL)");
                saveData("Method", "Muslim World League (MWL)");

            }
        });

        cardViewMakkah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("Umm Al-Qura University, Makkah");
                saveData("Method", "Umm Al-Qura University, Makkah");

            }
        });

        cardviewUoif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("Union des organisations islamiques de France");
                saveData("Method", "Union des organisations islamiques de France");

            }
        });

        cardViewKarachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                methodText.setText("University of Islamic Sciences, Karachi");
                saveData("Method", "University of Islamic Sciences, Karachi");

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(requireActivity(), "Permission needed", Toast.LENGTH_SHORT).show();
        }
    }


    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                CircularProgressIndicator progressBar = requireActivity().findViewById(R.id.progressbar);
                TextView progressBarText = requireActivity().findViewById(R.id.progressbarText);
                progressBar.setVisibility(View.VISIBLE);
                progressBarText.setVisibility(View.VISIBLE);
                requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                                    progressBar.setVisibility(View.GONE);
                                    progressBarText.setVisibility(View.GONE);
                                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    public void updateViews() {
        // currentLocation.setText(loadedText);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HELLO","DARK TIMES");
        setAlarm();
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(requireContext(), AlarmStart.class);

        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }

        Toast.makeText(requireContext(),"Alarm is set",Toast.LENGTH_SHORT).show();
    }
}