package com.george.vector.network.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EditUserModel implements Serializable {


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
    private final String username;

    @SerializedName("roles")
    @Expose
    private final List<String> roles;

    public EditUserModel(String zone, String name, String lastName, String patronymic, String email, String username, List<String> roles) {
        this.zone = zone;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }
}
