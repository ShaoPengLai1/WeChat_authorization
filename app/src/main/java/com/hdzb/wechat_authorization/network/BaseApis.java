package com.hdzb.wechat_authorization.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface BaseApis<E> {

    @GET
    Observable<ResponseBody> get(@Url String url);
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> map);
}
