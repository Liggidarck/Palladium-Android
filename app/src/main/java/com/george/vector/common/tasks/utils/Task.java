package com.george.vector.common.tasks.utils;

public class Task {

    public void save(TaskBehavior taskBehavior, String location, String name_task, String address, String dateText, String floor,
                     String cabinet, String litera, String comment, String date_done, String executor, String status, String timeText,
                     String email, boolean urgent, String image) {

        taskBehavior.initialize_task(location, name_task, address, dateText, floor, cabinet, litera, comment, date_done, executor, status, timeText, email, urgent, image);

    }

}
