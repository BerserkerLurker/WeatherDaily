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
import com.onadasoft.weatherdaily.models.recyclerCities.City;

import java.util.List;

public class AddCityAdapter extends RecyclerView.Adapter<AddCityAdapter.ViewHolder> {
    private List<City> mCities;

    public AddCityAdapter(List<City> cities) {
        mCities = cities;
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

        City city = mCities.get(position);

        TextView cityName = viewHolder.cityName;
        cityName.setText(city.getName());

        //ImageView delBtn = viewHolder.delBtn;
        // ToDo

    }

    @Override
    public int getItemCount() {
        return mCities.size();
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
