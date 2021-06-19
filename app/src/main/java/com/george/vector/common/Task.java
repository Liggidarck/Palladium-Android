package com.george.vector.common;

import com.google.firebase.firestore.DocumentChange;

public class Task {

    private String title; // Название заявки
    private String description; // Адрес
    private String priority; // Дата создания заявки


    private String floor;
    private String cabinet;
    private String comment;
    private String date_done;
    private String executor;
    private String status;

    private String time_priority;
    private String email_creator;

    private String uri_image;

    public Task() {}

    public Task(String name_task, String description, String priority, String floor,
                String cabinet, String comment, String date_done, String executor, String status,
                String time_priority, String email_creator, String uri_image) {
        this.title = name_task;
        this.description = description;
        this.priority = priority;

        this.floor = floor;
        this.cabinet = cabinet;
        this.comment = comment;
        this.date_done = date_done;
        this.executor = executor;
        this.status = status;

        this.time_priority = time_priority;
        this.email_creator = email_creator;

        this.uri_image = uri_image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
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

    public String getTime_priority() {
        return time_priority;
    }

    public String getEmail_creator() {
        return email_creator;
    }

    public String getUri_image() {
        return uri_image;
    }
}