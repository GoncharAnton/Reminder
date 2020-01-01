package com.gonchar.project.reminder.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;


import static android.content.ContentValues.TAG;
import static android.content.Context.ACTIVITY_SERVICE;


public class Tools {

    /**
     * this method check string variable
     *
     * @param message - variable for check
     * @return true if message is null or is empty
     */
    public static Boolean isEmptyMessage(String message) {
        return message.isEmpty();
    }



    /**
     * This method check is service is work or not
     *
     * @param ServiceName  - name of service class
     * @param mainActivity - context value
     * @return true if service was started
     */
    public static boolean checkServiceRunning(String ServiceName, Context mainActivity) {
        ActivityManager manager = (ActivityManager) mainActivity.getSystemService(ACTIVITY_SERVICE);
        if (manager == null){
            Log.e(TAG, "error: while get manager, manager is null");
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method print error message under reminderMessage field
     *
     * @param errorMessage - string with error message
     * @param InputLayout  - TextInputLayout value (reminderMessage or timeValue field)
     */
    public static void showError(String errorMessage, TextInputLayout InputLayout) {
        InputLayout.setError(errorMessage);
    }

    /**
     * This method compare two integer value constantValue & usersValue.
     *
     * @param constantValue - usually it is app constant value (min length of the string or min time value)
     * @param usersValue    - usually it is user value in reminder message or time value fields
     * @return true if users value larger or bigger of the constant value (then as constant value it is min value)
     */
    public static boolean shouldShowError(int constantValue, int usersValue) {
        return usersValue < constantValue;
    }


}
