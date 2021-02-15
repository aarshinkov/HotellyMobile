package com.aarshinkov.mobile.hotelly.api;

import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HotelsApi {

    @GET("api/hotels")
    Call<List<HotelGetResponse>> getHotels();
}
