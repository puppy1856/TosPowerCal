package com.moonpa.tospowercal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.moonpa.mylibrary.PowerCal;

import java.util.List;

public class BootReceiver extends BroadcastReceiver
{
    final String DATABASE_NAME = "alarm.db";
    final int DATABASE_VERISON = 5;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerCal cal = new PowerCal();
        AlarmListSQL alarmM = new AlarmListSQL(context,DATABASE_NAME,null, DATABASE_VERISON);
        List<PowerCalList> powerCalLists = alarmM.getDataList();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int a = 0; a < powerCalLists.size(); a++)
        {
            Intent i = new Intent(context,AlarmReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putString("time", powerCalLists.get(a).getMsg());
            bundle.putString("power", powerCalLists.get(a).getPower() + "");
            bundle.putInt("count",powerCalLists.get(a).getCount());
            i.putExtras(bundle);
            i.addCategory(powerCalLists.get(a).getCount() + "");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

            long time = cal.getTime(powerCalLists.get(a).getTime()).getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP,time,pendingIntent);
        }
    }
}
