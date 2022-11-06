package com.george.vector.ui.common.help.root.develop_app;

public class Release {

    private final String title;
    private final String date;
    private final String description;

    public Release(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

}
