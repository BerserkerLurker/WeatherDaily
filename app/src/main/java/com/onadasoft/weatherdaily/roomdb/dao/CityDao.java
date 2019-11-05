package com.onadasoft.weatherdaily.roomdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.onadasoft.weatherdaily.models.recyclerCities.City;

import java.util.List;

@Dao
public interface CityDao {
    @Query("SELECT * FROM city")
    List<City> getAll();

    @Query("SELECT * FROM city WHERE name LIKE :name || '%' ORDER BY name ASC LIMIT 10")
    List<City> findByName(String name);

    @Query("SELECT COUNT(*) FROM city")
    int getCitySize();

    @Insert
    void insertAll(City... cities);

    @Insert
    void insertCity(City city);

    @Delete
    void deleteCity(City city);

    @Query("DELETE FROM city")
    void nukeCities();
}
