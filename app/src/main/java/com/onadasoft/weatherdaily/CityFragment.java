package com.onadasoft.weatherdaily;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.CustomCurrent;
import com.onadasoft.weatherdaily.utils.HelperFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    private String title;
    private int page;
    private CustomCurrent ccWeather;
    private Date lastUpdate;

    // View components
    private TextView tvLastUpdated;
    private TextView tvCityCountry;
    private TextView tvWeatherIcon;
    private TextView tvTemp;
    private TextView tvTempMax;
    private TextView tvTempMin;
    private TextView tvWeatherDesc;
    private TextView tvWind;
    private TextView tvHumidity;
    private Typeface weatherFont;

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(int page, String title, CustomCurrent currentWeather, Date lastUpdate){
        CityFragment cityFragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putParcelable("currentWeather", currentWeather);
        args.putLong("lastUpdate", lastUpdate.getTime());
        cityFragment.setArguments(args);

        Log.d("Fron frag", currentWeather.getName());
        return cityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
            ccWeather = getArguments().getParcelable("currentWeather");
            lastUpdate = new Date(getArguments().getLong("lastUpdate"));
            Log.d("ccWeather: ", ccWeather.toString());
            weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);
//        TextView tvLabel = view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);


        tvLastUpdated = view.findViewById(R.id.tvLastUpdate);
        tvCityCountry = view.findViewById(R.id.tvCityCountry);
        tvWeatherIcon = view.findViewById(R.id.tvWeatherIcon);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvTempMax = view.findViewById(R.id.tvTempMax);
        tvTempMin = view.findViewById(R.id.tvTempMin);
        tvWeatherDesc = view.findViewById(R.id.tvWeatherDesc);
        tvWind = view.findViewById(R.id.tvWind);
        tvHumidity = view.findViewById(R.id.tvHumidity);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        String dateTime = "Last updated: "+df.format(lastUpdate);
        tvLastUpdated.setText(dateTime);
        if (System.currentTimeMillis() - lastUpdate.getTime() > TimeUnit.MINUTES.toMillis(20)) {
            tvLastUpdated.setTextColor(Color.RED);
        }else {
            tvLastUpdated.setTextColor(Color.GREEN);
        }
        String cityCountry = ccWeather.getName() + ", " + ccWeather.getSysCountry();
        tvCityCountry.setText(cityCountry);
        // ToDo this is not how it's done
        long currentTime = new Date().getTime();
        tvWeatherIcon.setTypeface(weatherFont);
        tvWeatherIcon.setText(HelperFunctions.setWeatherIcon((int)ccWeather.getWeatherId(), currentTime, currentTime));

        //HelperFunctions.getStringRes(R.string.wi_thermometer)
        tvTemp.setTypeface(weatherFont);
        String temp = String.format(Locale.getDefault(), "%s %d%s", HelperFunctions.getStringRes(R.string.wi_thermometer),
                (int) ccWeather.getMainTemp(), HelperFunctions.getStringRes(R.string.wi_celsius));
        tvTemp.setText(temp);
        tvTempMax.setText(String.format(Locale.getDefault(), "%d", (int) ccWeather.getMainTempMax()));
        tvTempMin.setText(String.format(Locale.getDefault(), "%d", (int) ccWeather.getMainTempMin()));
        tvWeatherDesc.setText(ccWeather.getWeatherDescription());
        tvWind.setTypeface(weatherFont);
        tvWind.setText(String.format("%s %s", HelperFunctions.getStringRes(R.string.wi_strong_wind), ccWeather.getWindSpeed()));
        tvHumidity.setTypeface(weatherFont);
        tvHumidity.setText(String.format("%s %s", HelperFunctions.getStringRes(R.string.wi_humidity), ccWeather.getMainHumidity()));

        return view;
    }

}
