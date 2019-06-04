package com.hdzb.wechat_authorization.wxapi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hdzb.wechat_authorization.R;
import com.hdzb.wechat_authorization.api.Apis;
import com.hdzb.wechat_authorization.base.BaseActivity;
import com.hdzb.wechat_authorization.mvp.ContractEntity;
import com.hdzb.wechat_authorization.mvp.presenter.IPresenterImpl;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;
import java.util.Map;

import rx.schedulers.Schedulers;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler, ContractEntity.IView {

    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private IPresenterImpl iPresenter;
    private static final String WEIXIN_APP_ID="wxa1c980f33676e55c";
    private static final String WEIXIN_APP_SECRET="c1affb81fcd554edfd19a04aafae1136";
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wxentry;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        iPresenter=new IPresenterImpl(this);
        WeiXinUtil.reg(WXEntryActivity.this).handleIntent(getIntent(), this);
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        edit = preferences.edit();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(final BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //得到code
                        String code = ((SendAuth.Resp) baseResp).code;
                        Map<String, String> map = new HashMap<>();
                        map.put("code", code);
                        map.put("appid",WEIXIN_APP_ID);
                        map.put("secret",WEIXIN_APP_SECRET);
                        map.put("grant_type","authorization_code");
                        System.out.println(code);
//                        iPresenter.startRequestPost();
                    }
                });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                break;
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof WeiXinBean) {
            WeiXinBean xinBean = (WeiXinBean) data;
            if (xinBean == null || !xinBean.isSuccess()) {
                Toast.makeText(this,xinBean.getMessage(),Toast.LENGTH_LONG).show();
            } else {
                int userId = xinBean.getResult().getUserId();
                String sessionId = xinBean.getResult().getSessionId();
                finish();
            }
        }
    }

    @Override
    public void getDataFail(String error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }
}
