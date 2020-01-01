package com.gonchar.project.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.gonchar.project.reminder.service.ReminderService;
import com.gonchar.project.reminder.utils.PreferencesManager;

import java.util.Objects;

import static com.gonchar.project.reminder.utils.Constants.*;

public class AutoBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PreferencesManager manager = PreferencesManager.init(context);

        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intentCreator(context, manager));
            } else {
                context.startService(intentCreator(context, manager));
            }
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * this method create and return new intent. user for restart service
     *
     * @param context app context
     * @param manager PreferencesManager object (here is saving all users settings)
     * @return new Intent object
     */
    private Intent intentCreator(Context context, PreferencesManager manager) {

        return new Intent(context, ReminderService.class)
                .putExtra(EXTRAS_MESSAGE_KEY, manager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY))
                .putExtra(EXTRAS_TIME_VALUE_KEY, manager.getStringPreference(SHARED_PREFERENCES_TIME_VALUE_KEY));
    }
}
