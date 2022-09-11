package com.freeislamicapps.athantime.ui.home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.freeislamicapps.athantime.PrayerTimes.QiblaTime;
import com.freeislamicapps.athantime.PrayerTimes.QiblaTimeCalculator;
import com.freeislamicapps.athantime.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment implements SensorEventListener {
    private ImageView imageView;
    private TextView textView2;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magenetometerSensor;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private  float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    boolean isLastAccelerometerArrayCopied = false;
    boolean isLastMagnetometerArrayCopied = false;

    long lastUpdatedTime = 0;
    float currentDegree = 0f;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        imageView = binding.imageView;
        textView2 = binding.textView2;

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magenetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        return root;
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

    public double calculateQibla(double latitude, double longitude){
        double phiK = 21.4 * Math.PI / 180.0;
        double lambdaK = 39.8 * Math.PI/180.0;
        double phi = latitude*Math.PI/180.0;
        double lambda = longitude*Math.PI/180.0;
        double psi = 180.0/Math.PI * Math.atan2(Math.sin(lambdaK-lambda),Math.cos(phi)*Math.tan(phiK)-Math.sin(phi)*Math.cos(lambdaK-lambda));
        return Math.round(psi);
    }

}