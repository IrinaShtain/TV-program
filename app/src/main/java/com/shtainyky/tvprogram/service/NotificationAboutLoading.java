package com.shtainyky.tvprogram.service;

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

public class NotificationAboutLoading {
    public static void sendNotification(Context context)
    {
        Resources resources = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(resources.getString(R.string.data_are_loaded_for_a_month))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(resources.getString(R.string.data_are_loaded_for_a_month))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(0, notification);
        Log.i("MyIntentService","=======================================================================");

    }
}
