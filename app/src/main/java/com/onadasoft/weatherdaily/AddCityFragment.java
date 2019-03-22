package com.onadasoft.weatherdaily;


import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onadasoft.weatherdaily.adapters.AddCityAdapter;
import com.onadasoft.weatherdaily.models.Coord;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.utils.SwipeController;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCityFragment extends Fragment implements View.OnClickListener {

    ImageView backBtn;
    RecyclerView rvCities;
    FloatingActionButton fabAddCity;

    AddCityAdapter addCityAdapter;

    public MainActivity mainActivity;

    private List<City> mCities = new ArrayList<>();

    public AddCityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        // ToDo -- iterate hashmap and fill cities list

        for(Current current:mainActivity.currentWeatherMap.values()){
            City city = new City(current.getId(), current.getName(), current.getSys().getCountry(), current.getCoord());
            mCities.add(city);
        }

        Log.d("FromAddFrag", "ListCities: "+ mCities.toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mainActivity = (MainActivity) getActivity();
//        Log.d("FromAddFrag: ", mainActivity.currentWeatherMap.toString());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);
        backBtn = view.findViewById(R.id.btn_back_add_city);
        backBtn.setOnClickListener(this);

        fabAddCity = view.findViewById(R.id.fabAddCity);
        fabAddCity.setOnClickListener(this);

        rvCities = view.findViewById(R.id.rvCities);
        addCityAdapter = new AddCityAdapter(mCities);
        rvCities.setLayoutManager(new LinearLayoutManager(mainActivity));

        rvCities.setAdapter(addCityAdapter);

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvCities);

        rvCities.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_add_city){

        }
        if(view.getId() == R.id.fabAddCity){
            // ToDo -- replace with right logic (Dialog box and JSON indexing for available cities)
            City test = new City(123, "London", "uk", new Coord(10.10, 11.11));
            mCities.add(test);
            addCityAdapter.notifyDataSetChanged();
        }
    }
}
