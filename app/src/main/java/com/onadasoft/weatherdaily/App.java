package com.onadasoft.weatherdaily;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;


import com.facebook.stetho.Stetho;
import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.roomdb.db.AppDatabase;
import com.onadasoft.weatherdaily.utils.HelperFunctions;
import com.onadasoft.weatherdaily.utils.notifRunnable.NotifyingRunnable;
import com.onadasoft.weatherdaily.utils.notifRunnable.RunnableCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class App extends Application implements RunnableCompleteListener {

    // public int DB_EMPTY = 1;

    private final CountDownLatch startLatch = new CountDownLatch(1);

    private static Resources resources;

    private AppDatabase appDB;

    //--- get Application singleton
    static App myAppInstance;
    public App() {
        myAppInstance = this;
    }
    public static App getInstance() {
        return myAppInstance;
    }
    //---

    @Override
    public void onCreate() {
        super.onCreate();

        // ToDo -- Debug only (Stetho)
        Stetho.initializeWithDefaults(this);

        resources = getResources();

        //appDB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "cities-db").build();
        appDB = AppDatabase.getDatabase(this);

        Thread thread = Thread.currentThread();
        Log.d("threadmain", thread.getName());
        NotifyingRunnable readCitiesJSON = new NotifyingRunnable() {
            List<City> citiesList = new ArrayList<>();

            @Override
            public void doRun() {
                int dbCitySize = appDB.cityDao().getCitySize();
                // if db is empty
                if (dbCitySize == 0){
                    Thread thread = Thread.currentThread();
                    Log.d("thread", thread.getName());
                    citiesList = HelperFunctions.getCitiesFromJSON(getApplicationContext());
                    Log.d("blob", citiesList.toString());
                    appDB.cityDao().insertAll(citiesList.toArray(new City[citiesList.size()]));
                }
            }
        };
        readCitiesJSON.addListener(this);
        Thread separate = new Thread(readCitiesJSON);
        separate.start();


    }

    public static Resources getAppResources(){
        return resources;
    }

    public AppDatabase getDatabase() {
        return appDB;
    }

    public CountDownLatch getStartLatch() {
        return startLatch;
    }

    @Override
    public void notifyOfRunnableComplete(Runnable runnable) {
        // DB_EMPTY = 0;
        startLatch.countDown();

    }
}
