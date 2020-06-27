package com.onadasoft.weatherdaily;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onadasoft.weatherdaily.models.Current;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class GlobalData {
    private Gson gson = new Gson();
    private Type mapType = new TypeToken<LinkedHashMap<String, Current>>(){}.getType();
    // private PreferenceManager prefMgr;
    private SharedPreferences sharedPref;



    private LinkedHashMap<String, Current> currentWeatherMap = new LinkedHashMap<>();
    private boolean CWMAP_EMPTY = true;
    private Date lastUpdateTimestamp;
    private String lastKnowLocation;


    public GlobalData(Context context, String key){
        // prefMgr = new PreferenceManager(context);
        sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        init();
    }

    private void init() {
        Gson gson = new Gson();
        Type mapType = new TypeToken<LinkedHashMap<String, Current>>(){}.getType();

        if (sharedPref.contains("data")){
            String currentJson = sharedPref.getString("data", "");
            if(!(currentJson.equals("") || currentJson.equals("{}"))){
                currentWeatherMap = gson.fromJson(currentJson, mapType);
                CWMAP_EMPTY = false;
            }
        }

        if (sharedPref.contains("lastUpdate")){
            String timestamp = sharedPref.getString("lastUpdate", "");
            Log.d("global", "init: " + timestamp);

            if (!timestamp.equals("")){
                lastUpdateTimestamp = new Date(Long.parseLong(timestamp));
                Log.d("global", "init: " + lastUpdateTimestamp.toString());
            }
        }

        lastKnowLocation = "*";
        if (sharedPref.contains("lastLocation")){
            String location = sharedPref.getString("lastLocation", "*");
            Log.d("global", "init: " + location);

            if (!location.equals("*")){
                if(currentWeatherMap.containsKey(location)){
                    lastKnowLocation = location;
                }
                Log.d("global", "init: " + lastKnowLocation);
            }
        }
    }

    public void saveWeather(){
        String json = gson.toJson(currentWeatherMap, mapType);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("data", json);

        Date now = new Date();
        String timestamp = String.valueOf(now.getTime());

        editor.putString("lastUpdate", timestamp);

        editor.apply();
    }

    public void saveLastLocation(String location){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastLocation", location);
        editor.apply();
        lastKnowLocation = location;
    }

    public LinkedHashMap<String, Current> getCurrentWeatherMap() {
        return currentWeatherMap;
    }

//    public void setCurrentWeatherMap(LinkedHashMap<String, Current> currentWeatherMap) {
//        this.currentWeatherMap = currentWeatherMap;
//    }


    public boolean isCWMAP_EMPTY() {
        return CWMAP_EMPTY;
    }

    public void setCWMAP_EMPTY(boolean CWMAP_EMPTY) {
        this.CWMAP_EMPTY = CWMAP_EMPTY;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getLastKnowLocation(){
        return lastKnowLocation;
    }
}
