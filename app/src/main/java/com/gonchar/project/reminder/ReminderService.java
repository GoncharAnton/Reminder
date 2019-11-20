package com.gonchar.project.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;


import java.util.*;


import static android.content.Intent.FILL_IN_ACTION;
import static com.gonchar.project.reminder.utils.Constants.*;

import static android.os.Build.ID;

public class ReminderService extends Service {


    private Notification userReminder;
    Timer reminderMessage = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationAboutService();
        userReminder = createReminder(createReminderIntent(), intent);
        initTimer(createTimerTask(),intent);

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
                    assert messageManager != null;
                    messageManager.createNotificationChannel(initNotificationChannel());
                }
                assert messageManager != null;
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
        return PendingIntent.getActivity(getApplicationContext(),
                0, handleClick, FILL_IN_ACTION);
    }


    private void notificationAboutService() {

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), "channel_id");
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setContentTitle(getString(R.string.app_name)).setAutoCancel(true);
        startForeground(1, builder.build());
    }

    /**
     * this method create new notification and return it in onStartCommand method
     *
     * @param secondPedIntent it is pendingIntent value
     * @param intent          it is intent value which has user message
     * @return notification with user value in onStartCommand method
     */
    private Notification createReminder(PendingIntent secondPedIntent, Intent intent) {

        return new NotificationCompat.Builder(getApplicationContext(), ID)
                .setSmallIcon(R.mipmap.ic_error_outline_black_18dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(intent.getCharSequenceExtra(EXTRAS_MESSAGE_KEY))
                .setContentIntent(secondPedIntent)
                .setChannelId("channel_id")
                .setAutoCancel(true).build();
    }


    private NotificationChannel initNotificationChannel() {

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(SERVICE_CHANNEL_ID,
                    SERVICE_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(SERVICE_CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(false);
        }
        return channel;
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
