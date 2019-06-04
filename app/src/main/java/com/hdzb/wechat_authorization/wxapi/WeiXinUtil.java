package com.hdzb.wechat_authorization.wxapi;

import android.content.Context;

import com.hdzb.wechat_authorization.network.NetWorkUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import rx.schedulers.Schedulers;

public class WeiXinUtil {
    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    public static  String APP_ID = "wxa1c980f33676e55c";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private WeiXinUtil() {

    }
    public  static  boolean success(Context context){
        //判断是否安装过微信
        if (WeiXinUtil.reg(context).isWXAppInstalled()) {
            return  true;
        }else {
            return false;
        }
    }
    public static IWXAPI reg(Context context) {
        if (context != null) {
            //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
            IWXAPI wxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
            //注册到微信
            wxapi.registerApp(APP_ID);
            return wxapi;
        } else {
            return null;
        }
    }



}
