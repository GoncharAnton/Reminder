package com.gonchar.project.reminder.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

import static android.content.Context.ACTIVITY_SERVICE;

public class Tools {

    public static Boolean isEmptyMessage(String message){
        return message.isEmpty();
    }


    public static boolean checkServiceRunning(String ServiceName, Context mainActivity){

        ActivityManager manager = (ActivityManager) mainActivity.getSystemService(ACTIVITY_SERVICE );
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
