package com.seki.noteasklite.RetrofitHelper.RequestBody;

import android.util.Log;

import com.seki.noteasklite.BuildConfig;
import com.seki.noteasklite.Config.NONoConfig;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
            OkHttpClient okHttpClient= null;
            if(BuildConfig.DEBUG){
                okHttpClient =  new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {

                                Request request = chain.request();
                                Response response = chain.proceed(request);


                                Log.d("nono net request",String.valueOf( request.toString()));


                                okhttp3.MediaType mediaType = response.body().contentType();
                                String content = response.body().string();
                                Log.i("nono net response", "response body:" + content);
                                return response.newBuilder()
                                        .body(okhttp3.ResponseBody.create(mediaType, content))
                                        .build();
                            }
                        })
                        .build();
            }else{
                okHttpClient = new OkHttpClient.Builder().build();
            }

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
