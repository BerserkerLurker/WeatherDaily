package com.onadasoft.weatherdaily;

import android.content.Context;
import android.os.AsyncTask;

import com.onadasoft.weatherdaily.models.recyclerCities.City;
import com.onadasoft.weatherdaily.roomdb.dao.CityDao;
import com.onadasoft.weatherdaily.roomdb.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class CityRepository {

    private CityDao mCityDao;

    private List<City> searchResults;

    // public  CityRepository(Application application){
    public  CityRepository(){
        // AppDatabase db = AppDatabase.getDatabase(application);
        AppDatabase db = AppDatabase.getDatabase(App.getInstance());
        mCityDao = db.cityDao();
    }

    private void asyncFinished(List<City> results){
        searchResults  = new ArrayList<City>(results);
    }

    public void getCities(String name, Context context){
        ReadAsyncTask task = new ReadAsyncTask(mCityDao);
        task.delegate = this;
        task.mListener = (AsyncResponseListener)context;
        task.execute(name);
//        return task;
    }

    // For Adapter??
    public List<City> getCityInfo(Context context, String cityStr){
        return mCityDao.findByName(cityStr);
    }


    public List<City> getSearchResults(){
        return searchResults;
    }



    public interface AsyncResponseListener{
        void responseCallback(List<City> results);
    }

    public static class ReadAsyncTask extends AsyncTask<String, Void, List<City>>{

        private CityDao mAsyncTaskDao;
        private CityRepository delegate = null;

        private AsyncResponseListener mListener = null;

        ReadAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<City> doInBackground(String... strings) {
            return mAsyncTaskDao.findByName(strings[0]);
        }


        @Override
        protected void onPostExecute(List<City> result) {
            delegate.asyncFinished(result);
            // delegate.processFinish(result);
            mListener.responseCallback(result);
        }
    }

}
