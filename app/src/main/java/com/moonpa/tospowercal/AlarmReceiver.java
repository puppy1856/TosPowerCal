package com.moonpa.tospowercal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver
{
    int id;
    private String time,power,msg;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        time = bundle.getString("time");
        power = bundle.getString("power");
        id = bundle.getInt("count");
        msg = "體力:" + power + " 在" + time + "時到達摟!";

        PowerCalNotification alarm = new PowerCalNotification(id,context);

        alarm.alarmSet(msg);
    }
}
