package com.hdzb.wechat_authorization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.hdzb.wechat_authorization.base.BaseActivity;
import com.hdzb.wechat_authorization.wxapi.WeiXinUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginPageActivity extends BaseActivity {

    @BindView(R.id.webView)
    BridgeWebView webView;



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login_page;
    }

    @Override
    protected void initData() {
        webView.loadUrl("http://sa.bhz.com.cn");
        webView.evaluateJavascript("", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
        //默认接收
        webView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String msg = "默认接收到js的数据：" + data;
                Toast.makeText(LoginPageActivity.this, msg, Toast.LENGTH_LONG).show();

                function.onCallBack("java默认接收完毕，并回传数据给js"); //回传数据给js
            }
        });
        //指定接收 submitFromWeb 与js保持一致
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String msg = "指定接收到js的数据：" + data;
                Toast.makeText(LoginPageActivity.this, msg, Toast.LENGTH_LONG).show();
                function.onCallBack("java指定接收完毕，并回传数据给js"); //回传数据给js
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }


    public void login(View view) {
        //微信登录
        if (!WeiXinUtil.success(LoginPageActivity.this)) {
            Toast.makeText(LoginPageActivity.this, "请先安装应用", Toast.LENGTH_SHORT).show();
        } else {
            //  验证
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            WeiXinUtil.reg(LoginPageActivity.this).sendReq(req);
            finish();
        }
    }
}
