package com.islamicproapps.namaz.ui.settings;

public class LocationModel {
    private final String location;
    private final Double latitude;
    private final Double longitude;


    public LocationModel(String location, Double latitude, Double longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
