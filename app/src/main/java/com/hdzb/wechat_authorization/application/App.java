package com.hdzb.wechat_authorization.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary内存泄露检测
        refWatcher = LeakCanary.install(this);


        //android 7.0调用相机闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }
    //LeakCanary内存泄露检测的方法
    public static RefWatcher getRefWatcher(Context mContext){
        App myApplication  = (App) mContext.getApplicationContext();
        return myApplication.refWatcher;
    }
}
