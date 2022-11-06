package com.george.vector.network.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RegisterUserModel implements Serializable {

    @SerializedName("zone")
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

    @Expose
    private final List<String> role;

    public RegisterUserModel(String zone, String name, String lastName, String patronymic, String email, String password, String username, List<String> role) {
        this.zone = zone;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
