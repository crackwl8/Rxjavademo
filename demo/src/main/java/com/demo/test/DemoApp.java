package com.demo.test;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.demo.test.module.ActivityInteractor;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.List;

/**

 */
public class DemoApp extends MultiDexApplication {
    private AppComponent component;
    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Stetho.initializeWithDefaults(this);

        AutoLayoutConifg.getInstance().useDeviceSize();

        component = DaggerAppComponent.builder().appModule(new AppModule()).build();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static ActivityInteractor getActivityInteractor(Context context) {
        return DemoApp.get(context).component().getActivityInteractor();
    }


    private AppComponent component() {
        return component;
    }

    private static DemoApp get(Context context) {
        return (DemoApp) context.getApplicationContext();
    }
}
