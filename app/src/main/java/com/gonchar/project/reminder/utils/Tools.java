package com.gonchar.project.reminder.utils;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.ActionBar;

import com.gonchar.project.reminder.MainActivity;
import com.gonchar.project.reminder.R;
import com.gonchar.project.reminder.ReminderService;
import com.google.android.material.textfield.TextInputLayout;


import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.gonchar.project.reminder.utils.Constants.SERVICE_CHANNEL_DESCRIPTION;
import static com.gonchar.project.reminder.utils.Constants.SERVICE_CHANNEL_ID;
import static com.gonchar.project.reminder.utils.Constants.SERVICE_CHANNEL_NAME;
import static com.gonchar.project.reminder.utils.Constants.USER_SETINGS_REMINDER_KEY;
import static com.gonchar.project.reminder.utils.Constants.USER_SETING_TIME_VALUE_KEY;

public class Tools {

    /**
     * this method check string variable
     * @param message it is variable for check
     * @return true if message is null or is empty
     */
    public static Boolean isEmptyMessage(String message){
        return message.isEmpty();
    }


    /**
     * this method change default toolbar
     * in this version print label (app name) in the middle of toolbar
     * @param actionBar it is default toolbar
     */
    public static void makeCustomToolBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.toolbar);
    }


    /**
     * This method check is service is work or not
     * @param ServiceName name of service class
     * @param mainActivity context value
     * @return true if service was started
     */
    public static boolean checkServiceRunning(String ServiceName, Context mainActivity){
        ActivityManager manager = (ActivityManager) mainActivity.getSystemService(ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * this method print error message under reminderMessage field
     * @param errorMessage it is string with error message
     * @param InputLayout TextInputLayout value (reminderMessage or timeValue field)
     */
    public static void showError(String errorMessage, TextInputLayout InputLayout) {
        InputLayout.setError(errorMessage);
    }

    /**
     * This method compare two integer value constantValue & usersValue.
     * @param constantValue usually it is app constant value (min length of the string or min time value)
     * @param usersValue usually it is user value in reminder message or time value fields
     * @return true if users value larger or bigger of the constant value (then as constant value it is min value)
     */
    public static boolean shouldShowError(int constantValue, int usersValue) {
        return usersValue < constantValue;
    }



}
