package com.shtainyky.tvprogram.httpconnection;

import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface MyApiService {

    @GET("chanels")
    Call<List<ChannelItem>> getChannelsList();

    @GET("categories")
    Call<List<CategoryItem>> getCategoriesList();

    @GET("programs/{programs}")
    Call<List<ProgramItem>> getProgramsList(@Path("programs") long timeStamp);
}
