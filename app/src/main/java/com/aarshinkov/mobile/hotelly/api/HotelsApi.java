package com.aarshinkov.mobile.hotelly.api;

import com.aarshinkov.mobile.hotelly.requests.hotels.HotelCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HotelsApi {

    @GET("api/hotels")
    Call<List<HotelGetResponse>> getHotels();

    @POST("api/hotels")
    Call<HotelGetResponse> createHotel(@Body HotelCreateRequest hcr);
}
