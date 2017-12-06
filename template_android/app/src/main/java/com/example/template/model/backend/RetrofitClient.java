package com.example.template.model.backend;


import com.example.template.utils.ToStringConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chike on 12/3/2016.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static void clearRetrofit() {
        RetrofitClient.retrofit = null;
    }

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {

//            int cacheSize = 10 * 1024 * 1024; // 10 MB
//            Cache cache = new Cache(getCacheDir(), cacheSize);
//
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .cache(cache)
//                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    //.client(okHttpClient)
                    // RxJava
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(new ToStringConverterFactory())
                    .build();
        } else {

        }
        return retrofit;
    }
}
