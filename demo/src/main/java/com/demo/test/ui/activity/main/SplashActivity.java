package com.demo.test.ui.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.library.rx.RxCountDown;
import com.demo.test.R;
import com.demo.test.DemoApp;
import com.demo.test.base.BaseSubscriberContext;
import com.demo.test.entities.StartBean;
import com.demo.test.service.ServiceResponse;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class SplashActivity extends AppCompatActivity implements BaseSubscriberContext{

    private String mPicture;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.main_in, R.anim.main_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
            pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", getPackageName()));
        DemoApp.getActivityInteractor(this)
            .getStartAds(new ServiceResponse<StartBean>(SplashActivity.this) {
                @Override
                public void onNext(StartBean startBean) {
                    mPicture = startBean.getPicture();
                    if (mPicture != null && !"".equals(mPicture)) {
                        Glide.with(SplashActivity.this).load(mPicture).downloadOnly(
                            1280, 1920);
                    }
                }
            });
        if (permission) {
            RxCountDown.countdown(2).subscribe(integer -> {
                if (integer == 0) {
                    jumpToAds();
                }
            }, throwable -> jumpToAds());
        } else {
            new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("应用包含缓存节省流量功能,需要打开存储权限,应用才能正常使用。")
                .setPositiveButton("确认", (dialog, which) -> {
                    dialog.dismiss();
                    getAppDetailSettingIntent();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
        }
    }

    private void jumpToAds() {
    }

    @Override
    protected void onStop() {
        if (mCompositeDisposable!=null){
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.main_in, R.anim.main_out);
    }

    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
        finish();
    }

    @Override
    public void addDisposable(Disposable d) {
        if (mCompositeDisposable==null){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(d);
    }
}
