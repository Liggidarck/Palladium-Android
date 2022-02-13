package com.george.vector.develop.network;

public class Task {

    private String nameTask;
    private String address;
    private String dateCreate;
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

    public Task(String nameTask, String address, String dateCreate, String floor, String cabinet,
                String letter, String comment, String date_done, String executor, String status,
                boolean urgent, String time_create, String email_creator, String image,
                String full_name_executor, String name_creator) {
        this.nameTask = nameTask;
        this.address = address;
        this.dateCreate = dateCreate;
        this.floor = floor;
        this.cabinet = cabinet;
        this.letter = letter;
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

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_done() {
        return date_done;
    }

    public void setDate_done(String date_done) {
        this.date_done = date_done;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    public String getEmail_creator() {
        return email_creator;
    }

    public void setEmail_creator(String email_creator) {
        this.email_creator = email_creator;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFull_name_executor() {
        return full_name_executor;
    }

    public void setFull_name_executor(String full_name_executor) {
        this.full_name_executor = full_name_executor;
    }

    public String getName_creator() {
        return name_creator;
    }

    public void setName_creator(String name_creator) {
        this.name_creator = name_creator;
    }
}
