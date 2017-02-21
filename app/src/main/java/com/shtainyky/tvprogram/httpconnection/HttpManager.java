package com.shtainyky.tvprogram.httpconnection;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpManager {

    private static OkHttpClient client = new OkHttpClient();
    // TODO: 20.02.17 declare this variable in build.gradle file
    private static final String URI_BASE = "http://52.50.138.211:8080/ChanelAPI/";

    public static MyApiService getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URI_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MyApiService.class);
    }
}
