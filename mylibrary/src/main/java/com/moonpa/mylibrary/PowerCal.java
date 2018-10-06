package com.moonpa.mylibrary;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PowerCal
{
    /**
     *
     *RECOVER_TIME 神魔之塔恢復體力之週期(分鐘)
     *recoverPower 恢復目標體力之差,初始為-1
     *recoverHours 恢復目標體力之小時差,初始為-1
     *recoverMinutes 恢復目標體力之分鐘差,初始為-1
     */
    private final int RECOVER_TIME = 8;
    private int recoverPower;
    private int recoverHours;
    private int recoverMinutes;
    private String recoverTimeYear;
    private String recoverTimeMonth;
    private String recoverTimeDays;
    private String recoverTimeHours;
    private String recoverTimeMinutes;

    SimpleDateFormat sDF = new SimpleDateFormat("d日 HH:mm", Locale.TAIWAN);
    SimpleDateFormat csDF = new SimpleDateFormat("yyyy/M/d/HH:mm:ss", Locale.TAIWAN);
    SimpleDateFormat YearDF = new SimpleDateFormat("yyyy", Locale.TAIWAN);
    SimpleDateFormat MonthDF = new SimpleDateFormat("M", Locale.TAIWAN);
    SimpleDateFormat DayDF = new SimpleDateFormat("d", Locale.TAIWAN);
    SimpleDateFormat HourDF = new SimpleDateFormat("HH", Locale.TAIWAN);
    SimpleDateFormat MinuteDF = new SimpleDateFormat("mm", Locale.TAIWAN);
    Date time;
    Calendar t = Calendar.getInstance();

    public PowerCal()
    {
        recoverPower = -1;
        recoverHours = -1;
        recoverMinutes = -1;
        recoverTimeYear = "wrong";
        recoverTimeMonth = "wrong";
        recoverTimeDays = "wrong";
        recoverTimeHours = "wrong";
        recoverTimeMinutes = "wrong";
    }
    /**
     *計算當前體力至目標體力需恢復之時間
     *
     *@param maxPower 目標之體力
     *@param nowPower 目前體力
     *@return 到達目標所需之時間
     */
    public String getCalTime(int maxPower,int nowPower)
    {
        if (maxPower > 0 && nowPower >= 0 && (maxPower >= nowPower))
        {
            recoverPower = maxPower - nowPower;
            recoverHours = (recoverPower * RECOVER_TIME) / 60;
            recoverMinutes = (recoverPower * RECOVER_TIME) % 60;

            time = new Date();
            t.setTime(time);
            t.add(t.HOUR, recoverHours);
            t.add(t.MINUTE, recoverMinutes);
            Date endtime = t.getTime();
            return sDF.format(endtime);
        }
        else
            return "wrong";
    }

    public String getRecoverPower()
    {
        return recoverPower != -1 ? (recoverPower + "") : "wrong";
    }

    public String getRecvoerTimeHours()
    {
        return recoverTimeHours;
    }

    public String getRecoverTimeMinutes()
    {
        return recoverTimeMinutes;
    }

    public String getRecoverTimeYear()
    {
        return recoverTimeYear;
    }

    public String getRecoverTimeMonth()
    {
        return recoverTimeMonth;
    }

    public String getRecoverTimeDays()
    {
        return recoverTimeDays;
    }

    /**
     *計算當前時間至目標時間能恢復之體力
     *
     *@param hours 目標之小時
     *@param minutes 目標之分鐘
     *@return 能恢復之體力
     */
    public String getCalPower(int hours,int minutes)
    {
        int recoverAllMinute = -1;
        t.setTime(time);
        int nowHours = t.get(Calendar.HOUR_OF_DAY);
        int nowMinutes = t.get(Calendar.MINUTE);

        if (hours > nowHours || (hours == nowHours && minutes >= nowMinutes))
            recoverAllMinute = (hours - nowHours) * 60 + minutes - nowMinutes;

        else
            recoverAllMinute = ((hours + 24) - nowHours) * 60 + minutes - nowMinutes;

        return recoverAllMinute != -1 ? ((recoverAllMinute / RECOVER_TIME) + "") : "wrong";
    }

    /**
     * @return 現在之時間
     */
    public String getTime()
    {
        time = new Date();
        t.setTime(time);
        Date nt = t.getTime();

        return csDF.format(nt);
    }

    public String getMsgTime()
    {
        time = new Date();
        t.setTime(time);
        Date nt = t.getTime();

        return sDF.format(nt);
    }

    /**
     *
     * @param time 原時間
     * @param plusPower 欲增加之體力
     * @return  體力回復後的加總時間
     * @throws ParseException
     */
    public String getTime(String time, int plusPower)
    {
        String temp = "";
        try
        {
            Calendar cT = Calendar.getInstance();
            Date timeD = csDF.parse(time);
            cT.setTime(timeD);
            cT.add(t.MINUTE, plusPower * RECOVER_TIME);
            timeD = cT.getTime();
            temp = csDF.format(timeD);
            recoverTimeYear = YearDF.format(timeD);
            recoverTimeMonth = MonthDF.format(timeD);
            recoverTimeDays = DayDF.format(timeD);
            recoverTimeHours = HourDF.format(timeD);
            recoverTimeMinutes = MinuteDF.format(timeD);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return temp;
    }

    public String getMsg(String time, int plusPower)
    {
        String temp = "";
        try
        {
            Calendar cT = Calendar.getInstance();
            Date timeD = csDF.parse(time);
            cT.setTime(timeD);
            cT.add(t.MINUTE, plusPower * RECOVER_TIME);
            timeD = cT.getTime();
            temp = sDF.format(timeD);
            recoverTimeYear = YearDF.format(timeD);
            recoverTimeMonth = MonthDF.format(timeD);
            recoverTimeDays = DayDF.format(timeD);
            recoverTimeHours = HourDF.format(timeD);
            recoverTimeMinutes = MinuteDF.format(timeD);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return temp;
    }

    public Calendar getTime(String time)
    {
        Calendar c = Calendar.getInstance();
        try
        {
            Date timeD = csDF.parse(time);
            c.setTime(timeD);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return c;
    }

    /**
     *
     * @param pastTimeS 上次紀錄之時間
     * @param nowPower
     * @param maxPower
     * @return 計算上次時間至現在回復之目前體力
     * @throws ParseException
     */
    public int getReNowPower(String pastTimeS,int nowPower,int maxPower)
    {
        Date nowtimeD,pasttimeD;
        int timeP = 0;

        time = new Date();
        t.setTime(time);
        Date nt = t.getTime();
        String nowtimeS = csDF.format(nt);

        try
        {
            nowtimeD = csDF.parse(nowtimeS);
            pasttimeD = csDF.parse(pastTimeS);
            timeP = (int) (nowtimeD.getTime() - pasttimeD.getTime()) / 1000 / 60;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        int reCoverPower = timeP / RECOVER_TIME;
        int temp = nowPower + reCoverPower;

        return temp >= maxPower ? maxPower : temp;
    }
}
