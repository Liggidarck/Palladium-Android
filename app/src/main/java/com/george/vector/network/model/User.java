package com.george.vector.network.model;

public class User {

    private String name;
    private String last_name;
    private String patronymic;
    private String email;
    private String role;
    private String permission;
    private String password;

    public User() {
    }

    public User(String name, String last_name, String patronymic, String email, String role, String permission, String password) {
        this.name = name;
        this.last_name = last_name;
        this.patronymic = patronymic;
        this.email = email;
        this.role = role;
        this.permission = permission;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPermission() {
        return permission;
    }

    public String getPassword() {
        return password;
    }
}
