package com.hdzb.wechat_authorization.base;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hdzb.wechat_authorization.application.App;
import com.squareup.leakcanary.RefWatcher;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 页面增加一个判断，因为4.4版本之前没有沉浸式可言
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //加载布局
        setContentView(getLayoutResId());
        //初始化view
        initView(savedInstanceState);
        //初始化数据
        initData();
        stateNetWork();
    }



    /**
     * 加载布局
     * */
    protected abstract int getLayoutResId();
    /**
     * 初始化数据
     * */
    protected abstract void initData();
    /**
     * 初始化view
     * */
    protected abstract void initView(Bundle savedInstanceState);
    //动态注册权限
    private void stateNetWork() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            String[] mStatenetwork = new String[]{
                    //写的权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    //读的权限
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //入网权限
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    //WIFI权限
                    Manifest.permission.ACCESS_WIFI_STATE,
                    //读手机权限
                    Manifest.permission.READ_PHONE_STATE,
                    //网络权限
                    Manifest.permission.INTERNET,
                    //相机
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    //定位
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,

                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.REQUEST_INSTALL_PACKAGES,
            };
            ActivityCompat.requestPermissions(this,mStatenetwork,100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission  = false;
        if(requestCode == 100){
            for (int i = 0;i<grantResults.length;i++){
                if(grantResults[i] == -1){
                    hasPermission = true;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //使用 RefWatcher 监控Activity内存泄漏
        RefWatcher refWatcher = App.getRefWatcher(this);
        refWatcher.watch(this);
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (BaseActivity.this.getCurrentFocus() != null) {
                if (BaseActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
