package com.example.administrator.chineseweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.chineseweather.db.ChineseWeatherOpenHelper;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class ChineseWeatherDB {
    public static final String DB_name = "Chinese_Weather";

    public static final int version = 1;

    private static ChineseWeatherDB chineseWeatherDB;

    private SQLiteDatabase db;

    private ChineseWeatherDB(Context context){
        ChineseWeatherOpenHelper dbHelper = new ChineseWeatherOpenHelper(context,DB_name,null,version);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static ChineseWeatherDB getInstance(Context context){
        if (chineseWeatherDB == null){
            chineseWeatherDB = new ChineseWeatherDB(context);
        }
        return chineseWeatherDB;
    }

    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvincename());
            values.put("province_code",province.getProvincecode());
            db.insert("province",null,values);
        }
    }

    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityname());
            values.put("city_code",city.getCitycode());
            values.put("province_id",city.getProvinceid());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{
                String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceid(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    public void saveCountry(Country country){
        if(country != null){
            ContentValues values = new ContentValues();
            values.put("country_name",country.getCountryname());
            values.put("country_code",country.getCountrycode());
            values.put("city_id",country.getCityid());
            db.insert("Country",null,values);
        }
    }

    public List<Country> loadCounties(int cityId) {
            List<Country> list = new ArrayList<Country>();
            Cursor cursor = db.query("Country", null, "city_id = ?", new String[]{
                    String.valueOf(cityId)}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryname(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountrycode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityid(cityId);
                list.add(country);
            }while(cursor.moveToNext());
        }
        if(cursor.moveToNext()){
            cursor.close();
        }
        return list;
    }
}
