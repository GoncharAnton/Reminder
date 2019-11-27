package com.gonchar.project.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.*;

import static android.content.Intent.ACTION_ANSWER;
import static android.content.Intent.FILL_IN_ACTION;
import static com.gonchar.project.reminder.utils.Constants.*;

public class ReminderService extends Service {


    private Notification userReminder;
    Timer reminderMessage = new Timer();
    private NotificationChannel channel = null;

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
        handleClick.setAction(ACTION_ANSWER);
        handleClick.putExtra(QUICK_NOTIFICATION_CHANGE, 123);
        return PendingIntent.getActivity(getApplicationContext(),
                123, handleClick, FILL_IN_ACTION);
    }


    private void notificationAboutService() {

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), initNotificationChannel().getId());
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

        Notification.Builder reminder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            reminder = new Notification.Builder(getApplicationContext(), notificationChannel.getId());

            // change notification without create new activity or without opening already created activity
           /* //_____________________________________________________________________________________________

            android.app.RemoteInput userInput = new RemoteInput.Builder(REMOTE_KEY)
                    .setLabel("Change message")
                    .build();

            Notification.Action notificationButton = new Notification.Action.Builder
                    (R.mipmap.ic_error_outline_black_18dp,"Change message",createReminderIntent())
                    .addRemoteInput(userInput)
                    .build();


            reminder = new Notification.Builder(getApplicationContext(), notificationChannel.getId())
            .addAction(notificationButton);


            if (intent.getAction() != null) {
                intent.putExtra(EXTRAS_MESSAGE_KEY,"11111111111111");
                userReminder = createReminder(createReminderIntent(), intent);
                initTimer(createTimerTask(),intent);
                createReminder(secondPedIntent, intent);
                return reminder.setSmallIcon(R.mipmap.ic_error_outline_black_18dp)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(intent.getCharSequenceExtra("11111111111111"))
                        .setContentIntent(secondPedIntent)
                        .build();
            }
            //_____________________________________________________________________________________________*/


        }else{
            reminder = new Notification.Builder(this);

        }
        return reminder
                .setSmallIcon(R.mipmap.ic_error_outline_black_18dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(intent.getCharSequenceExtra(EXTRAS_MESSAGE_KEY))
                .setContentIntent(secondPedIntent)
                .build();


    }












    private NotificationChannel initNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(SERVICE_CHANNEL_ID,
                    SERVICE_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            channel.setDescription(SERVICE_CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(false);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(channel);
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
