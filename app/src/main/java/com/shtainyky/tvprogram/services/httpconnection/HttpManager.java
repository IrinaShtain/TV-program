package com.shtainyky.tvprogram.services.httpconnection;


import com.shtainyky.tvprogram.BuildConfig;
import com.shtainyky.tvprogram.R;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpManager {
    //  20.02.17 declare this variable in build.gradle file

    public static MyApiService getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MyApiService.class);
    }
}
