package com.onadasoft.weatherdaily.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.onadasoft.weatherdaily.CityFragment;
import com.onadasoft.weatherdaily.MainActivity;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.CustomCurrent;
import com.onadasoft.weatherdaily.utils.HelperFunctions;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainPagerAdapter extends SmartFragmentStatePagerAdapter {


    private int NUM_ITEMS;
    private LinkedHashMap<String, Current> currentWeatherMap;

    public MainPagerAdapter(FragmentManager fragmentManager, LinkedHashMap<String, Current> currentWeatherMap) {
        super(fragmentManager);
        if(!MainActivity.CWMapEmpty){
            this.currentWeatherMap = currentWeatherMap;
            NUM_ITEMS = this.currentWeatherMap.size();
            Log.d("From Adapter const", getCount()+" "+this.currentWeatherMap.size() );

        }
    }

    public void setCurrentWeatherMap(LinkedHashMap<String, Current> currentWeatherMap){
        this.currentWeatherMap = currentWeatherMap;
        notifyDataSetChanged();
        Log.d("From Adapter setter", getCount()+" "+this.currentWeatherMap.size() );

    }


    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                return CityFragment.newInstance(0, "Page #1", currentWeatherMap.get("Tunis"));
//            case 1:
//                return CityFragment.newInstance(1, "Page #2", currentWeatherMap.get("Paris"));
//            case 2:
//                return CityFragment.newInstance(2, "Page #3", currentWeatherMap.get("Berlin"));
//            default:
//                return null;
//        }
        CustomCurrent customCurrent = new CustomCurrent((Current)HelperFunctions.getElementByIndex(currentWeatherMap, position));

        return CityFragment.newInstance(position, "Page #" + position, customCurrent);
    }

    @Override
    public int getCount() {     
        //if (currentWeatherMap!=null) {
            return currentWeatherMap.size();
        //}
        //return 0;
//        return NUM_ITEMS;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
////        return "Page " + position;
//        return "x";
//    }

}
