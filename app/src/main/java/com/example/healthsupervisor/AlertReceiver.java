package com.example.healthsupervisor;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Random;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String[] arr = new  String[10];
        arr[0]="Gentle reminder";
        arr[1]="Have you taken your meds?";
        arr[2]="It's Time!";
        arr[3]="He who treats himself has a fool for a patient";
        arr[4]="Adapt the remedy to the disease";
        arr[5]="Be careful about reading health books. You may die of a misprint" ;
        arr[6]="Better ten times ill than one time dead";
        arr[7]="When the heart is at ease, the body is healthy";
        arr[8]="Sometimes the remedy is worse than the disease";
        arr[9]="A man's health can be judged by which he takes two at a time - pills or stair";

        Random r = new Random();
        int x=r.nextInt(9);
        String val = intent.getStringExtra("med");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification( "Take "+val,arr[x]);
        notificationHelper.getManager().notify(1,nb.build());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        nb.setSound(alarmSound);


    }
}
