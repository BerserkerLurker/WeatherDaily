package com.onadasoft.weatherdaily.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.onadasoft.weatherdaily.APIRequests;
import com.onadasoft.weatherdaily.App;
import com.onadasoft.weatherdaily.GlobalData;
import com.onadasoft.weatherdaily.event.DataSyncEvent;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.utils.HelperFunctions;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class CurrentWeatherService extends IntentService {
    private static final String TAG = CurrentWeatherService.class.getSimpleName();
    private GlobalData globalData;

    public CurrentWeatherService() {
        super("CurrentWeatherService");
    }

    public CurrentWeatherService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int responseCode = 0;
        String responseMsg = "null response";

        globalData = App.getInstance().getGlobalData();

        if(!HelperFunctions.isNetworkAvailable(this)){
            EventBus.getDefault().post(new DataSyncEvent(responseMsg, responseCode));
            return;
        }
        if(intent != null){
            Log.d(TAG, "onHandleIntent, Thread name: " + Thread.currentThread().getName());

            LinkedHashMap<String, Current> map = globalData.getCurrentWeatherMap();
            LinkedHashMap<String, Current> mapAux = new LinkedHashMap<>();
            Gson gson = new Gson();

            long[] ids = intent.getLongArrayExtra("ids");

            for(long id : ids){
                String lat = "0";
                String lon = "0";
                if(id == -1){
                    lat = intent.getStringExtra("lat");
                    lon = intent.getStringExtra("lon");
                }
                APIRequests apiRequests= new APIRequests();
                Call call = apiRequests.runGetReq("/weather",id,
                        null,null,null, lat, lon,null,"json","metric");

                Response response = null;
                try {
                    Log.d(TAG, "onHandleIntent, Thread name: " + Thread.currentThread().getName());

                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if ((response != null) && (response.isSuccessful())){
                    responseCode = response.code();
                    responseMsg = response.message();
                    Log.d(TAG, "onHandleIntent: " + response.toString());
                    Current current = null;
                    try {
                        current = gson.fromJson(response.body().string(), Current.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "onHandleIntent: city "+ current.getName());
                    mapAux.put(current.getName(), current);
                    if(id == -1){
//                        map.remove(globalData.getLastKnowLocation());

                        globalData.saveLastLocation(current.getName());
                    }


                }else if (response != null){
                    responseCode = response.code();
                    responseMsg = response.message();
                }
            }

            if(!globalData.getLastKnowLocation().equals("*")){
                map.remove("*");
            }

            for (Current city : mapAux.values()){
                map.put(city.getName(), city);
            }

            globalData.setLastUpdateTimestamp(new Date(System.currentTimeMillis()));


            Log.d(TAG, "onHandleIntent: HashMap: " + map.toString());

            globalData.saveWeather();

            // ToDo -- response code is for last request only
            EventBus.getDefault().post(new DataSyncEvent(responseMsg, responseCode));

//            Maybe need to cancel requests
//            if(!apiRequests.queueIsEmpty()) {
//            Log.d("Api",apiRequests.queueIsEmpty() +" ");
//            apiRequests.cancelAll();
//        }
        }

    }
}
