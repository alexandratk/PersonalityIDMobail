package com.example.personalityidmobail.services;

import com.example.personalityidmobail.models.Lesson;
import com.example.personalityidmobail.models.Pupil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LessonService {
    @GET("/lesson/pupil/todaytimetable/{id}")
    Call<List<Lesson>> listLessonPupil(@Path("id") String id);

    @GET("/lesson/teacher/todaytimetable/{id}")
    Call<List<Lesson>> listLessonTeacher(@Path("id") String id);
}
