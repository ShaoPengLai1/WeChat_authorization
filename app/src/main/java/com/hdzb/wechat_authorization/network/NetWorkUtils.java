package com.hdzb.wechat_authorization.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NetWorkUtils {
    private final String BASE_URL = "http://sa.bhz.com.cn/";
    private static volatile NetWorkUtils instance;
    private final OkHttpClient httpClient;
    private BaseApis mBaseApis;
    public static NetWorkUtils getInstance() {

        if (instance == null){
            synchronized (NetWorkUtils.class){
                if (instance == null){
                    instance = new NetWorkUtils();
                }
            }
        }
        return instance;
    }

    private NetWorkUtils() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        httpClient=builder.build();
        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build();
        mBaseApis=retrofit.create(BaseApis.class);
    }
    public void get(String url, HttpListener listener) {
        mBaseApis.get(url)
                //后台执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置rxjava
                .subscribe(getObserver(listener));
    }

    public void post(String url, Map<String, String> map, HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        mBaseApis.post(url,map)
                // 后台执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置rxjava
                .subscribe(getObserver(listener));

    }

    /**
     * 观察者
     */
    private Observer getObserver(final HttpListener listener) {
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onFail(e.getMessage());
                }
            }
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    if (null != listener) {
                        listener.onSuccess(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }
        };
        return observer;
    }

    //定义接口
    public interface HttpListener {
        void onSuccess(String data);

        void onFail(String error);
    }
}
