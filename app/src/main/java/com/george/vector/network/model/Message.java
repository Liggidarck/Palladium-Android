package com.george.vector.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    @SerializedName("message")
    @Expose
    String message;

    public String getMessage() {
        return message;
    }
}
