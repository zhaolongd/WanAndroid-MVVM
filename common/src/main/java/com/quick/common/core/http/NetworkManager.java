package com.quick.common.core.http;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhaolong.
 * Description: 管理retrofit
 * Date: 2020/5/22 0022 14:24
 */

public class NetworkManager {
    private static NetworkManager instance;
    private Retrofit app_retrofit;

    private NetworkManager(){
        init();
    }

    public static NetworkManager instance() {
        if (instance == null){
            instance = new NetworkManager();
        }
        return instance;
    }

    private void init(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//打印请求参数，请求结果
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        app_retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.wanandroid.com/")//base_url:http+域名
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用Rxjava对回调数据进行处理
                .addConverterFactory(GsonConverterFactory.create())//响应结果的解析器，包含gson，xml，protobuf
                .build();
    }

    public <T> T create(int requestType ,final Class<T> service){
        /*if (requestType == WDViewModel.REQUEST_TYPE_APP_AD){//如果请求广告的接口
            return ad_retrofit.create(service);
        }*/
        return app_retrofit.create(service);
    }
}
