package com.shtainyky.tvprogram.services.httpconnection;

import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.models.models_retrofit.Category;
import com.shtainyky.tvprogram.models.models_retrofit.Channel;
import com.shtainyky.tvprogram.models.models_retrofit.Program;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

//  20.02.17 avoid My prefix, make no sense
public interface ApiService {

    @GET("chanels")
    Call<List<Channel>> getChannelsList();

    @GET("categories")
    Call<List<Category>> getCategoriesList();

    @GET("programs/{timestamp}")
    Call<List<Program>> getProgramsList(@Path("timestamp") long timeStamp);
}
