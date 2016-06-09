package com.example.administrator.chineseweather.util;

import android.text.TextUtils;

import com.example.administrator.chineseweather.model.ChineseWeatherDB;
import com.example.administrator.chineseweather.model.Country;
import com.example.administrator.chineseweather.model.Province;
import com.example.administrator.chineseweather.model.City;
/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(
            ChineseWeatherDB chineseWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvincecode(array[0]);
                    province.setProvincename(array[1]);
                    chineseWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(
            ChineseWeatherDB chineseWeatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length > 0){
                for(String c : allCities){
                    String [] array =c.split("\\|");
                    City city = new City();
                    city.setCitycode(array[0]);
                    city.setCityname(array[1]);
                    city.setProvinceid(provinceId);
                    chineseWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(
            ChineseWeatherDB chineseWeatherDB,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountrycode(array[0]);
                    country.setCountryname(array[1]);
                    country.setCityid(cityId);
                    chineseWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }
}
