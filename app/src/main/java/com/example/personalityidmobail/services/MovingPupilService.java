package com.example.personalityidmobail.services;

import com.example.personalityidmobail.models.AuthUserRequest;
import com.example.personalityidmobail.models.AuthUserResponse;
import com.example.personalityidmobail.models.MovingPupil;
import com.example.personalityidmobail.models.Pupil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MovingPupilService {
    @GET("/movingPupil/movingpupilforparent/{id}")
    Call<List<MovingPupil>> listMovingPupil(@Path("id") String id);
}
