package com.freeislamicapps.athantime.ui.qiblah;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.BuildConfig;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.databinding.FragmentQiblahBinding;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class QiblahFragment extends Fragment implements SensorEventListener {
    private ImageView imageView;
    private TextView textView2;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magenetometerSensor;
    private final float[] lastAccelerometer = new float[3];
    private final float[] lastMagnetometer = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientation = new float[3];

    boolean isLastAccelerometerArrayCopied = false;
    boolean isLastMagnetometerArrayCopied = false;

    long lastUpdatedTime = 0;
    float currentDegree = 0f;

    LocationRequest locationRequest;
    ImageView qiblahArrow;
    MaterialCardView getMyLocationCard;
    TextView currentLocationText;
    View mainView;
    Double latitude,longitude;


    private FragmentQiblahBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QiblahViewModel qiblahViewModel =
                new ViewModelProvider(this).get(QiblahViewModel.class);

        binding = FragmentQiblahBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        qiblahViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mainView = binding.getRoot();

        imageView = binding.imageView;
        textView2 = binding.textView2;

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magenetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        qiblahArrow = binding.qiblahArrow;
        getMyLocationCard = binding.getmylocationCard;
        currentLocationText = binding.currentLocationText;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        currentLocationText.setText(SharedPreferencesHelper.getValue(requireContext(),"location",""));

        getMyLocationCard.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(requireContext(), getResources().getString(R.string.message_enable_location_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    if (Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                        if (Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION))) {
                            getCurrentLocation();
                        } else {
                            Snackbar.make(requireContext(), requireView(), getResources().getString(R.string.message_exact_location), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Snackbar.make(requireContext(), requireView(), getResources().getString(R.string.message_location), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                ProgressBar progressBar = mainView.findViewById(R.id.getmylocationProgressbar);
                TextView progressBarText = mainView.findViewById(R.id.getmylocationText);
                progressBar.setVisibility(View.VISIBLE);
                progressBarText.setText(getResources().getString(R.string.progress_get_location));
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
                                    //   currentLocation.setText("Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                                    SharedPreferencesHelper.storeValue(requireContext(), "latitude", latitude);
                                    SharedPreferencesHelper.storeValue(requireContext(), "longitude", longitude);
                                    progressBarText.setText(getResources().getString(R.string.progress_get_address));

                                    Handler handler = new Handler();

                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            OkHttpClient client = new OkHttpClient();
                                            String startUrl = "https://maps.googleapis.com/maps/api/geocode/json?";
                                            String latitude = SharedPreferencesHelper.getValue(requireContext(), "latitude", "52.0");
                                            String longitude = SharedPreferencesHelper.getValue(requireContext(), "longitude", "9.0");
                                            String midUrl = "latlng=" + latitude + "," + longitude;
                                            String endUrl = "&key=" + BuildConfig.MAPS_API_KEY ;
                                            System.out.println(startUrl+midUrl+endUrl);
                                            Request request = new Request.Builder()
                                                    .url(startUrl + midUrl + endUrl)
                                                    .get()
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
                                                        System.out.println(latitude + " " +longitude);
                                                        myResponseJson = new JSONObject(myResponse);
                                                        JSONArray results = myResponseJson.getJSONArray("results");
                                                        JSONObject firstResult = results.getJSONObject(0);
                                                        System.out.println(firstResult.get("formatted_address"));
                                                        //  SharedPreferencesHelper.storeValue(requireContext(), "location", myResponseJson.getString("display_name"));
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressBar.setVisibility(View.GONE);
                                                            progressBarText.setText(getResources().getString(R.string.progress_task_done));

                                                            try {
                                                                SharedPreferencesHelper.storeValue(mainView.getContext(), "location", firstResult.getString("formatted_address"));
                                                                currentLocationText.setText(firstResult.getString("formatted_address"));
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == accelerometerSensor) {
            System.arraycopy(sensorEvent.values,0,lastAccelerometer,0,sensorEvent.values.length);
            isLastAccelerometerArrayCopied = true;
        } else if(sensorEvent.sensor == magenetometerSensor) {
            System.arraycopy(sensorEvent.values,0,lastMagnetometer,0,sensorEvent.values.length);
            isLastMagnetometerArrayCopied = true;
        }
        if(isLastAccelerometerArrayCopied && isLastMagnetometerArrayCopied && System.currentTimeMillis() - lastUpdatedTime>250) {
            SensorManager.getRotationMatrix(rotationMatrix,null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix,orientation);

            float azimuthInRadians = orientation[0];
            float azimuthInDegree = (float) Math.toDegrees(azimuthInRadians);

            RotateAnimation rotateAnimation = new RotateAnimation(currentDegree,-azimuthInDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            imageView.startAnimation(rotateAnimation);

            currentDegree = -azimuthInDegree;
            lastUpdatedTime = System.currentTimeMillis();

            int x = (int) azimuthInDegree;
            textView2.setText(x+"°");

            double latitude = SharedPreferencesHelper.getValue(requireContext(), "latitude", 52.0);
            double longitude = SharedPreferencesHelper.getValue(requireContext(), "longitude", 9.0);

            qiblahArrow.setRotation(calculateQibla2(latitude,longitude)-azimuthInDegree);



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,magenetometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this,accelerometerSensor);
        sensorManager.unregisterListener(this,magenetometerSensor);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public double calculateQibla(double latitude, double longitude){
        double phiK = 21.4 * Math.PI / 180.0;
        double lambdaK = 39.8 * Math.PI/180.0;
        double phi = latitude*Math.PI/180.0;
        double lambda = longitude*Math.PI/180.0;
        double psi = 180.0/Math.PI * Math.atan2(Math.sin(lambdaK-lambda),Math.cos(phi)*Math.tan(phiK)-Math.sin(phi)*Math.cos(lambdaK-lambda));
        return Math.round(psi);
    }

    private float calculateQibla2(double latitude, double longitude) {
        if (Math.abs(latitude-21.4)<Math.abs(0.0-0.01) && Math.abs(longitude-39.8)<Math.abs(0.0-0.01))  {
            System.out.println("KEIN PLAM MEKKA?");//Mecca
        }
        double phiK = 21.4*Math.PI/180.0;
        double lambdaK = 39.8*Math.PI/180.0;
        double phi = latitude*Math.PI/180.0;
        double lambda = longitude*Math.PI/180.0;
        double psi = 180.0/Math.PI*Math.atan2(Math.sin(lambdaK-lambda), Math.cos(phi)*Math.tan(phiK)-Math.sin(phi)*Math.cos(lambdaK-lambda));
        return Math.round(psi);
    }

}