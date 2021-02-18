package com.ncov.coronatracking.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.ncov.coronatracking.R;
import com.ncov.coronatracking.activities.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

class ForegroundNotification {

    static void showNotification(Service context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Object systemService = context.getSystemService(NOTIFICATION_SERVICE);
        if (systemService != null) {
            NotificationManager notificationManager = (NotificationManager) systemService;
            NotificationChannel notificationChannel = new NotificationChannel("10001", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(),"10001");
            builder.setSmallIcon(R.drawable.ic_virus).setAutoCancel(false).setOngoing(true).setChannelId("10001").setPriority(Notification.PRIORITY_MIN).setContentIntent(activity);
            builder.setContentText("nCov read time... ");
            context.startForeground(1,builder.build());
        }
    }

    static void removeNotification(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        Object systemService = context.getSystemService(NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) systemService;
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
    }

}
