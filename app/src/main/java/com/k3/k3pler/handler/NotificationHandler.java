package com.k3.k3pler.handler;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.k3.k3pler.R;

/** Handle notifications **/
public class NotificationHandler {
    private Context context;
    private Class mClass;
    private int ID, btnID1 = 1, btnID2 = 2;
    private NotificationManager notificationManager;
    private int pendingFlag = PendingIntent.FLAG_MUTABLE;

    public NotificationHandler(int ID, Context context, Class mClass){
        this.ID = ID;
        this.context = context;
        this.mClass = mClass;
    }
    public Intent getIntent(){
        Intent intent = new Intent(context, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
    public PendingIntent getPendingIntent(Intent intent){
        return PendingIntent.getService(context, 0, intent,
                pendingFlag);
    }
    private NotificationManager createNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(ID),
                    context.getString(R.string.app_name) + String.valueOf(ID),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.app_name) + String.valueOf(ID));
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            return notificationManager;
        }else{
            return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
    public NotificationManager getNotificationManager(){
        return notificationManager;
    }
    private PendingIntent getStopPendingIntent(){
        Intent intent = getIntent();
        intent.putExtra(context.getString(R.string.proxy_stop), true);
        return PendingIntent.getService(context, btnID1, intent,
                pendingFlag);
    }
    private PendingIntent getShowPendingIntent(){
        Intent intent = getIntent();
        intent.putExtra(context.getString(R.string.show_gui), true);
        return PendingIntent.getService(context, btnID2, intent,
                pendingFlag);
    }
    public void notify(String messageTitle, String messageBody, Boolean isOnGoing) {
        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(context, String.valueOf(ID))
                    .setSmallIcon(R.mipmap.k3pler_logo)
                    .addAction(0, context.getString(R.string.proxy_stop), getStopPendingIntent())
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(false)
                    .setOngoing(isOnGoing)
                    .setContentIntent(getShowPendingIntent());
        }
        notificationManager = createNotificationManager();
        if (notificationBuilder != null) {
            notificationManager.notify(ID, notificationBuilder.build());
        }
    }

}
