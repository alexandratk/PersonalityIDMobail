package com.example.personalityidmobail.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Pupil implements Serializable {
    public int id;
    public String name;
    public String role;
    public String dateofbirth;
    public Group group;
}
