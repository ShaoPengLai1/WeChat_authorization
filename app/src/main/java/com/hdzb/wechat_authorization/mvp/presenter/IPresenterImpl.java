package com.hdzb.wechat_authorization.mvp.presenter;

import com.hdzb.wechat_authorization.mvp.ContractEntity;
import com.hdzb.wechat_authorization.mvp.MyCallBack;
import com.hdzb.wechat_authorization.mvp.model.IModelImpl;

import java.lang.ref.WeakReference;
import java.util.Map;

public class IPresenterImpl implements ContractEntity.IPresenter {
    WeakReference iPresenterWeakReference;
    private IModelImpl mImodel;
    private ContractEntity.IView mIview;

    public IPresenterImpl(ContractEntity.IView mIview) {
        this.mIview = mIview;
        mImodel=new IModelImpl();
    }

    @Override
    public void startRequestPost(String url, Map<String, String> params, Class clazz) {
        mImodel.requestDataPost(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIview.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIview.getDataFail(error);
            }
        });
    }

    @Override
    public void requestGet(String url, Class clazz) {
        mImodel.getRequest(url, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIview.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIview.getDataFail(error);
            }
        });
    }

    public void onDetach() {
        if (mImodel != null) {
            mImodel = null;
        }
        if (mIview != null) {
            mIview = null;
            iPresenterWeakReference = new WeakReference(mIview);
        }
    }
}
