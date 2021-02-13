package com.aarshinkov.mobile.hotelly.api;

import com.aarshinkov.mobile.hotelly.requests.LoginRequest;
import com.aarshinkov.mobile.hotelly.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest login);
}
