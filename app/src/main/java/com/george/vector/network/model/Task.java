package com.george.vector.network.model;

public class Task {

    private long id;

    private String zone;
    private String status;

    private String name;
    private String comment;

    private String address;
    private String floor;
    private String cabinet;
    private String letter;
    private boolean urgent;
    private String dateDone;

    private int executorId;
    private int creatorId;

    private String dateCreate;
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