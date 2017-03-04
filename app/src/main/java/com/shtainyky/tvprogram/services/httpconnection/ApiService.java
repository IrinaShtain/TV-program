package com.shtainyky.tvprogram.services.httpconnection;

import com.shtainyky.tvprogram.models.retrofit.Category;
import com.shtainyky.tvprogram.models.retrofit.Channel;
import com.shtainyky.tvprogram.models.retrofit.Program;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("chanels")
    Call<List<Channel>> getChannelsList();

    @GET("categories")
    Call<List<Category>> getCategoriesList();

    @GET("programs/{timestamp}")
    Call<List<Program>> getProgramsList(@Path("timestamp") long timeStamp);
}
