package com.gonchar.project.reminder;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.os.Build.ID;
import static com.gonchar.project.reminder.R.string.MyService_onStartCommand_method_messageText;


public class MyService extends Service {

    ScheduledFuture future;
    ScheduledExecutorService scheduler;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //final Intent secondIntent = intent;
        //final Context context = this;

        // make notification active for user clicks
        final PendingIntent secondPedIntent = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //this block make new notification for user (service is execution)
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_id")
                    .setContentTitle(getString(R.string.app_name))
                    .setAutoCancel(true);
            Notification message = builder.build();
            startForeground(1, message);

            // create new channel
            NotificationChannel channel = new NotificationChannel("channel_id",
                    " delivery reminder", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("notification channel (delivery reminder)");
            channel.enableLights(true);
            channel.enableVibration(false);

        }

        // this block create new message
        final NotificationManager messageManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), ID)
                .setSmallIcon(R.mipmap.ic_error_outline_black_18dp)
                .setContentTitle("Reminder")
                .setContentText(intent.getCharSequenceExtra("message"))
                .setContentIntent(secondPedIntent)
                .setChannelId("channel_id")
                .setAutoCancel(true);

        final Notification message = notification.build();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                assert messageManager != null;
                messageManager.notify(2,message);
            }
        };

        // this block do runnable with some periodical
        scheduler = Executors.newScheduledThreadPool(1);
        future = scheduler.scheduleAtFixedRate(runnable,
                intent.getIntExtra("time", 0),
                intent.getIntExtra("time", 0), TimeUnit.SECONDS);


        showMessage(getText(MyService_onStartCommand_method_messageText));

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        showMessage(getText(R.string.MyService_onDestroy_method_messageText));
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                future.cancel(true);
            }
        }, 0, TimeUnit.SECONDS);
        super.onDestroy();
    }

    /**
     * this method create and show some message for user
     *
     * @param message message text
     */
    private void showMessage(CharSequence message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 50);
        toast.show();
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }
}
