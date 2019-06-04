package com.example.lavisha.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //get id and message
        int notificationid=intent.getIntExtra("notificationid",0);
        String message=intent.getStringExtra("todo");

        //
        Intent mainIntent =new Intent(context,MainActivity.class);
        PendingIntent contentIntent= PendingIntent.getActivity(context,0,mainIntent,0);


        NotificationManager myNotificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification.Builder builder=new Notification.Builder(context);

        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("its time")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);
        myNotificationManager.notify(notificationid,builder.build());


    }



}


