package com.aarshinkov.mobile.hotelly.api;

import com.aarshinkov.mobile.hotelly.requests.hotels.HotelCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HotelsApi {

    @GET("api/hotels")
    Call<List<HotelGetResponse>> getHotels();

    @GET("api/hotels/{hotelId}")
    Call<HotelGetResponse> getHotel(@Path("hotelId") String hotelId);

    @POST("api/hotels")
    Call<HotelGetResponse> createHotel(@Body HotelCreateRequest hcr);
}
