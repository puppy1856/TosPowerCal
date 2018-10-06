package com.moonpa.tospowercal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class PowerCalNotification
{
    final String SETTING = "setting";
    final String CHANNEL_ID = "TosPowerCal";
    private int icon = R.mipmap.ic_launcher;
    private int id,notify;
    private boolean notifyChannelCheck;
    private Boolean vibrate,voice;
    private NotificationManager noMgr;
    private Notification.Builder builder;
    private Intent I;
    private SharedPreferences setting;
    private PendingIntent appIntent;

    public PowerCalNotification(int id, Context context)
    {
        this.id = id;
        setting = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        notifyChannelCheck = setting.getBoolean("notifyChannelCheck", false);
        vibrate = setting.getBoolean("vibrate",true);
        voice = setting.getBoolean("voice",true);

        noMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        I = new Intent(context,MainActivity.class);
        I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appIntent = PendingIntent.getActivity(context, 0, I, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if (!notifyChannelCheck)
            {
                NotificationChannel mNotificationChannel1 = new NotificationChannel(CHANNEL_ID + 1, "TosPowerCal"
                        , NotificationManager.IMPORTANCE_DEFAULT);
                NotificationChannel mNotificationChannel2 = new NotificationChannel(CHANNEL_ID + 2, "TosPowerCal"
                        , NotificationManager.IMPORTANCE_DEFAULT);
                NotificationChannel mNotificationChannel3 = new NotificationChannel(CHANNEL_ID + 3, "TosPowerCal"
                        , NotificationManager.IMPORTANCE_DEFAULT);
                NotificationChannel mNotificationChannel4 = new NotificationChannel(CHANNEL_ID + 4, "TosPowerCal"
                        , NotificationManager.IMPORTANCE_LOW);

                setChannelDefault(mNotificationChannel1);
                setChannelDefault(mNotificationChannel2);
                setChannelDefault(mNotificationChannel3);
                setChannelDefault(mNotificationChannel4);

                mNotificationChannel3.setSound(null,null);

                mNotificationChannel1.enableVibration(true);
                mNotificationChannel1.setVibrationPattern(new long[]{100, 200, 400, 500, 700, 500, 400, 200, 400});
                mNotificationChannel2.enableVibration(false);
                mNotificationChannel3.enableVibration(true);
                mNotificationChannel3.setVibrationPattern(new long[]{100, 200, 400, 500, 700, 500, 400, 200, 400});
                mNotificationChannel4.enableVibration(false);

                noMgr.createNotificationChannel(mNotificationChannel1);
                noMgr.createNotificationChannel(mNotificationChannel2);
                noMgr.createNotificationChannel(mNotificationChannel3);
                noMgr.createNotificationChannel(mNotificationChannel4);

                setting.edit().putBoolean("notifyChannelCheck", true).commit();
            }

            if (voice == true)
            {
                if (vibrate == true)
                    notify = 1;
                else
                    notify = 2;
            }
            else
            {
                if (vibrate == true)
                    notify = 3;
                else
                    notify = 4;
            }

            switch (notify)
            {
                case 1:
                    builder = new Notification.Builder(context, CHANNEL_ID + 1);
                    break;
                case 2:
                    builder = new Notification.Builder(context, CHANNEL_ID + 2);
                    break;
                case 3:
                    builder = new Notification.Builder(context, CHANNEL_ID + 3);
                    break;
                case 4:
                    builder = new Notification.Builder(context, CHANNEL_ID + 4);
                    break;
            }
        }
        else
            builder = new Notification.Builder(context);
    }

    public void alarmSet(String msg)
    {
        builder.setContentIntent(appIntent).setSmallIcon(icon)
                .setAutoCancel(true).setWhen(System.currentTimeMillis())
                .setContentTitle("神魔計算機提醒").setContentText(msg);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            int set = 0;
            set |= Notification.DEFAULT_LIGHTS;
            if (vibrate)
                set |= Notification.DEFAULT_VIBRATE;
            if (voice)
                set |= Notification.DEFAULT_SOUND;

            builder.setDefaults(set);
        }

        noMgr.notify(id,builder.build());
    }

    private void setChannelDefault(NotificationChannel mNotificationChannel)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            mNotificationChannel.setDescription("For TosPowerCal Notification");
            mNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            mNotificationChannel.enableLights(true);
            mNotificationChannel.setLightColor(Color.GREEN);
        }
    }
}
