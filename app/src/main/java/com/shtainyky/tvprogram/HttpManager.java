package com.shtainyky.tvprogram;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpManager {
    private static OkHttpClient client = new OkHttpClient();

    public static String getData(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
