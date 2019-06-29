package com.onadasoft.weatherdaily.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.onadasoft.weatherdaily.App;
import com.onadasoft.weatherdaily.R;
import com.onadasoft.weatherdaily.models.Coord;
import com.onadasoft.weatherdaily.models.recyclerCities.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


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

    private static String getStringRes(int addr){
        return App.getAppResources().getString(addr);
    }


    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null) return false;
        NetworkInfo.State network = networkInfo.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    public static Object getElementByIndex(LinkedHashMap map, int index){
        return map.get((map.keySet().toArray())[index]);
    }

//
//    public static Set<City> getCities(Activity activity){
//        Set<City> citiesList = new HashSet<>();
//        String json = readFromAssets(activity, "jsons/city.list.min.json");
//        Type listType = new TypeToken<HashSet<City>>() {}.getType();
//
//        // convert json into a list of cities
//        try {
//            citiesList = new Gson().fromJson(json,listType);
//        }catch (Exception e){
//            Log.e("Error parsing", e.toString());
//        }
//        return citiesList;
//    }
//
//    public static String readFromAssets(Activity activity, final String fileName){
//        String text = "";
//        try {
//            InputStream is = activity.getAssets().open(fileName);
//            int size = is.available();
//
//            // read the entire asset into a local buffer
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            text = new String(buffer, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return text;
//    }

    public static List<City> getCitiesFromJSON(Context context){
        String json = "";
        try {
            InputStream is = context.getAssets().open("jsons/city.list.min.json");
            int size = is.available();

            // read the entire asset into a local buffer
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
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
//
//    public static List<City> getCitiesFromJSON(Context context){
//        List<City> cityList = new LinkedList<>();
//        try{
//            InputStream is = context.getAssets().open("jsons/city.list.min.json");
//            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
//
//            // Read File in stream mode
//            reader.beginArray();
//
//            Gson gson = new GsonBuilder().create();
//
//            while (reader.hasNext()){
//                City cityJson = gson.fromJson(reader, City.class);
//                City city = new City();
//                city.setId(cityJson.getId());
//                city.setName(cityJson.getName());
//                city.setCountry(cityJson.getCountry());
//                city.setCoord(new Coord(cityJson.getCoord().getLon(),cityJson.getCoord().getLat()));
//                cityList.add(city);
//            }
//            reader.close();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return cityList;
//    }

}
