package com.hdzb.wechat_authorization;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hdzb.wechat_authorization.adapter.PagerAdapter;
import com.hdzb.wechat_authorization.base.BaseActivity;
import com.hdzb.wechat_authorization.mvp.ContractEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ContractEntity.IView {


    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mTablayout)
    TabLayout mTablayout;
    @BindView(R.id.submit_skip)
    TextView submitSkip;
    private DrawerLayout.DrawerListener drawerListener;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

        //加载侧拉
        loginDrawLeft();
        //初始化ViewPage
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        mTablayout.setupWithViewPager(mViewPager);
        //跳转
        submitSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginPageActivity.class));
            }
        });
    }

    private void loginDrawLeft() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {

        } else {
            //判断左侧框是否打开，打开返回true，关闭返回flase
            //打开左侧框
            mDrawerLayout.openDrawer(Gravity.START);
            //关闭左侧框
            mDrawerLayout.closeDrawer(Gravity.START);
            mDrawerLayout.addDrawerListener(drawerListener);
            //去除侧滑时的阴影
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View view, float v) {


                }

                @Override
                public void onDrawerOpened(@NonNull View view) {

                }

                @Override
                public void onDrawerClosed(@NonNull View view) {
//                    webView.clearCache(true);
//                    webView.clearHistory();
                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            };
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);


    }

    @Override
    public void getDataSuccess(Object data) {

    }

    @Override
    public void getDataFail(String error) {

    }

}
