package com.project.todolist.activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.todolist.activities.TaskEditionActivity;

public class MyNotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("itemName");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "667")
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setContentTitle("Délai arrivé !")
                .setContentText("Le délai de votre tâche : "+title+", est arrivé à sa fin.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent2 = new Intent(context, TaskEditionActivity.class);
        PendingIntent pending = PendingIntent.getBroadcast(
                context,
                0,
                intent2,
                0);
        builder.setContentIntent(pending);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(667, builder.build());
    }

}
