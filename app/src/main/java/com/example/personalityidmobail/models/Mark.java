package com.example.personalityidmobail.models;

import java.io.Serializable;

public class Mark implements Serializable {
    public int id;
    public Lesson lesson;
    public Pupil pupil;
    public String lessonMark;
    public String description;
    public String dateTimeMark;
}
