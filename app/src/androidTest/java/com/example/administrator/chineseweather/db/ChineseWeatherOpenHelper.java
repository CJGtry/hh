package com.example.administrator.chineseweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class ChineseWeatherOpenHelper extends SQLiteOpenHelper {
    public static final String Province = "create table Province(" + " id integer primary key autoincrement,"
            + "province_name text," + "province_code text )";
    public static final String  City = "create table city (" + " id integer primary key autoincrement,"
            + "city_name text," + "city_code text," + "provice_id integer)";
    public static final String  Country = "create table city (" + " id integer primary key autoincrement,"
            + "country_name text," + "country_code text," + "city_id integer)";
    public ChineseWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(Province);
        db.execSQL(City);
        db.execSQL(Country);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
