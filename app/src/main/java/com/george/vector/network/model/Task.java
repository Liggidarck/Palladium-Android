package com.george.vector.network.model;

public class Task {

    private String name_task;
    private String address;
    private String date_create;
    private String floor;
    private String cabinet;
    private String letter;
    private String comment;
    private String date_done;
    private String executor;
    private String status;
    private boolean urgent;
    private String time_create;
    private String email_creator;
    private String image;
    private String full_name_executor;
    private String name_creator;

    public Task() {}

    public Task(String name_task, String address, String date_create, String floor,
                String cabinet, String litera, String comment, String date_done, String executor, String status,
                String time_create, String email_creator, boolean urgent, String image, String full_name_executor, String name_creator) {
        this.name_task = name_task;
        this.address = address;
        this.date_create = date_create;
        this.floor = floor;
        this.cabinet = cabinet;
        this.letter = litera;
        this.comment = comment;
        this.date_done = date_done;
        this.executor = executor;
        this.status = status;
        this.urgent = urgent;
        this.time_create = time_create;
        this.email_creator = email_creator;
        this.image = image;
        this.full_name_executor = full_name_executor;
        this.name_creator = name_creator;
    }

    public String getName_task() {
        return name_task;
    }

    public String getAddress() {
        return address;
    }

    public String getDate_create() {
        return date_create;
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

    public String getDate_done() {
        return date_done;
    }

    public String getExecutor() {
        return executor;
    }

    public String getStatus() {
        return status;
    }

    public String getTime_create() {
        return time_create;
    }

    public String getEmail_creator() {
        return email_creator;
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
        return full_name_executor;
    }

    public String getNameCreator() {
        return name_creator;
    }

}