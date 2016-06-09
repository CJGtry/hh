package com.example.administrator.chineseweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.chinaweather.R;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.chineseweather.model.ChineseWeatherDB;
import com.example.administrator.chineseweather.model.City;
import com.example.administrator.chineseweather.model.Country;
import com.example.administrator.chineseweather.model.Province;
import com.example.administrator.chineseweather.util.HttpCallbackListener;
import com.example.administrator.chineseweather.util.HttpUtil;
import com.example.administrator.chineseweather.util.Utility;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ChineseWeatherDB chineseWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        chineseWeatherDB = ChineseWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>arg0, View view, int index,long arg3) {
               if(currentLevel == LEVEL_PROVINCE){
                   selectedProvince = provinceList.get(index);
                   queryCities();
               }else if(currentLevel == LEVEL_CITY){
                   selectedCity = cityList.get(index);
                   queryCounties();
               }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        provinceList = chineseWeatherDB.loadProvinces();
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province province: provinceList){
                dataList.add(province.getProvincename());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }
    }

    private void queryCities(){
        cityList = chineseWeatherDB.loadCities(selectedProvince.getId());
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvincename());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvincecode(),"city");
        }
    }

    private void queryCounties(){
        countryList = chineseWeatherDB.loadCounties(selectedCity.getId());
        if(countryList.size() > 0){
            dataList.clear();
            for(Country country : countryList){
                dataList.add(country.getCountryname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityname());
            currentLevel = LEVEL_COUNTRY;
        }else
            queryFromServer(selectedCity.getCitycode(),"country");
    }

    private void queryFromServer(final String code,final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(chineseWeatherDB,response);
                }else if("city".equals(type)){
                    result = Utility.handleCitiesResponse(chineseWeatherDB,response,selectedProvince.getId());
                }else if("country".equals(type)){
                    result = Utility.handleCountiesResponse(chineseWeatherDB,response,selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable(){
                       @Override
                        public void run(){
                           closeProgressDialog();
                           if("province".equals(type)){
                               queryProvinces();
                           }else if("city".equals(type)){
                               queryCities();
                           }else if("country".equals(type)){
                               queryCounties();
                           }
                       }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed(){
        if(currentLevel == LEVEL_COUNTRY){
            queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }else{
            finish();
        }
    }
}
