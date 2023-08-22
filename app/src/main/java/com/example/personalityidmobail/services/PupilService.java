package com.example.personalityidmobail.services;

import com.example.personalityidmobail.models.AuthUserRequest;
import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.models.Pupil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PupilService {
    @GET("/pupil/listpupilfromparent/{id}")
    Call<List<Pupil>> listPupil(@Path("id") String id);
}
