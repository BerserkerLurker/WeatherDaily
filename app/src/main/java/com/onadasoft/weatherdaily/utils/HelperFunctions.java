package com.onadasoft.weatherdaily.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onadasoft.weatherdaily.App;
import com.onadasoft.weatherdaily.R;
import com.onadasoft.weatherdaily.models.recyclerCities.City;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


public class HelperFunctions {

    // TODO -- Handle more icon varieties
    public static String setWeatherIcon(int weatherId, long sunrise, long sunset){
        int idCat = weatherId / 100;

        String icon = "";

        if (weatherId == 800){
            long currentTime = new Date().getTime();

            if (currentTime >= sunrise && currentTime < sunset){
                icon = getStringRes(R.string.wi_day_sunny);
            }else{
                icon = getStringRes(R.string.wi_night_clear);
            }
        }else {
            switch (idCat){
                case 2 : icon = getStringRes(R.string.wi_thunderstorm);
                    break;
                case 3 : icon = getStringRes(R.string.wi_sprinkle);
                    break;
                case 7 : icon = getStringRes(R.string.wi_fog);
                    break;
                case 8 : icon = getStringRes(R.string.wi_cloudy);
                    break;
                case 6 : icon = getStringRes(R.string.wi_snow);
                    break;
                case 5 : icon = getStringRes(R.string.wi_rain);
                    break;
            }

        }
        return icon;
    }

    public static String getStringRes(int addr){
        return App.getAppResources().getString(addr);
    }


    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
//        if(networkInfo == null) return false;
//        NetworkInfo.State network = networkInfo.getState();
//        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    public static Object getElementByIndex(LinkedHashMap map, int index){
        return map.get((map.keySet().toArray())[index]);
    }

    public static Object getKeyByIndex(LinkedHashMap map, int index){
        return map.keySet().toArray()[index];
    }

    public static List<City> getCitiesFromJSON(Context context){
        String json = "";
        try {
            InputStream is = context.getAssets().open("jsons/city.list.min.json");
            int size = is.available();

            // read the entire asset into a local buffer
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<City> citiesList = new ArrayList<City>();
        Type listType = new TypeToken<ArrayList<City>>() {}.getType();

        // convert json into a list of cities
        try {
            citiesList = new Gson().fromJson(json, listType);
        }catch (Exception e){
            Log.e("Error parsing", e.toString());
        }
        return citiesList;
    }

}
