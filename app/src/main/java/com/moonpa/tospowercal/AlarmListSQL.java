package com.moonpa.tospowercal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moonpa.mylibrary.PowerCal;

import java.util.ArrayList;
import java.util.List;

public class AlarmListSQL extends SQLiteOpenHelper
{
    final String TABLE_NAME = "Alarm";
    final String TABLE_ID = "_id";
    final String TABLE_COUNT = "count";
    final String TABLE_POWER = "power";
    final String TABLE_TIME = "time";
    final String TABLE_MSG = "msg";

    private String databaseName;
    private int version;

    public AlarmListSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.databaseName = name;
        this.version = version;
    }
    /**
     * _id(Integer)
     * pwoer(Integer)
     * time(Varchar)
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + TABLE_ID +" INTEGER PRIMARY KEY NOT NULL , "
                    + TABLE_COUNT + " INTEGER , "
                    + TABLE_POWER + " INTEGER , "
                    + TABLE_TIME + " VARCHAR , "
                    + TABLE_MSG + " VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + TABLE_ID +" INTEGER PRIMARY KEY NOT NULL , "
                + TABLE_COUNT + " INTEGER , "
                + TABLE_POWER + " INTEGER , "
                + TABLE_TIME + " VARCHAR , "
                + TABLE_MSG + " VARCHAR);");
    }

    public boolean isTableExist()
    {
        boolean isTableExist=true;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name="
                + "\'" + TABLE_NAME + "\';", null);

        c.moveToFirst();
        if (c.getInt(0)==0)
            isTableExist=false;

        c.close();

        return isTableExist;
    }

    public void addData(int count, int power, String time, String msg)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_COUNT, count);
        values.put(TABLE_POWER, power);
        values.put(TABLE_TIME, time);
        values.put(TABLE_MSG , msg);

        db.insert(TABLE_NAME, null, values);
    }

    public List<PowerCalList> getDataList()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PowerCalList> powerCalLists = new ArrayList<PowerCalList>();

        refreshData();

        Cursor c = db.query(TABLE_NAME,null,null,null,null,null,null);

        while (c.moveToNext())
        {
            powerCalLists.add(new PowerCalList(c.getInt(1), c.getString(3)
                            , c.getInt(2), c.getString(4)));
        }

        c.close();

        return powerCalLists;
    }

    private void refreshData()
    {
        String temp;
        PowerCal cal = new PowerCal();
        SQLiteDatabase rdb = this.getReadableDatabase();
        SQLiteDatabase wdb = this.getWritableDatabase();

        Cursor c = rdb.query(TABLE_NAME,null,null,null,null,null,null);
        while (c.moveToNext())
        {
            temp = c.getString(3);
            if (cal.getTime(cal.getTime()).getTimeInMillis() >=
                    cal.getTime(temp).getTimeInMillis())
                wdb.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_TIME + " = " + "\'" + temp + "\';");
        }

        c.close();
    }

    public void cleanTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

    public int getDataCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,null,null,null,null,null,null);
        int count = c.getCount();

        c.close();

        return count;
    }

    public int getPower(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,null
                ,TABLE_COUNT + "=?",new String[]{id + ""},null,null,null);

        c.moveToFirst();
        int temp = c.getInt(2);
        c.close();

        return temp;
    }

    public String getTime(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,null
                ,TABLE_COUNT + "=?",new String[]{id + ""},null,null,null);

        c.moveToFirst();
        String temp = c.getString(3);
        c.close();

        return temp;
    }

    public String getHours(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,null
                ,TABLE_COUNT + "=?",new String[]{id + ""},null,null,null);

        c.moveToFirst();
        String temp = c.getString(4);
        c.close();

        return temp;
    }

    public String getminutes(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,null
                ,TABLE_COUNT + "=?",new String[]{id + ""},null,null,null);

        c.moveToFirst();
        String temp = c.getString(5);
        c.close();

        return temp;
    }

    public int getCount(String time)
    {
        int temp;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,null
                ,TABLE_TIME + "=?",new String[]{time},null,null,null);

        if (c.moveToFirst())
            temp = c.getInt(1);
        else
            temp = -1;

        c.close();

        return temp;
    }

    public void delData(String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_TIME + " = " + "\'" + time + "\';");
    }

    public void delData(int power)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_POWER + " = " + "\'" + power + "\';");
    }


}
