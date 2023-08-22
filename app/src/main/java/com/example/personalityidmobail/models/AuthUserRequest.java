package com.example.personalityidmobail.models;

public class AuthUserRequest {
    public String login;
    public String password;

    public AuthUserRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
