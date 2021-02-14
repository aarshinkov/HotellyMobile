package com.aarshinkov.mobile.hotelly.api;

import com.aarshinkov.mobile.hotelly.requests.users.UserCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.users.UserCreatedResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersApi {

    @POST("api/users")
    Call<UserCreatedResponse> createUser(@Body UserCreateRequest ucr);
}
