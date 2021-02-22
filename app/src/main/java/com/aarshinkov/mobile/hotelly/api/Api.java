package com.aarshinkov.mobile.hotelly.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.aarshinkov.mobile.hotelly.utils.Constants.API_URL;

public class Api {

    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
