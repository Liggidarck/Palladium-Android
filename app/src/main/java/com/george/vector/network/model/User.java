package com.george.vector.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;

    @Expose
    private final String zone;

    @Expose
    private final String name;

    @Expose
    private final String lastName;

    @Expose
    private final String patronymic;

    @Expose
    private final String email;

    @Expose
    private final String password;

    @Expose
    private final String username;

    @SerializedName("roles")
    @Expose
    private final List<Role> roles;

    public User(String zone, String name, String lastName, String patronymic,
                String email, String password, String username, List<Role> roles) {
        this.zone = zone;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Role> getRoles() {
        return roles;
    }
}
