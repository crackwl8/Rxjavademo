package com.demo.test.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.demo.test.BuildConfig;
import com.demo.test.DemoApp;
import com.demo.test.data.DemoApi;
import com.demo.test.okhttp3.PersistentCookieJar;
import com.demo.test.okhttp3.cache.SetCookieCache;
import com.demo.test.okhttp3.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient.Builder builder;

    static {
        builder = initOkHttpClient();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor).addNetworkInterceptor(new StethoInterceptor());
        }
        mOkHttpClient = builder.build();
    }

    public static Retrofit createAdapter() {
        SharedPreferences sharedPreferences = DemoApp.getInstance().getSharedPreferences("APICLIENT", Context.MODE_PRIVATE);
        String baseUrl;
        if (!TextUtils.isEmpty(sharedPreferences.getString("BASE_URL", ""))) {
            baseUrl = "http://" + sharedPreferences.getString("BASE_URL", "");
        } else {
            baseUrl = DemoApi.APP_BASE_URL;
        }
        return new Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mOkHttpClient)
            .build();
    }

    /**
     * 初始化OKHttpClient
     */
    private static OkHttpClient.Builder initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitClient.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(DemoApp.getInstance().getCacheDir(), "OkHttpCache"),
                        1024 * 1024 * 100);
                    builder = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .cache(cache)
                        .addInterceptor(chain -> {
                            Request request = chain.request();
                            request = request.newBuilder()
                                .header("User-Agent", "Android/" + Build.VERSION.RELEASE + " DemoApp/"
                                    + String.valueOf(BuildConfig.VERSION_CODE) + " Mobile/"
                                    + Build.MODEL)
                                .build();
                            return chain.proceed(request);
                        })
                        .cookieJar(new PersistentCookieJar(new SetCookieCache(),
                            new SharedPrefsCookiePersistor(DemoApp.getInstance())));
                }
            }
        }
        return builder;
    }

}
