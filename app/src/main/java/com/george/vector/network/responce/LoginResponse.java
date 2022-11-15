package com.george.vector.network.responce;

import java.util.List;

public class LoginResponse {

    private final String token;
    private String type;
    private Long id;
    private String username;

    private String zone;

    private String name;
    private String lastName;
    private String patronymic;
    private String email;
    private List<String> roles;

    public LoginResponse(String token, String type, Long id, String username, String zone, String name, String lastName, String patronymic, String email, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.zone = zone;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getZone() {
        return zone;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
