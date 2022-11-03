package com.george.vector.network.model;

import java.util.List;
import java.util.Set;

public class User {

    private long id;
    private final String zone;
    private final String name;
    private final String lastName;
    private final String patronymic;
    private final String email;
    private final String password;
    private final String username;
    private final List<Role> role;

    public User(String zone, String name, String lastName, String patronymic,
                String email, String password, String username, List<Role> role) {
        this.zone = zone;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public List<Role> getRole() {
        return role;
    }
}
