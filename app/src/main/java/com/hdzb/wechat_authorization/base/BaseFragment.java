package com.hdzb.wechat_authorization.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.hdzb.wechat_authorization.application.App;
import com.squareup.leakcanary.RefWatcher;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(),container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化view
        initView(view);
        //初始化数据
        initData();

    }
    /**
     * 初始化数据
     * */
    protected abstract void initData();
    /**
     * 初始化view
     * */
    protected abstract void initView(View view);

    /**
     * 加载布局
     * */
    protected abstract int getLayoutResId();
    @Override
    public void onDestroy() {
        super.onDestroy();

        //使用 RefWatcher 监控Activity内存泄漏
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
