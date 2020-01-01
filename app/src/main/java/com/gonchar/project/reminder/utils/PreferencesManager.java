package com.gonchar.project.reminder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static com.gonchar.project.reminder.utils.Constants.*;

public class PreferencesManager {


    private SharedPreferences settings;
    private static PreferencesManager instance;
    private static Context context;

    private PreferencesManager() {
    }

    /**
     * this method check instance of the sharedPreference object (was sharedPreference object creating or not)
     *
     * @param appContext - application context
     * @return preferenceManager object (if object was be creating - return already created object ,
     * else will be create and return new object  )
     */
    public static PreferencesManager init(Context appContext) {
        if (instance == null) {
            Log.d("++", "create new SharedPreferences");
            instance = new PreferencesManager();
        }
        Log.d("++", "here create  sharedPreference!");
        context = appContext;
        return instance;
    }

    /**
     * this method save user settings (text from text fields in reminderMassage and timeValue)
     *
     * @param key        - key which shared preferences used for save user settings
     * @param preference last variant of user string
     */
    public void putStringPreferences(String key, String preference) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, preference);
        editor.apply();
    }

    /**
     * this method save user settings (boolean value which show is activity active or not)
     *
     * @param key        - key which shared preferences used for save user settings
     * @param preference boolean value
     */
    public void putBooleanPreferences(String key, boolean preference) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, preference);
        editor.apply();
    }

    /**
     * this method check some user key(is the sharedPreferences contains some things from pair with this key)
     *
     * @param key - key for search preference
     * @return return true is  preference is finding, else return false
     */
    public boolean contains(String key) {
        settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return settings.contains(key);
    }

    /**
     * this method find and return user preferences if  sharedPreferences contains value? else return null
     *
     * @param key - key for search preference
     * @return return string value if sharedPreferences contains some things from pair with this key, else - null
     */
    public String getStringPreference(String key) {
        if (contains(key)) {
            return settings.getString(key, EMPTY_STRING);
        } else {
            Log.e(TAG, "error: value with key : '" + key + "' not found ");
            return EMPTY_STRING;
        }
    }


    /**
     * this method find and return user preferences if  sharedPreferences contains value? else return null
     *
     * @param key - key for search preference
     * @return return boolean value if sharedPreferences contains some things from pair with this key, else - null
     */
    public boolean getBooleanPreference(String key) {
        if (contains(key)) {
            return settings.getBoolean(key, DEFAULT_BOOLEAN_VALUE);
        } else {
            Log.e(TAG, "error: value with kye : '" + key + "' not found ");
            return false;
        }
    }

}
