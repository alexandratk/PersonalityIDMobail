package com.example.personalityidmobail.services;

import com.example.personalityidmobail.models.Mark;
import com.example.personalityidmobail.models.MovingPupil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MarkService {
    @GET("/mark/getmarkspupil/{id}")
    Call<List<Mark>> listMarks(@Path("id") String id);

    @GET("/mark/searchmarkspupil/{id}/{search}")
    Call<List<Mark>> searchMarks(@Path("id") String id, @Path("search") String search);
}
