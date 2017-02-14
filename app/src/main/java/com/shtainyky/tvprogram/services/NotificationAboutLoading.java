package com.shtainyky.tvprogram.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.shtainyky.tvprogram.MainActivity;
import com.shtainyky.tvprogram.R;

class NotificationAboutLoading {
    static void sendNotification(Context context, String msg, int id) {
        Resources resources = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(msg)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(msg)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
        Log.i("myLog", "NotificationAboutLoading");

    }
}
