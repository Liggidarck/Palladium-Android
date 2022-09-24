package com.george.vector.network.model;

public class Task {

    private String nameTask;
    private String address;
    private String dateCreate;
    private String floor;
    private String cabinet;
    private String letter;
    private String comment;
    private String dateDone;
    private String executor;
    private String status;
    private boolean urgent;
    private String timeCreate;
    private String emailCreator;
    private String image;
    private String fullNameExecutor;
    private String nameCreator;

    public Task() {}

    public Task(String nameTask, String address, String dateCreate, String floor,
                String cabinet, String litera, String comment, String dateDone, String executor, String status,
                String timeCreate, String emailCreator, boolean urgent, String image, String fullNameExecutor, String nameCreator) {
        this.nameTask = nameTask;
        this.address = address;
        this.dateCreate = dateCreate;
        this.floor = floor;
        this.cabinet = cabinet;
        this.letter = litera;
        this.comment = comment;
        this.dateDone = dateDone;
        this.executor = executor;
        this.status = status;
        this.urgent = urgent;
        this.timeCreate = timeCreate;
        this.emailCreator = emailCreator;
        this.image = image;
        this.fullNameExecutor = fullNameExecutor;
        this.nameCreator = nameCreator;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getAddress() {
        return address;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getFloor() {
        return floor;
    }

    public String getCabinet() {
        return cabinet;
    }

    public String getComment() {
        return comment;
    }

    public String getDateDone() {
        return dateDone;
    }

    public String getExecutor() {
        return executor;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public String getEmailCreator() {
        return emailCreator;
    }

    public String getLitera() {
        return letter;
    }

    public boolean getUrgent() {
        return urgent;
    }

    public String getImage() {
        return image;
    }

    public String getFullNameExecutor() {
        return fullNameExecutor;
    }

    public String getNameCreator() {
        return nameCreator;
    }

}