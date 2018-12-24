package com.example.usuario.bakery91;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body) {

        Uri sound = Uri.parse("android.resource://" + mCtx.getPackageName() + "/" + R.raw.notificationsound);
        long[] vibrationPattern = {100, 200, 300, 400, 500, 400, 300, 200, 400};
        // Create channel if device has android Oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationManager mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableVibration(true);
            mChannel.setSound(sound,aa);
            mChannel.setVibrationPattern(vibrationPattern);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        int defaults = 0;
        defaults |= Notification.DEFAULT_VIBRATE;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setDefaults(defaults)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSound(sound)
                        .setVibrate(vibrationPattern);

        Intent resultIntent = new Intent(mCtx,OrdersClients.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, mBuilder.build());
    }
}
