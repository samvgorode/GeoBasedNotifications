package com.example.who.geobasednotifications.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.example.who.geobasednotifications.App;
import com.example.who.geobasednotifications.R;
import com.example.who.geobasednotifications.ui.MapsActivity;

public class NotificationHelper {

    public static void sendNotification(String title, String message) {
        Context context = App.getAppContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "circle_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("IN/OUT circle");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
//        Intent intent = new Intent(activity, MapsActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
//                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_cursor)
                .setTicker("")
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("");

        if (notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build());
        }
    }
}
