package com.onadasoft.weatherdaily.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

public class Coord implements Serializable {
    @SerializedName("lon")
    private double lon;

    @SerializedName("lat")
    private double lat;

    public Coord(){

    }

    public Coord(JSONObject json){
        this.lon = json.optDouble("lon");
        this.lat = json.optDouble("lat");
    }

    public Coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "lon=" + lon +
                ", lat=" + lat +
                "}";
    }
}
