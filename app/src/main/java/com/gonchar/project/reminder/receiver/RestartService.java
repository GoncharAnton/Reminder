package com.gonchar.project.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.gonchar.project.reminder.service.ReminderService;

import static android.content.Context.MODE_PRIVATE;
import static com.gonchar.project.reminder.utils.Constants.ACTION_NAME;
import static com.gonchar.project.reminder.utils.Constants.EMPTY_STRING;
import static com.gonchar.project.reminder.utils.Constants.EXTRAS_MESSAGE_KEY;
import static com.gonchar.project.reminder.utils.Constants.EXTRAS_TIME_VALUE_KEY;
import static com.gonchar.project.reminder.utils.Constants.SHARED_PREFERENCES_FILE_NAME;
import static com.gonchar.project.reminder.utils.Constants.SHARED_PREFERENCES_REMINDER_KEY;
import static com.gonchar.project.reminder.utils.Constants.SHARED_PREFERENCES_TIME_VALUE_KEY;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("+++", "intent " + intent.getAction());

        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        Intent serviceIntent = new Intent(context, ReminderService.class)
                .putExtra(EXTRAS_MESSAGE_KEY, settings.getString(SHARED_PREFERENCES_REMINDER_KEY, EMPTY_STRING))
                .putExtra(EXTRAS_TIME_VALUE_KEY, settings.getString(SHARED_PREFERENCES_TIME_VALUE_KEY, EMPTY_STRING));

        Log.d("Bundle","It is reminder : " + settings.getString(SHARED_PREFERENCES_REMINDER_KEY, EMPTY_STRING));
        Log.d("Bundle","It is time value : " + settings.getString(SHARED_PREFERENCES_TIME_VALUE_KEY, EMPTY_STRING));

        Log.d("Bundle","It is intent reminder : " + serviceIntent.getStringExtra(EXTRAS_MESSAGE_KEY));
        Log.d("Bundle","It is intent time value : " + serviceIntent.getStringExtra(EXTRAS_TIME_VALUE_KEY));

        if(ACTION_NAME.equals(intent.getAction())){

            context.stopService(new Intent(context, ReminderService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            }else{
                context.startService(serviceIntent);
            }
        }
    }
}
