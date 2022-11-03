package com.george.vector.network.api;

import com.george.vector.network.model.User;
import com.george.vector.network.request.LoginRequest;
import com.george.vector.network.responce.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthInterface {

    @POST("/fluffy_foxy/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/fluffy_foxy/auth/register")
    Call<String> register(@Body User user);

}
