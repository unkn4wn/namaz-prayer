package com.freeislamicapps.athantime.ui.prayer;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freeislamicapps.athantime.BuildConfig;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.freeislamicapps.athantime.ui.settings.LocationModel;
import com.freeislamicapps.athantime.ui.settings.LocationRecyclerAdapter;
import com.freeislamicapps.athantime.ui.settings.LocationRecyclerInterface;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationFragment extends DialogFragment implements LocationRecyclerInterface {

    private LocationRequest locationRequest;
    LatLng latLng;

    private ArrayList<LocationModel> arrayList;

    View mainView;


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.bottomsheet_background_maps));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Log.d("displayed", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_settings_location, container, false);
        mainView = view;

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        double latitude = SharedPreferencesHelper.getValue(requireContext(), "latitude", 52.0);
        double longitude = SharedPreferencesHelper.getValue(requireContext(), "longitude", 9.0);
        latLng = new LatLng(latitude, longitude);


        ImageButton closeButton = view.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(view1 -> dismiss());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_location);


        SearchView searchView = view.findViewById(R.id.sv_location);

        searchView.setOnSearchClickListener(view12 -> {

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                arrayList = new ArrayList<>();
                LocationRecyclerAdapter locationRecyclerAdapter = new LocationRecyclerAdapter(arrayList, LocationFragment.this);
                recyclerView.setAdapter(locationRecyclerAdapter);
                Handler handler = new Handler(Looper.getMainLooper());
                OkHttpClient client = new OkHttpClient();
                String startUrl = "https://geoapify-platform.p.rapidapi.com/v1/geocode/search?apiKey=" + BuildConfig.GEOAPIFY_GEOCODING_API_KEY + "&text=";
                String midUrl = "&lang=" + Locale.getDefault().getLanguage(); //BEFORE: Locale.getDefault().getCountry().toLowerCase(Locale.ROOT);
                String endUrl = "&limit=20&type=city";
                Log.d("URLWEBSITE", startUrl + s + midUrl + endUrl);
                Request request = new Request.Builder()
                        .url(startUrl + s + midUrl + endUrl)
                        .get()
                        .addHeader("X-RapidAPI-Key", BuildConfig.RAPID_GEOCODING_API_KEY)
                        .addHeader("X-RapidAPI-Host", "geoapify-platform.p.rapidapi.com")
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


                        String myResponse = Objects.requireNonNull(response.body()).string();
                        JSONObject myResponseJson = null;
                        try {
                            myResponseJson = new JSONObject(myResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray jsonArray = Objects.requireNonNull(myResponseJson).getJSONArray("features");
                            System.out.println(jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                JSONObject propertiesJson = jsonObject.getJSONObject("properties");
                                String location = propertiesJson.getString("formatted");
                                Double latitude = propertiesJson.getDouble("lat");
                                Double longitude = propertiesJson.getDouble("lon");

                                LocationModel locationModel = new LocationModel(location, latitude, longitude);
                                arrayList.add(locationModel);
                                System.out.println(location + latitude + longitude);

                            }
                            handler.post(() -> {
                                LocationRecyclerAdapter locationRecyclerAdapter1 = new LocationRecyclerAdapter(arrayList, LocationFragment.this);
                                recyclerView.setAdapter(locationRecyclerAdapter1);
                                LinearLayoutManager llm = new LinearLayoutManager(requireContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(llm);
                                searchView.clearFocus();
                            });

                            client.dispatcher().executorService().shutdown();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        MaterialCardView getmylocationButton = view.findViewById(R.id.getmylocationCard);
        getmylocationButton.setOnClickListener(view13 -> {
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
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("displayed", "onViewCreated");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }
    }

    @Override
    public void onItemClick(int position) {
        SharedPreferencesHelper.storeValue(requireContext(), "location", arrayList.get(position).getLocation());
        SharedPreferencesHelper.storeValue(requireContext(), "latitude", arrayList.get(position).getLatitude());
        SharedPreferencesHelper.storeValue(requireContext(), "longitude", arrayList.get(position).getLongitude());
        dismiss();
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
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


                                if (locationResult.getLocations().size() > 0) {
                                    int index = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(index).getLatitude();
                                    double longitude = locationResult.getLocations().get(index).getLongitude();
                                    //   currentLocation.setText("Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                                    SharedPreferencesHelper.storeValue(requireContext(), "latitude", latitude);
                                    SharedPreferencesHelper.storeValue(requireContext(), "longitude", longitude);
                                    progressBarText.setText(getResources().getString(R.string.progress_get_address));

                                    Handler handler = new Handler();

                                    Runnable runnable = () -> {
                                        OkHttpClient client = new OkHttpClient();
                                        String startUrl = "https://forward-reverse-geocoding.p.rapidapi.com/v1/reverse?";
                                        String latitude1 = SharedPreferencesHelper.getValue(requireContext(), "latitude", "52.0");
                                        String longitude1 = SharedPreferencesHelper.getValue(requireContext(), "longitude", "9.0");
                                        String midUrl = "lat=" + latitude1 + "&lon=" + longitude1;
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
                                                String myResponse = Objects.requireNonNull(response.body()).string();
                                                JSONObject myResponseJson;
                                                try {
                                                    myResponseJson = new JSONObject(myResponse);
                                                    SharedPreferencesHelper.storeValue(requireContext(), "location", myResponseJson.getString("display_name"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                        handler.postDelayed(() -> dismiss(), 1000);

                                        handler.post(() -> {
                                            progressBar.setVisibility(View.GONE);
                                            progressBarText.setText(getResources().getString(R.string.progress_task_done));
                                            //   dismiss();
                                        });
                                    };
                                    Thread thread = new Thread(runnable);
                                    thread.start();


                                }
                            }
                        }, Looper.getMainLooper());

            }
        }
    }


}