package com.freeislamicapps.athantime.ui.intro;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.freeislamicapps.athantime.BuildConfig;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class IntroLocationFragment extends Fragment {
    private LocationRequest locationRequest;

    View mainView;
    private Context mContext;

    public IntroLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.intro_fragment_location, container, false);

        mainView = view;

        Button enableLocationButton = view.findViewById(R.id.enableLocationButton);
        enableLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // WHEN Permission is granted
                        getCurrentLocation();
                    } else {
                        requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enable Location and Internet first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button maybeLaterButton = view.findViewById(R.id.maybeLaterButton);
        maybeLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new IntroLocationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new IntroNotificationFragment());
                transaction.commit();
            }
        });

        return view;
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    if (Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                        if (Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION))) {
                            getCurrentLocation();
                        } else {
                            Snackbar.make(requireContext(), requireView(), "Please allow to retrieve your exact location to provide accurate prayer times", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Snackbar.make(requireContext(), requireView(), "Please allow to retrieve your location", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                CircularProgressIndicator progressBar = requireActivity().findViewById(R.id.progressbarLocation);
                TextView progressBarText = requireActivity().findViewById(R.id.progressbarTextLocation);
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
                                    SharedPreferencesHelper.storeValue(requireContext(), "latitude", latitude);
                                    SharedPreferencesHelper.storeValue(requireContext(), "longitude", longitude);
                                    progressBar.setVisibility(View.GONE);
                                    progressBarText.setVisibility(View.GONE);
                                    requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    Handler handler = new Handler();

                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            OkHttpClient client = new OkHttpClient();
                                            String startUrl = "https://forward-reverse-geocoding.p.rapidapi.com/v1/reverse?";
                                            String latitude = SharedPreferencesHelper.getValue(mContext, "latitude", "52.0");
                                            String longitude = SharedPreferencesHelper.getValue(mContext, "longitude", "9.0");
                                            String midUrl = "lat=" + latitude + "&lon=" + longitude;
                                            String endUrl = "&accept-language=en&polygon_threshold=0.0";
                                            Request request = new Request.Builder()
                                                    .url(startUrl + midUrl + endUrl)
                                                    .get()
                                                    .addHeader("X-RapidAPI-Key", BuildConfig.RAPID_GEOCODING_API_KEY)
                                                    .addHeader("X-RapidAPI-Host", "forward-reverse-geocoding.p.rapidapi.com")
                                                    .build();

                                            client.newCall(request).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                                                }

                                                @Override
                                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                    String myResponse = response.body().string();
                                                    JSONObject myResponseJson = null;
                                                    try {
                                                        myResponseJson = new JSONObject(myResponse);
                                                        SharedPreferencesHelper.storeValue(mContext, "location", myResponseJson.getString("display_name"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.GONE);
                                                    progressBarText.setText("Task Done");
                                                }
                                            });
                                        }

                                    };
                                    Thread thread = new Thread(runnable);
                                    thread.start();

                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, new IntroNotificationFragment());
                                    transaction.commit();
                                }
                            }
                        }, Looper.getMainLooper());

            }
        }
    }
}