package com.onadasoft.weatherdaily.models.recyclerCities;



import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.onadasoft.weatherdaily.models.Coord;

import org.json.JSONObject;

@Entity
public class City {
    @PrimaryKey
    private long id;
    private String name;
    private String country;
    @Embedded
    private Coord coord;

    public City(){

    }

    public City(JSONObject json){
        this.id = json.optLong("id");
        this.name = json.optString("name");
        this.country = json.optString("country");
        this.coord = new Coord(json.optJSONObject("coord"));
    }

    public City(long id, String name, String country, Coord coord) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coord = coord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", coord=" + coord +
                '}';
    }
}
