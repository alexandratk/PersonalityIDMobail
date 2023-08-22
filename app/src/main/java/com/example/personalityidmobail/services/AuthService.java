package com.example.personalityidmobail.services;

import com.example.personalityidmobail.models.AuthUserRequest;
import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/user/authuser")
    Call<AuthUserResponse> authUser(@Body AuthUserRequest user);

    @POST("/user/profile")
    Call<User> profileUser(@Body AuthUserResponse authUserResponse);
}
