package com.gonchar.project.reminder.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.gonchar.project.reminder.utils.Constants.EMPTY_STRING;
import static com.gonchar.project.reminder.utils.Constants.SHARED_PREFERENCES_FILE_NAME;

public class PreferencesManager extends Application {

    public static Context context;
    SharedPreferences userVariable ;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    /**
     * this method save user settings (text from text fields in reminderMassage and timeValue)
     *
     * @param key  which shared preferences used for save user settings
     * @param preference last variant of user string
     */
    public void putPreferences(String key, String preference){
        SharedPreferences.Editor editor = userVariable.edit();
        editor.putString(key, preference);
        editor.apply();
    }

    /**
     *  this method check some user key(is the sharedPreferences contains some things from pair with this key)
     * @param key for search preference
     * @return return true is  preference is finding, else return false
     */
    public boolean contains(String key){
        userVariable = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,MODE_PRIVATE);
        return userVariable.contains(key);
    }

    /**
     * this method find and return user preferences if  sharedPreferences contains value? else return null
     * @param key for search preference
     * @return return string value if sharedPreferences contains some things from pair with this key, else - null
     */
    public String getPreference(String key){
        return userVariable.getString(key, EMPTY_STRING);
    }



}
