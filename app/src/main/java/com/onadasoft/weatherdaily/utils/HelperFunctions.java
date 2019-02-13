package com.onadasoft.weatherdaily.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.onadasoft.weatherdaily.App;
import com.onadasoft.weatherdaily.R;

import java.util.Date;



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


    public boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null) return false;
        NetworkInfo.State network = networkInfo.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

}
