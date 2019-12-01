package com.gonchar.project.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import static com.gonchar.project.reminder.utils.Constants.*;

public class AutoBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context,ReminderService.class)
                .putExtra(EXTRAS_MESSAGE_KEY,intent.getCharSequenceExtra(EXTRAS_MESSAGE_KEY))
                .putExtra(EXTRAS_TIME_VALUE_KEY, intent.getCharSequenceExtra(EXTRAS_TIME_VALUE_KEY));
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            }else{
                context.startService(serviceIntent);
            }
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

}
