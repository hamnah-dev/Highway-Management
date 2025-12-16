package com.motorway.model;

import java.io.Serializable;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    private double lat;
    private double lng;
    private String address;

    public Location(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public String getAddress() { return address; }

    public void setLat(double latitude) { this.lat= lat; }
    public void setLng(double longitude) { this.lng = lng; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("%s (%.4f, %.4f)", address, lat, lng);
    }
}