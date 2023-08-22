package com.example.personalityidmobail.models;

import java.io.Serializable;

public class AuthUserResponse implements Serializable {
    public int id;
    public String role;
    public String jwtToken;
}
