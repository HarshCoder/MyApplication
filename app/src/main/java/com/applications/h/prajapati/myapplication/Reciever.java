package com.applications.h.prajapati.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vishwa on 5/16/2016.
 */
public class Reciever extends BroadcastReceiver {
    private NotificationManager manager;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("SomeService", "Receiving Broadcast, starting service");
        Intent intents = new Intent(context,MainActivity.class);
        intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intents,PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("My Notification")
                .setContentText("Hello World")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        manager.notify(0,builder.build());
    }
}
