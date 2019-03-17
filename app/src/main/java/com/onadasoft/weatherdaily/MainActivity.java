package com.onadasoft.weatherdaily;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onadasoft.weatherdaily.adapters.MainPagerAdapter;
import com.onadasoft.weatherdaily.adapters.SmartFragmentStatePagerAdapter;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.utils.HelperFunctions;
import com.onadasoft.weatherdaily.utils.PreferenceManager;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnCity;
    private FrameLayout fl;
    PreferenceManager prefMgr;
    SharedPreferences sharedPref;

    Gson gson = new Gson();
    Type type = new TypeToken<LinkedHashMap<String, Current>>(){}.getType();

    public static boolean CWMapEmpty = true;
    LinkedHashMap<String, Current> currentWeatherMap = new LinkedHashMap<>();

    SmartFragmentStatePagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize the weatherMap from stored sharedPrefs
        prefMgr = new PreferenceManager(this);
        sharedPref = prefMgr.getSharedPreferences("weatherDaily");
        if(sharedPref.contains("initialized")){
            if(currentWeatherMap.isEmpty()){
                String currentJson = prefMgr.getString(sharedPref, "initialized", "");
                currentWeatherMap = gson.fromJson(currentJson, type);
                CWMapEmpty = false;
            }
        }else{
            //if (HelperFunctions.isNetworkAvailable(this)) {
                CWMapEmpty = true;
                testFill();

                getWeather();

                saveWeather();
                CWMapEmpty = false;
           // }else {
             //   Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
           // }

        }





        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.vpPager);

        //if(currentWeatherMap!=null) {
            fragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), currentWeatherMap);


            viewPager.setAdapter(fragmentPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager, true);
       // }
/*
        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                testChange();
                ((MainPagerAdapter) fragmentPagerAdapter).setCurrentWeatherMap(currentWeatherMap);
            }
        };
        handler.postDelayed(r, 3000);
*/



/*

       // Type type = new TypeToken<Map<String, Current>>(){}.getType();
        String json = gson.toJson(currentWeatherMap, type);

//        PreferenceManager prefMgr= new PreferenceManager(this);
//        SharedPreferences sharedPref = prefMgr.getSharedPreferences("currentTest");
//        prefMgr.setString(sharedPref, "test", json);


        String testJson = prefMgr.getString(sharedPref, "test", "");
        Map<String, Current> test = gson.fromJson(testJson, type);

        //Log.d("currentMap", currentWeatherMap.keySet().toString());
        Log.d("currentMap", test.keySet().toString());
*/
        fl = findViewById(R.id.flActionPlaceholder);

        btnCity = findViewById(R.id.btn_city);
        btnCity.setOnClickListener(this);


    }

    private void testFill() {
        currentWeatherMap.put("Tunis", null);
        currentWeatherMap.put("Paris", null);
        currentWeatherMap.put("Berlin", null);
    }

    private void testChange() {
        currentWeatherMap.put("Tunis", null);
        currentWeatherMap.put("Paris", null);
        currentWeatherMap.put("Berlin", null);
        currentWeatherMap.put("London", null);
        currentWeatherMap.put("Madrid", null);
    }

    private void getWeather(){
        APIRequests apiRequests = new APIRequests();

//        Gson gson = new Gson();

        for(String city:currentWeatherMap.keySet()){
            try {

                // TODO -- handle connection availability
                Response response = apiRequests.runGetReq("/weather",
                        city,null,null,null,null,null,"json","metric");

                //Log.d("response 1", response.body().string());

                Current current = gson.fromJson(response.body().charStream(), Current.class);
                Log.d(city + "resp: ", current.toString());

                currentWeatherMap.put(city, current);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        }
    }

    private void saveWeather(){
        String json = gson.toJson(currentWeatherMap, type);
        prefMgr.setString(sharedPref, "initialized", json);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_city){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fl.setVisibility(View.VISIBLE);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.flActionPlaceholder, new AddCityFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fl.setVisibility(View.GONE);
    }
}
