package com.seki.noteasklite.RetrofitHelper.RequestBody;

import com.seki.noteasklite.Config.NONoConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yuan-tian01 on 2016/3/30.
 */
public class RetrofitWrapper {
    static Retrofit retrofit = null;
    public static Retrofit getRetrofit(){
        if(retrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            Retrofit.Builder getNoteServiceBuilder = new Retrofit.Builder();
            getNoteServiceBuilder.baseUrl(NONoConfig.NONoURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            retrofit = getNoteServiceBuilder.build();
            return retrofit;
        }else{
            return retrofit;
        }
    }
}
