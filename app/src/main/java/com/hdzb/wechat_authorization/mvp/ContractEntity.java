package com.hdzb.wechat_authorization.mvp;

import java.util.Map;

public interface ContractEntity {

    interface IModel{
        void requestDataPost(String url, Map<String, String> params, Class clazz, MyCallBack myCallBack);
        void getRequest(String url,Class clazz,MyCallBack myCallBack);
    }
    interface IPresenter{
        void startRequestPost(String url, Map<String, String> params, Class clazz);
        void requestGet(String url,Class clazz);
    }
    interface IView <T>{
        void getDataSuccess(T data);
        void getDataFail(String error);
    }
}
