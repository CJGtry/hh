package com.example.administrator.chineseweather.model;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public class Country {
    private int id;
    private String countryname;
    private String countrycode;
    private int cityid;

    public int getId(){
        return  id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCountryname(){
        return countryname;
    }

    public void setCountryname(String countryname){
        this.countryname = countryname;
    }

    public String getCountrycode(){
        return countrycode;
    }

    public void setCountrycode(String countrycode){
        this.countrycode = countrycode;
    }

    public int getCityid(){
        return cityid;
    }

    public void setCityid(int cityid){
        this.cityid = cityid;
    }
}
