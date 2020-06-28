package com.onadasoft.weatherdaily;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DebugUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.onadasoft.weatherdaily.adapters.MainPagerAdapter;
import com.onadasoft.weatherdaily.event.DataSyncEvent;
import com.onadasoft.weatherdaily.models.Coord;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.service.CurrentLocationService;
import com.onadasoft.weatherdaily.service.CurrentWeatherService;
import com.onadasoft.weatherdaily.utils.HelperFunctions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

import static com.onadasoft.weatherdaily.App.getInstance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GlobalData globalData = getInstance().getGlobalData();

    private ImageView btnCity;
    private ImageView btnPlace;
    private ImageView btnSettings;
    private FrameLayout fl;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ConstraintLayout toolbarLayout;
    private SwipeRefreshLayout mSwipeRefresh;

    private ProgressBar locationProgressBar;

    private DialogFragment dialogFragment;

    MainPagerAdapter fragmentPagerAdapter;

    LinkedHashMap<String, Current> currentWeatherMap;

    private static final int LOCATION_PERMISSION_CODE = 1;
    private static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentWeatherMap = globalData.getCurrentWeatherMap();


        if(currentWeatherMap.isEmpty()){
//            CWMapEmpty = true;
            globalData.setCWMAP_EMPTY(true);
        }


        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mSwipeRefresh = findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(refreshListener);

        toolbarLayout = findViewById(R.id.toolbarLayout);
        //toolbarLayout.setBackgroundColor(Color.RED);

        viewPager = findViewById(R.id.vpPager);

        //if(currentWeatherMap!=null) {
            fragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), currentWeatherMap, globalData.getLastUpdateTimestamp());

            viewPager.setAdapter(fragmentPagerAdapter);

            tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager, true);
       // }

        fl = findViewById(R.id.flActionPlaceholder);

        btnCity = findViewById(R.id.btn_city);
        btnCity.setOnClickListener(this);

        btnPlace = findViewById(R.id.btn_place);
        btnPlace.setOnClickListener(this);

        locationProgressBar = findViewById(R.id.progress_bar);
        locationProgressBar.setVisibility(View.GONE);

        btnSettings = findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(this);

        if(currentWeatherMap.size() > 0)
            getWeather();
    }


    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {
            if(currentWeatherMap.size() > 0)
                getWeather();
        }
    };

    private boolean getWeather(){
        Log.d(TAG, "getWeather: " + "called");
        //    DateFormat fmt = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault());

        Date timestamp = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        //Date timestamp = globalData.getLastUpdateTimestamp();
        Date now = new Date(System.currentTimeMillis());


//        Log.d(TAG, "getWeather: " + TimeUnit.MILLISECONDS.toMinutes((now.getTime() - timestamp.getTime())));
//        Log.d(TAG, "getWeather: " + (now.getTime() - timestamp.getTime()));
//        Log.d(TAG, "getWeather: now " + now.getTime());
//        Log.d(TAG, "getWeather: last " + timestamp.getTime());
//        Log.d(TAG, "getWeather: 30min " + TimeUnit.MINUTES.toMillis(20));



        if (now.getTime() - timestamp.getTime() > TimeUnit.MINUTES.toMillis(20)) {
            Intent intent = new Intent(this, CurrentWeatherService.class);
            long[] ids = new long[currentWeatherMap.size()];
            int i = 0;
            for (String city : currentWeatherMap.keySet()) {

                ids[i] = currentWeatherMap.get(city).getId();
                if(ids[i] == -1){
                    intent.putExtra("lat", String.valueOf(currentWeatherMap.get(city).getCoord().getLat()));
                    intent.putExtra("lon", String.valueOf(currentWeatherMap.get(city).getCoord().getLon()));
                }
                i++;
            }
            intent.putExtra("ids", ids);
            startService(intent);
            return true;
        }else {
            Log.d(TAG, "getWeather: Up to date");
            mSwipeRefresh.setRefreshing(false);
            return false;
        }
    }




    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_city){
            mSwipeRefresh.setRefreshing(false);
            mSwipeRefresh.setEnabled(false);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fl.setVisibility(View.VISIBLE);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.flActionPlaceholder, new AddCityFragment(), "addCityFragment");
            ft.addToBackStack(null);
            ft.commit();
        } if(view.getId() == R.id.btn_place){
            /*
            Intent intent = new Intent(this, CurrentWeatherService.class);
            //intent.putExtra("name", "Tunis");
            long[] ids = {524901, 703448, 2643743};
            intent.putExtra("ids", ids);
            startService(intent);

             */
            requestLocation();

        } if(view.getId() == R.id.btn_settings){


            dialogFragment = new NotConnectedDialogFragment();

            dialogFragment.show(getFragmentManager(), "tag");
//            mSwipeRefresh.setRefreshing(false);
//            mSwipeRefresh.setEnabled(false);

            /*
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("addCityFragment");
            if (fragment != null){
                Log.d(TAG, "onClick: " + fragment.toString());
            }

            if(fragment != null){
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                Toast.makeText(this, "frag deleted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "frag doesn't exist", Toast.LENGTH_SHORT).show();
            }
            */
        }
    }

    private void requestLocation() {
        int fineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((fineLocationPermission != PackageManager.PERMISSION_GRANTED) && (coarseLocationPermission != PackageManager.PERMISSION_GRANTED)){
            requestPermission();
        }else {
            detectLocation();
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                    .setTitle("Permission needed")
                    .setMessage("Location permissions are needed to determine your current city")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    PERMISSIONS, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(findViewById(android.R.id.content), "Location permissions have been granted", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(findViewById(android.R.id.content), "Location permissions not granted!", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void detectLocation() {
        boolean isGPSEnabled = locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)
                && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)
                && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(TAG, "detectLocation: " + isGPSEnabled + isNetworkEnabled);

        if (isNetworkEnabled){
            networkRequestLocation();
            locationProgressBar.setVisibility(View.VISIBLE);
        }else {
            if(isGPSEnabled){
                gpsRequestLocation();
                locationProgressBar.setVisibility(View.VISIBLE);
            }else {
                AlertDialog.Builder settingsAlert = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialogTheme);
                settingsAlert.setTitle("Location settings");
                settingsAlert.setMessage("Location is not enabled. Open settings menu?");

                settingsAlert.setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goToSettings = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(goToSettings);
                            }
                        });

                settingsAlert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                settingsAlert.show();
                locationProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void gpsRequestLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            HandlerThread locationThread = new HandlerThread("LocationHandlerThread");
            locationThread.start();
            Looper locationLooper = locationThread.getLooper();

            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, locationLooper);

            Handler locationHandler = new Handler(locationLooper);
            locationHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            locationManager.removeUpdates(locationListener);
                            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(lastLocation != null){
                                    locationListener.onLocationChanged(lastLocation);
                                }else{
                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                                }
                            }
                        }
                    }, 3000L);
            Log.d(TAG, "gpsRequestLocation: " + locationHandler.getLooper().getThread().getName());
        };
    }

    private void networkRequestLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            HandlerThread locationThread = new HandlerThread("LocationHandlerThread");
            locationThread.start();
            Looper locationLooper = locationThread.getLooper();

            // ToDo -- keep this and kill handler in location listener in case of success, how to know if location is new
            //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, locationLooper);

            Handler locationHandler = new Handler(locationLooper);
            locationHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            locationManager.removeUpdates(locationListener);
                            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                                Location lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                Location lastGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if((lastGpsLocation == null) && (lastNetworkLocation != null)){
                                    locationListener.onLocationChanged(lastNetworkLocation);
                                }else if((lastGpsLocation != null) && (lastNetworkLocation == null)){
                                    locationListener.onLocationChanged(lastGpsLocation);
                                }
                                else{
                                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                                }
                            }
                        }
                    }, 3000L);
            Log.d(TAG, "gpsRequestLocation: " + locationHandler.getLooper().getThread().getName());

        };
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationProgressBar.setVisibility(View.GONE);
                }
            });
            String lat = String.format("%1$.2f", location.getLatitude());
            String lon = String.format("%1$.2f", location.getLongitude());

            String latitude = lat.replace(",", ".");
            String longitude = lon.replace(",", ".");

            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
            }

            boolean networkAvailable = HelperFunctions.isNetworkAvailable(MainActivity.this);

            String tempCity = globalData.getLastKnowLocation();
            Log.d(TAG, "temp" + tempCity);

            Current current = new Current(-1, tempCity,
                    new Coord(Double.parseDouble(longitude), Double.parseDouble(latitude)), "*");
            if(tempCity.equals("*")){
                globalData.getCurrentWeatherMap().put("*", current);
            }else {
                globalData.getCurrentWeatherMap().get(tempCity).setCoord(new Coord(Double.parseDouble(longitude), Double.parseDouble(latitude)));
            }


            globalData.saveWeather();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragmentPagerAdapter.notifyDataSetChanged();
                }
            });
            if(networkAvailable) {
                // Add -1 id to map global
                // Check for the special id when retrieving data
                // If so get weather by latlong not by id
                // don't display -1 id fragment until data is updated and id changed
                // Current location entry in map needs special identifier to overwrite
                if(currentWeatherMap.size() > 0) {
                    globalData.setLastUpdateTimestamp(new Date(1));
                    getWeather();
                }

            }else {
                promptNotConnectedDialog();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 0){
//            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            if (!wifiManager.isWifiEnabled()){
//                Toast.makeText(this, "Wifi still disabled", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onActivityResult: Wifi off");
//            } else {
//                Toast.makeText(this, "Wifi enabled", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onActivityResult: Wifi on");
//
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fl.setVisibility(View.GONE);
        //fl.getContext().getTheme().applyStyle(R.style.AppTheme_NoActionBar_Sunny, true);
        viewPager.getContext().getTheme().applyStyle(R.style.AppTheme_NoActionBar_Sunny, true);

        if(currentWeatherMap.size() > 0)
            getWeather();

        Log.d("TAG", " "+ currentWeatherMap.toString());

        mSwipeRefresh.setEnabled(true);

    }



    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThisOnEvent(DataSyncEvent dataSyncEvent){

        Log.d(TAG, "doThisOnEvent: " + dataSyncEvent.getSyncStatusCode() + " " + dataSyncEvent.getSyncStatusMsg());

        String toastMsg;
        int statusCode = dataSyncEvent.getSyncStatusCode();
        switch ((int) statusCode / 100){
            case 2: toastMsg = "Success";fragmentPagerAdapter.notifyDataSetChanged();break;
            case 3: toastMsg = "Redirecting";break;
            case 4: toastMsg = "Client Error";break;
            case 5: toastMsg = "Server Error";break;
            default: toastMsg = "Something went wrong";promptNotConnectedDialog();
        }

        if (statusCode == 401){
            toastMsg += " : Invalid API key";
        }else if (statusCode == 404){
            toastMsg += " : City not found";
        }else if (statusCode == 429){
            toastMsg += " : API key blocked";
        }else if (statusCode == 500){
            toastMsg = "Internal Server Error";
        }

        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
        mSwipeRefresh.setRefreshing(false);

        //fragmentPagerAdapter.notifyDataSetChanged();
    }

    void promptNotConnectedDialog(){
        dialogFragment = new NotConnectedDialogFragment();

        dialogFragment.show(getFragmentManager(), "tag");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(connectivityBroadcastReceiver);
    }
}
