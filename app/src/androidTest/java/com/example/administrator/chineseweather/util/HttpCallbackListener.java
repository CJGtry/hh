package com.example.administrator.chineseweather.util;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
