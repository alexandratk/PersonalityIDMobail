package com.example.personalityidmobail.models;

import java.io.Serializable;

public class Lesson implements Serializable {
    public int id;
    public String dateofstart;
    public String dateoffinish;
    public String audience;
    public String description;
    public Teacher teacher;
    public int educInstId;
}
