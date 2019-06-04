package com.hdzb.wechat_authorization.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hdzb.wechat_authorization.base.BaseFragment;
import com.hdzb.wechat_authorization.fragment.HomeFragment;
import com.hdzb.wechat_authorization.fragment.MineFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private String[] pageNames=new String[]{"首页", "我的"};
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new HomeFragment();
            case 1:
                return new MineFragment();
            default:
                return new Fragment();
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageNames[position];
    }

    @Override
    public int getCount() {
        return pageNames.length;
    }

}
