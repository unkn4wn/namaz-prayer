package com.freeislamicapps.athantime.ui.settings;

import static android.content.Context.LOCATION_SERVICE;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.ui.intro.NotificationFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapsFragment extends DialogFragment implements OnMapReadyCallback {
    SearchView searchView;
    GoogleMap googleMap;
    SupportMapFragment map;

    Button enableLocationButton;
    private LocationRequest locationRequest;
    public static final String SHARED_PREFS = "sharedPrefs";
    LatLng latLng;

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.bottomsheet_background_maps));
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d("displayed","onMapReady");
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,10);
            googleMap.moveCamera(cameraUpdate);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("displayed","onCreateView");
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        map = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        latLng = new LatLng(Double.parseDouble(loadData("latitude")),Double.parseDouble(loadData("longitude")));



        ImageButton closeButton = view.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        enableLocationButton = view.findViewById(R.id.enableLocationButton);
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

        // TODO SEARCHVIEW FOR MAP
        /*
        searchView = view.findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if(location!=null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(requireContext());
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        }); */


        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("displayed","onViewCreated");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
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

              enableLocationButton.setText("Loading location...");
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
                                     latLng = new LatLng(latitude,longitude);
                                    saveData("latitude", String.valueOf(latitude));
                                    saveData("longitude", String.valueOf(longitude));
                                    enableLocationButton.setText("Get your Location");
                                    Log.d("currentLocationLat",String.valueOf(latitude));
                                    Log.d("currentLocationlong",String.valueOf(longitude));

                                    map.getMapAsync(MapsFragment.this);

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("displayed","BottomonMapReady");
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,10);
        googleMap.animateCamera(cameraUpdate);

    }
}