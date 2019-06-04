package com.hdzb.wechat_authorization.mvp.model;

import com.google.gson.Gson;
import com.hdzb.wechat_authorization.mvp.ContractEntity;
import com.hdzb.wechat_authorization.mvp.MyCallBack;
import com.hdzb.wechat_authorization.network.NetWorkUtils;

import java.util.Map;

public class IModelImpl implements ContractEntity.IModel {
    @Override
    public void requestDataPost(String url, Map<String, String> params, final Class clazz, final MyCallBack myCallBack) {
        NetWorkUtils.getInstance().post(url, params,new NetWorkUtils.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try{
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack != null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(myCallBack != null){
                    myCallBack.onFail(error);
                }
            }
        });
    }

    @Override
    public void getRequest(String url, final Class clazz, final MyCallBack myCallBack) {
        NetWorkUtils.getInstance().get(url, new NetWorkUtils.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack!=null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(myCallBack!=null){
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(myCallBack!=null){
                    myCallBack.onFail(error);
                }
            }
        });
    }
}
