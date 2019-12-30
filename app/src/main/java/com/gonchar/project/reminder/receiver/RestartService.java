package com.gonchar.project.reminder.receiver;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.gonchar.project.reminder.Activitys.MainActivity;
import com.gonchar.project.reminder.service.ReminderService;
import com.gonchar.project.reminder.utils.PreferencesManager;

import static android.content.ContentValues.TAG;
import static com.gonchar.project.reminder.utils.Constants.*;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(RemoteInput.getResultsFromIntent(intent).getString(REMOTE_KEY).length() >= MIN_MESSAGE_LENGTH) {

            PreferencesManager manager = PreferencesManager.init(context);
            manager.putStringPreferences(SHARED_PREFERENCES_REMINDER_KEY, RemoteInput.getResultsFromIntent(intent).getString(REMOTE_KEY));

            if (ACTION_NAME.equals(intent.getAction())) {
                context.stopService(new Intent(context, ReminderService.class));
                NotificationManager notification = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                notification.cancelAll();

                if(manager.getBooleanPreference(ACTIVITY_STATE)){
                    context.startActivity(new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intentCreator(context, manager));
                } else {
                    context.startService(intentCreator(context, manager));
                }
            }
        }else{
            Log.e(TAG, "error: massage short them min message length! (min message length - 4 symbols) ");
        }
    }

    /**
     * this method create and return new intent. user for restart service
     * @param context app context
     * @param manager PreferencesManager object (here is saving all users settings)
     * @return new Intent object
     */
    private Intent intentCreator(Context context, PreferencesManager manager ){

        return new Intent(context, ReminderService.class)
                .putExtra(EXTRAS_MESSAGE_KEY, manager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY))
                .putExtra(EXTRAS_TIME_VALUE_KEY, manager.getStringPreference(SHARED_PREFERENCES_TIME_VALUE_KEY));
    }
}
