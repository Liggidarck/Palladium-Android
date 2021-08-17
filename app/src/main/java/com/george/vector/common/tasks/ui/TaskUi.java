package com.george.vector.common.tasks.ui;

public class TaskUi {

    private String name_task; // Название заявки
    private String address; // Адрес
    private String date_create; // Дата создания заявки
    private String floor;
    private String cabinet;
    private String litera;
    private String comment;
    private String date_done;
    private String executor;
    private String status;

    private String time_create;
    private String email_creator;

    public TaskUi() {}

    public TaskUi(String name_task, String address, String date_create, String floor,
                  String cabinet, String litera, String comment, String date_done, String executor, String status,
                  String time_create, String email_creator) {
        this.name_task = name_task;
        this.address = address;
        this.date_create = date_create;
        this.floor = floor;
        this.cabinet = cabinet;
        this.litera = litera;
        this.comment = comment;
        this.date_done = date_done;
        this.executor = executor;
        this.status = status;

        this.time_create = time_create;
        this.email_creator = email_creator;
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
        return litera;
    }
}