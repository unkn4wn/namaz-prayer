package com.freeislamicapps.athantime.ui.settings;

import static android.content.Context.LOCATION_SERVICE;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.freeislamicapps.athantime.BuildConfig;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.helper.HttpDataHandler;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsFragment extends DialogFragment implements RecyclerViewInterface {
    SearchView searchView;
    GoogleMap googleMap;
    SupportMapFragment map;

    CardView enableLocationCard;
    ProgressBar enableLocationProgressbar;
    TextView enableLocationText;
    private LocationRequest locationRequest;
    public static final String SHARED_PREFS = "sharedPrefs";
    LatLng latLng;

    private ArrayList<locationModel> arrayList;

    Spinner spinnerCountry;


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
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
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Log.d("displayed", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        latLng = new LatLng(Double.parseDouble(loadData("latitude", "")), Double.parseDouble(loadData("longitude", "")));



        ImageButton closeButton = view.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_location);


        SearchView searchView = view.findViewById(R.id.sv_location);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                arrayList = new ArrayList<>();
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayList, MapsFragment.this);
                recyclerView.setAdapter(recyclerAdapter);
                Handler handler = new Handler(Looper.getMainLooper());
                OkHttpClient client = new OkHttpClient();
                String startUrl = "https://geoapify-platform.p.rapidapi.com/v1/geocode/search?apiKey=857d9a3e83b44167a3a4801a3fcd7edf&text=";
                String midUrl = "&lang=" + loadData("countrycode", "en") + "&countrycodes=" + loadData("countrycode", "en"); //BEFORE: Locale.getDefault().getCountry().toLowerCase(Locale.ROOT);
                String endUrl = "&limit=20&type=city";
                Log.d("URLWEBSITE", startUrl + s + midUrl + endUrl);
                Request request = new Request.Builder()
                        .url(startUrl + s + midUrl + endUrl)
                        .get()
                        .addHeader("X-RapidAPI-Key", "0a2ca5b1a0mshbc853a41b466c39p13f5c9jsn8c6b152135fa")
                        .addHeader("X-RapidAPI-Host", "geoapify-platform.p.rapidapi.com")
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {


                        String myResponse = response.body().string();
                        JSONObject myResponseJson = null;
                        try {
                            myResponseJson = new JSONObject(myResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray jsonArray = myResponseJson.getJSONArray("features");
                            System.out.println(jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                JSONObject propertiesJson = jsonObject.getJSONObject("properties");
                                String location = propertiesJson.getString("formatted");
                                Double latitude = propertiesJson.getDouble("lat");
                                Double longitude = propertiesJson.getDouble("lon");

                                locationModel locationModel = new locationModel(location, latitude, longitude);
                                arrayList.add(locationModel);
                                System.out.println(location + latitude + longitude);

                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayList, MapsFragment.this);
                                    recyclerView.setAdapter(recyclerAdapter);
                                    LinearLayoutManager llm = new LinearLayoutManager(requireContext());
                                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(llm);
                                    searchView.clearFocus();
                                }
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

        spinnerCountry = view.findViewById(R.id.spinnerCountry);

        setData();
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Country country = (Country) parent.getSelectedItem();
                saveData("countrycode", country.getId().toLowerCase(Locale.ROOT));

                saveData("countryPosition",String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // TODO LOCATION FOR MAP
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
        Log.d("displayed", "onViewCreated");
    }


    public void saveData(String savedKey, String savedValue) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedKey, savedValue);
        editor.apply();
    }

    public String loadData(String savedKey, String defaultValue) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(savedKey, defaultValue);
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
        saveData("location", String.valueOf(arrayList.get(position).getLocation()));
        saveData("latitude", String.valueOf(arrayList.get(position).getLatitude()));
        saveData("longitude", String.valueOf(arrayList.get(position).getLongitude()));
        dismiss();
    }

    private void setData() {

        ArrayList<Country> countryList = new ArrayList<>();
        //Add countries

        Map<String, String> englishcountries = Arrays.stream(Locale.getISOCountries()).collect(
                Collectors.toMap((String code) -> new Locale("", code).getDisplayCountry(Locale.getDefault()),
                        Function.identity(), (o1, o2) -> o1, TreeMap::new));


        for (Map.Entry<String, String> entry : englishcountries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            countryList.add(new Country(value, key));
        }




/*
        List keys = new ArrayList(englishcountries.values());
        int index = keys.indexOf(Locale.getDefault().getCountry()); */

        //fill data in spinner
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(requireContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setSelection(Integer.parseInt(loadData("countryPosition","0")));
    }
}