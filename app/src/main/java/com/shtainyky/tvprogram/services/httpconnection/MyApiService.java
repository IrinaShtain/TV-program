package com.shtainyky.tvprogram.services.httpconnection;

import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// TODO: 20.02.17 avoid My prefix, make no sense
public interface MyApiService {

    @GET("chanels")
    Call<List<ChannelItem>> getChannelsList();

    @GET("categories")
    Call<List<CategoryItem>> getCategoriesList();

    @GET("programs/{timestamp}")
    Call<List<ProgramItem>> getProgramsList(@Path("timestamp") long timeStamp);
}
