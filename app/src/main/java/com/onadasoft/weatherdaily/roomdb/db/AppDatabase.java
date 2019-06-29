package com.onadasoft.weatherdaily.roomdb.db;



import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.roomdb.dao.CityDao;

@Database(entities = {City.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
}
