package com.gonchar.project.reminder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.gonchar.project.reminder.MainActivity;
import com.gonchar.project.reminder.R;
import com.gonchar.project.reminder.receiver.RestartService;
import com.gonchar.project.reminder.utils.PreferencesManager;

import java.util.*;

import static android.content.Intent.ACTION_ANSWER;
import static android.content.Intent.FILL_IN_ACTION;
import static com.gonchar.project.reminder.utils.Constants.*;

public class ReminderService extends Service {


    Notification userReminder;
    Timer reminderMessage = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationAboutService();
        userReminder = createReminder(createReminderIntent());
        initTimer(createTimerTask(), intent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void initTimer(TimerTask timerTask, Intent intent) {

        reminderMessage.schedule(timerTask, COEFFICIENT_FOR_CONVERT_MS_IN_SEC
                        * intent.getIntExtra(EXTRAS_TIME_VALUE_KEY, DEFAULT_TIME_VALUE)
                , COEFFICIENT_FOR_CONVERT_MS_IN_SEC * intent.getIntExtra(EXTRAS_TIME_VALUE_KEY, DEFAULT_TIME_VALUE));
    }


    private TimerTask createTimerTask() {

        return new TimerTask() {
            @Override
            public void run() {

                NotificationManager messageManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    messageManager.createNotificationChannel(initNotificationChannel());
                }
                messageManager.notify(2, userReminder);

            }
        };
    }


    /**
     * this method create new pending intent (used for created notification (reminder message))
     *
     * @return new PendingIntent object
     */
    private PendingIntent createReminderIntent() {

        Intent handleClick = new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        handleClick.putExtra(QUICK_NOTIFICATION_CHANGE, 123);
        return PendingIntent.getActivity(getApplicationContext(),
                123, handleClick, FILL_IN_ACTION);
    }


    /**
     * this method create new notification about running service
     */
    private void notificationAboutService() {

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), initNotificationChannel().getId());
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true);

        startForeground(1, builder.build());
        Log.d("+++", "foreground is started");
    }

    /**
     * this method create new notification and return it in onStartCommand method
     *
     * @param secondPedIntent it is pendingIntent value
     * @return notification with user value in onStartCommand method
     */
    private Notification createReminder(PendingIntent secondPedIntent) {

        PreferencesManager settingManager = PreferencesManager.init(getApplicationContext());
        NotificationCompat.Builder reminder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
            reminder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId())
                    .addAction(createAction());
        }else{
            //noinspection deprecation
            reminder = new NotificationCompat.Builder(getApplicationContext());
        }


        return reminder
                .setSmallIcon(R.mipmap.ic_error_outline_black_18dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(settingManager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY))
                .setContentIntent(secondPedIntent)
                .build();

    }

    /**
     * this method used in createReminder method. Create new action ( create for change reminder ) consist of the action button
     * and text field for input new reminder
     *
     * @return new action class object
     */
    private NotificationCompat.Action createAction() {

        Intent restartServiceIntent = new Intent(getApplicationContext(), RestartService.class);
        restartServiceIntent.setAction(ACTION_NAME);
        restartServiceIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
        PendingIntent restartServicePendingIntent = PendingIntent.getBroadcast(this, 0, restartServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationCompat.Action.Builder(R.mipmap.ic_error_outline_black_18dp, "changes", restartServicePendingIntent)
                .addRemoteInput(createRemoteInput()).build();
    }

    /**
     * this method used in new action, which create for change user notification. Create text field for change reminder notification
     *
     * @return remote input object
     */
    private RemoteInput createRemoteInput() {
        return new RemoteInput.Builder(REMOTE_KEY)
                .setLabel("new notification")
                .build();
    }

    /**
     * this method create new notification chanel for notification about running service
     *
     * @return new notification channel
     */
    private NotificationChannel initNotificationChannel() {
        NotificationChannel channel ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(SERVICE_CHANNEL_ID,
                    SERVICE_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            channel.setDescription(SERVICE_CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(false);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);                                                          // fix it (write check for NulPointerException)
            return channel;
        }
        return null;
    }

    /**
     * this method stop this service
     */
    @Override
    public void onDestroy() {
        reminderMessage.cancel();
        super.onDestroy();
    }
}
