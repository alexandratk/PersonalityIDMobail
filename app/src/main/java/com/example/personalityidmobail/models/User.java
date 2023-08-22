package com.example.personalityidmobail.models;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String name;
    public String role;
    public String dateofbirth;

    public User() {
    }

    public User(int id, String role) {
        this.id = id;
        this.role = role;
    }
}
