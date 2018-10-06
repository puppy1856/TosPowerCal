package com.moonpa.tospowercal;

public class PowerCalList
{
    private String time, msg;
    private int count = -1, power;

    public PowerCalList(String time, int power, String msg)
    {
        this.time = time;
        this.power = power;
        this.msg = msg;
    }

    public PowerCalList(int count ,String time, int power , String msg)
    {
        this.count = count;
        this.time = time;
        this.power = power;
        this.msg = msg;
    }

    public int getCount()
    {
        return count;
    }

    public String getTime()
    {
        return time;
    }

    public int getPower()
    {
        return power;
    }

    public String getMsg()
    {
        return msg;
    }
}
