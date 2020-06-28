package com.onadasoft.weatherdaily;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.onadasoft.weatherdaily.adapters.AddCityAdapter;
import com.onadasoft.weatherdaily.adapters.CitySuggestionAdapter;
import com.onadasoft.weatherdaily.models.Current;
import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.roomdb.db.AppDatabase;
import com.onadasoft.weatherdaily.utils.HelperFunctions;
import com.onadasoft.weatherdaily.utils.SwipeController;
import com.onadasoft.weatherdaily.utils.SwipeControllerActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.onadasoft.weatherdaily.App.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCityFragment extends Fragment implements View.OnClickListener {

    private GlobalData globalData = getInstance().getGlobalData();

//    private AppDatabase appDB;

    ImageView backBtn;
    RecyclerView rvCities;
    FloatingActionButton fabAddCity;
    AlertDialog alert;

    AddCityAdapter addCityAdapter;

    public MainActivity mainActivity;

//    private List<City> mCities = new ArrayList<>();
    private LinkedHashMap<String, Current> mCurrentWeatherMap;

    public AddCityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        // ToDo -- iterate hashmap and fill cities list
        mCurrentWeatherMap = globalData.getCurrentWeatherMap();

//        for(Current current:mCurrentWeatherMap.values()){
//            City city = new City(current.getId(), current.getName(), current.getSys().getCountry(), current.getCoord());
//            mCities.add(city);
//        }

//        Log.d("FromAddFrag", "ListCities: "+ mCities.toString());


//        appDB = AppDatabase.getDatabase(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mainActivity = (MainActivity) getActivity();
//        Log.d("FromAddFrag: ", mainActivity.currentWeatherMap.toString());

        // Inflate the layout for this fragment
        getContext().getTheme().applyStyle(R.style.AppTheme_NoActionBar_Night, true);

        View view = inflater.inflate(R.layout.fragment_add_city, container, false);
        backBtn = view.findViewById(R.id.btn_back_add_city);
        backBtn.setOnClickListener(this);

        fabAddCity = view.findViewById(R.id.fabAddCity);
        fabAddCity.setOnClickListener(this);
        fabAddCity.setEnabled(false);
        fabAddCity.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        Thread fabController = new Thread(() -> {
            try {
                //((App)getActivity().getApplication()).getStartLatch().await();
                App.getInstance().getStartLatch().await();
                getActivity().runOnUiThread(() -> {
//                    fabAddCity.setEnabled(true);
//                    fabAddCity.getBackground().setColorFilter(null);
                    toggleAddBtn(mCurrentWeatherMap.size());

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        fabController.start();

        rvCities = view.findViewById(R.id.rvCities);
        addCityAdapter = new AddCityAdapter(mCurrentWeatherMap);//todo
        rvCities.setLayoutManager(new LinearLayoutManager(mainActivity));

        rvCities.setAdapter(addCityAdapter);

        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
//                Log.d("to be rem", " "+mCities.get(position).getName());
//                String toBeDel = mCities.get(position).getName();
                String toBeDel = (String)HelperFunctions.getKeyByIndex(mCurrentWeatherMap, position);
//                mCities.remove(position);
                if(toBeDel.equals(globalData.getLastKnowLocation())){
                    globalData.saveLastLocation("*");
                }
                mCurrentWeatherMap.remove(toBeDel);
                mainActivity.fragmentPagerAdapter.notifyDataSetChanged();

                addCityAdapter.notifyItemRemoved(position);
                addCityAdapter.notifyItemRangeChanged(position, addCityAdapter.getItemCount());

                // Limit 10
                toggleAddBtn(mCurrentWeatherMap.size());
                updateSharedPrefs();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvCities);

        rvCities.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        toggleAddBtn(mCurrentWeatherMap.size());


        return view;
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_add_city){
            //new Thread(() -> appDB.cityDao().nukeCities()).start();
            getActivity().onBackPressed();
        }
        if(view.getId() == R.id.fabAddCity){

            // ------------NewCityInput-------------
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert = builder.create();
            alert.setTitle("Add new city");
            alert.setMessage("Type the city name");
            // Set an EditText view to get user input
            // final EditText input = new EditText(getContext());

            final AutoCompleteTextView input = new AutoCompleteTextView(getContext());
            input.setId(R.id.addCityInputText);

            // Color of InputText content
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getContext().getTheme();
            theme.resolveAttribute(R.attr.themeContentTextColor, typedValue, true);
            @ColorInt int color = typedValue.data;
            input.setTextColor(color);


            input.setHint("ex.: London");
            input.setHintTextColor(Color.GRAY);

            // margin
            input.setSingleLine();
            FrameLayout container = new FrameLayout(getActivity());
            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            input.setLayoutParams(params);
            container.addView(input);

//            Window window = alert.getWindow();
//            WindowManager.LayoutParams wlp = window.getAttributes();
//            wlp.gravity = Gravity.TOP;
//            wlp.y = 24;
//            window.setAttributes(wlp);


/*
                //TEST
            // Get the string array
            String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries);
            input.setAdapter(adapter);
                //TEST
 */
            List<City> cities = new ArrayList<City>();
            CitySuggestionAdapter adapter = new CitySuggestionAdapter(this.getContext(), android.R.layout.simple_list_item_1, cities);
            input.setAdapter(adapter);
            input.setOnItemClickListener(onItemClickListener);


            alert.setView(container);

            /*alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String result = input.getText().toString();

                    // Add City to list
                    // ToDo -- replace with right logic (Dialog box and JSON indexing for available cities)
                    City test = new City(123, result, "uk", new Coord(10.10, 11.11));
                    mCities.add(test);
                    addCityAdapter.notifyDataSetChanged();
                }
            });*/

//
//            input.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    in.hideSoftInputFromWindow(input.getApplicationWindowToken(), 0);
//                }
//            });

            alert.show();
        // ------------NewCityInput-------------


        }
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(AddCityFragment.this.getContext(),"Clicked item from Auto completion list "
                    + parent.getItemAtPosition(position)
                    , Toast.LENGTH_SHORT).show();
//                    mCities.add((City)parent.getItemAtPosition(position));
                    City city = (City)parent.getItemAtPosition(position);
                    Current current = new Current(city.getId(), city.getName(), city.getCoord(), city.getCountry());
                    mCurrentWeatherMap.put(current.getName(), current);
                    alert.dismiss();
                    // Limit 10
                    toggleAddBtn(mCurrentWeatherMap.size());
                    //Test fix list bug 03/08/2019
//                    mainActivity.checkCitiesMatch(mCities, mCurrentWeatherMap);
                    mainActivity.fragmentPagerAdapter.notifyDataSetChanged();

                    // Timestamp reset if city added
                    globalData.setLastUpdateTimestamp(new Date(1));
                    updateSharedPrefs();
                }
            };

    private void updateSharedPrefs() {
        globalData.saveWeather();
//        String citiesJSON = mainActivity.gson.toJson(mCities);
//        mainActivity.prefMgr.setString(mainActivity.sharedPref, "cities", citiesJSON);
    }

    private void toggleAddBtn(int size){
        if(size == 8){
            fabAddCity.setEnabled(false);
            fabAddCity.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }else if(size < 8){
            fabAddCity.setEnabled(true);
            fabAddCity.getBackground().setColorFilter(null);
        }
    }

}
