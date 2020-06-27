package com.onadasoft.weatherdaily.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onadasoft.weatherdaily.R;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.utils.HelperFunctions;

import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;

public class AddCityAdapter extends RecyclerView.Adapter<AddCityAdapter.ViewHolder> {
    private LinkedHashMap<String, Current> mCurrentWeatherMap;

    public AddCityAdapter(LinkedHashMap<String, Current> currentWeatherMap) {
        mCurrentWeatherMap = currentWeatherMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cityView = inflater.inflate(R.layout.item_city_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(cityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        Current current = (Current)HelperFunctions.getElementByIndex(mCurrentWeatherMap, position);

        City city = new City(current.getId(), current.getName(), current.getSys().getCountry(), current.getCoord());

        TextView cityName = viewHolder.cityName;
        cityName.setText(city.getName());

        //ImageView delBtn = viewHolder.delBtn;
        // ToDo

    }

    @Override
    public int getItemCount() {
        return mCurrentWeatherMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView cityName;
        //ImageView delBtn;

        ViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.tvCityName);
            //delBtn = itemView.findViewById(R.id.btnDeleteCity);
        }
    }
}
