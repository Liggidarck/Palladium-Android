package com.george.vector.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Task implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;

    @Expose
    private String zone;

    @Expose
    private String status;

    @Expose
    private String name;

    @Expose
    private String comment;

    @Expose
    private String address;

    @Expose
    private String floor;

    @Expose
    private String cabinet;

    @Expose
    private String letter;

    @Expose
    private boolean urgent;

    @Expose
    private String dateDone;

    @Expose
    private int executorId;

    @Expose
    private int creatorId;

    @Expose
    private String dateCreate;

    @Expose
    private String image;

    public Task(String zone, String status, String name, String comment, String address, String floor, String cabinet, String letter, boolean urgent, String dateDone, int executorId, int creatorId, String dateCreate, String image) {
        this.zone = zone;
        this.status = status;
        this.name = name;
        this.comment = comment;
        this.address = address;
        this.floor = floor;
        this.cabinet = cabinet;
        this.letter = letter;
        this.urgent = urgent;
        this.dateDone = dateDone;
        this.executorId = executorId;
        this.creatorId = creatorId;
        this.dateCreate = dateCreate;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getZone() {
        return zone;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getAddress() {
        return address;
    }

    public String getFloor() {
        return floor;
    }

    public String getCabinet() {
        return cabinet;
    }

    public String getLetter() {
        return letter;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public String getDateDone() {
        return dateDone;
    }

    public int getExecutorId() {
        return executorId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getImage() {
        return image;
    }
}