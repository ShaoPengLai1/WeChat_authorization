package com.hdzb.wechat_authorization.mvp;

public interface MyCallBack<T> {
    void onSuccess(T data);
    void onFail(String error);
}
