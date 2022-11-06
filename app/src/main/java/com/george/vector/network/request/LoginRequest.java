package com.george.vector.network.request;

public class LoginRequest {

    private String username;

    private String password;


    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}