package com.gonchar.project.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.gonchar.project.reminder.service.ReminderService;

import java.util.Objects;


import static android.content.Context.MODE_PRIVATE;
import static com.gonchar.project.reminder.utils.Constants.*;

public class AutoBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        Intent serviceIntent = new Intent(context, ReminderService.class)
                .putExtra(EXTRAS_MESSAGE_KEY, settings.getString(SHARED_PREFERENCES_REMINDER_KEY, EMPTY_STRING))
                .putExtra(EXTRAS_TIME_VALUE_KEY, settings.getString(USER_SETTING_TIME_VALUE_KEY, EMPTY_STRING));


        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Log.d("myapp", intent.getStringExtra(EXTRAS_MESSAGE_KEY) == null ? "intent is empty" : "intent has some things");

                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);

            }
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
