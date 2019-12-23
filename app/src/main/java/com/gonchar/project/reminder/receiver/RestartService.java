package com.gonchar.project.reminder.receiver;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.gonchar.project.reminder.MainActivity;
import com.gonchar.project.reminder.service.ReminderService;
import com.gonchar.project.reminder.utils.PreferencesManager;

import static android.content.ContentValues.TAG;
import static com.gonchar.project.reminder.utils.Constants.*;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(RemoteInput.getResultsFromIntent(intent).getString(REMOTE_KEY).length() >= MIN_MESSAGE_LENGTH) {
            PreferencesManager manager = PreferencesManager.init(context);
            manager.putPreferences(SHARED_PREFERENCES_REMINDER_KEY, RemoteInput.getResultsFromIntent(intent).getString(REMOTE_KEY));
            Intent serviceIntent = new Intent(context, ReminderService.class)
                    .putExtra(EXTRAS_MESSAGE_KEY, manager.getPreference(SHARED_PREFERENCES_REMINDER_KEY))
                    .putExtra(EXTRAS_TIME_VALUE_KEY, manager.getPreference(SHARED_PREFERENCES_TIME_VALUE_KEY));


            if (ACTION_NAME.equals(intent.getAction())) {

                context.stopService(new Intent(context, ReminderService.class));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    context.startForegroundService(serviceIntent);
                } else {

                    context.startService(serviceIntent);
                }
            }
        }else{
            Log.e(TAG, "error: massage short them min message length! (min message length - 4 symbols) ");
        }
    }
}
