package com.onadasoft.weatherdaily.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private Context mContext;

    public PreferenceManager(Context context){
        mContext = context.getApplicationContext();
    }

    /**
     * Obtains a specified preference by passing a key.
     * @param key
     * @return The specified preference
     */
    public SharedPreferences getSharedPreferences(String key){
        return mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    /**
     *
     * @param prefs
     * @param key
     * @param value
     */
    public void setString(SharedPreferences prefs, String key, String value){
        prefs.edit().putString(key, value).apply();
    }

    /**
     *
     * @param prefs
     * @param key
     * @param defaultValue
     * @return Returns the string paired with the key, or the defaultValue if nothing is returned.
     */
    public String getString(SharedPreferences prefs, String key, String defaultValue){
        return prefs.getString(key, defaultValue);
    }

    /**
     *
     * @param prefs
     * @param key
     */
    public void remove(SharedPreferences prefs, String key){
        prefs.edit().remove(key).apply();
    }

    /**
     *
     * @param prefs
     * @return
     */
    public boolean clear(SharedPreferences prefs){
        return prefs.edit().clear().commit();
    }
}
