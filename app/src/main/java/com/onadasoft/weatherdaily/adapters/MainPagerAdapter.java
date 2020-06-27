package com.onadasoft.weatherdaily.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.onadasoft.weatherdaily.CityFragment;
import com.onadasoft.weatherdaily.GlobalData;
import com.onadasoft.weatherdaily.MainActivity;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.CustomCurrent;
import com.onadasoft.weatherdaily.utils.HelperFunctions;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.onadasoft.weatherdaily.App.getInstance;

public class MainPagerAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = "MainPagerAdapter";
    private GlobalData globalData = getInstance().getGlobalData();


    private int NUM_ITEMS;
    private LinkedHashMap<String, Current> mCurrentWeatherMap;
    private Date lastUpdateTimestamp;

    public MainPagerAdapter(FragmentManager fragmentManager, LinkedHashMap<String, Current> currentWeatherMap, Date lastUpdateTimestamp) {
        super(fragmentManager);

        //if(!currentWeatherMap.isEmpty()){
            mCurrentWeatherMap = currentWeatherMap;
            this.lastUpdateTimestamp = lastUpdateTimestamp;
            NUM_ITEMS = mCurrentWeatherMap.size();
            Log.d("From Adapter const", getCount()+" "+mCurrentWeatherMap.size() );

        //}
    }

    // TODO just notify after changing the global map (not needed???)
    public void setCurrentWeatherMap(LinkedHashMap<String, Current> currentWeatherMap){
        mCurrentWeatherMap = currentWeatherMap;
        notifyDataSetChanged();
        Log.d("From Adapter setter", getCount()+" "+mCurrentWeatherMap.size() );

    }


    @Override
    public Fragment getItem(int position) {
        /*
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
*/
        Current holder = (Current)HelperFunctions.getElementByIndex(mCurrentWeatherMap, position);
        CustomCurrent customCurrent = new CustomCurrent(holder);
        customCurrent.setMainTemp(Math.round(customCurrent.getMainTemp()));
        customCurrent.setMainTempMax(Math.round(customCurrent.getMainTempMax()));
        customCurrent.setMainTempMin(Math.round(customCurrent.getMainTempMin()));
        lastUpdateTimestamp = globalData.getLastUpdateTimestamp();

        return CityFragment.newInstance(position, "Page #" + position, customCurrent, lastUpdateTimestamp);
    }

    @Override
    public int getCount() {
        if (mCurrentWeatherMap!=null) {
            return mCurrentWeatherMap.size();
        }
        return 0;
//        return NUM_ITEMS;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
////        return "Page " + position;
//        return "x";
//    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
