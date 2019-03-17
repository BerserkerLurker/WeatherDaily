package com.onadasoft.weatherdaily;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCityFragment extends Fragment implements View.OnClickListener {

    ImageView backBtn;

    public MainActivity mainActivity;

    public AddCityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        Log.d("FromAddFrag: ", mainActivity.currentWeatherMap.toString());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);
        backBtn = view.findViewById(R.id.btn_back_add_city);
        backBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_add_city){

        }
    }
}
