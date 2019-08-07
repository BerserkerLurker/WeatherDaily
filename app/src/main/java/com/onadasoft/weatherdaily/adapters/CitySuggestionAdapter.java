package com.onadasoft.weatherdaily.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.onadasoft.weatherdaily.CityRepository;
import com.onadasoft.weatherdaily.models.recyclerCities.City;

import java.util.ArrayList;
import java.util.List;

public class CitySuggestionAdapter extends ArrayAdapter {

    private List<City> cityList;
    private Context mContext;
    private int itemLayout;

    private CityRepository cityRepository = new CityRepository();

    private ListFilter listFilter = new ListFilter();

    public CitySuggestionAdapter(@NonNull Context context, int resource, @NonNull List<City> listOfCities) {
        super(context, resource, listOfCities);
        cityList = listOfCities;
        mContext = context;
        itemLayout = resource;

    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Nullable
    @Override
    public City getItem(int position) {
        return cityList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if(view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        }

        TextView cityName = view.findViewById(android.R.id.text1);
        cityName.setText(getItem(position).getName());

//
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    InputMethodManager imm = (InputMethodManager)getContext()
//                            .getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                }
//                return false;
//            }
//        });


        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter{

        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if(prefix == null || prefix.length() == 0){
                synchronized (lock){
                    results.values = new ArrayList<String>();
                    results.count = 0;
                }
            }else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                List<City> matchValues = cityRepository.getCityInfo(mContext, searchStrLowerCase);

                results.values = matchValues;
                results.count = matchValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values != null){
                cityList = (ArrayList<City>)results.values;
            }else {
                cityList = null;
            }
            if(results.count > 0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }
        }
    }
}
