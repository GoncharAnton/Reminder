package com.gonchar.project.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gonchar.project.reminder.service.ReminderService;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.ACTION_ANSWER;
import static com.gonchar.project.reminder.utils.Constants.EMPTY_STRING;
import static com.gonchar.project.reminder.utils.Constants.EXTRAS_MESSAGE_KEY;
import static com.gonchar.project.reminder.utils.Constants.EXTRAS_TIME_VALUE_KEY;
import static com.gonchar.project.reminder.utils.Constants.SHARED_PREFERENCES_FILE_NAME;
import static com.gonchar.project.reminder.utils.Constants.SHARED_PREFERENCES_REMINDER_KEY;
import static com.gonchar.project.reminder.utils.Constants.USER_SETTING_TIME_VALUE_KEY;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("+++", "receiver inside!" );

        if("com.gonchar.project.reminder.Action".equals(intent.getAction())){


            Log.d("===", "Action != null!");
            context.stopService(new Intent(context, ReminderService.class));
        }
//        Log.d("+++", intent.getStringExtra("111") == null? "Massage is missing" : intent.getStringExtra("111") );
//        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
//        Intent serviceIntent = new Intent(context, ReminderService.class)
//                .putExtra(EXTRAS_MESSAGE_KEY, settings.getString(SHARED_PREFERENCES_REMINDER_KEY, EMPTY_STRING))
//                .putExtra(EXTRAS_TIME_VALUE_KEY, settings.getString(USER_SETTING_TIME_VALUE_KEY, EMPTY_STRING));

    /*if(intent.getAction().){

        }*/
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
