package com.demo.test.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.demo.test.DemoApp;


public class DemoApi {
    public static final String APP_BASE_URL = "https://m.test.com";

    public static String getAppUrl() {
        SharedPreferences preferences = DemoApp.getInstance().getSharedPreferences("APICLIENT", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(preferences.getString("BASE_URL", ""))) {
            return "https://" + preferences.getString("BASE_URL", "");
        }
        return DemoApi.APP_BASE_URL;
    }
}
