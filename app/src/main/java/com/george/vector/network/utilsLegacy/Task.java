package com.george.vector.network.utilsLegacy;

public class Task {

    public void save(TaskBehavior taskBehavior, String location, String name_task, String address, String dateText, String floor,
                     String cabinet, String letter, String comment, String date_done, String executor, String status, String timeText,
                     String email, boolean urgent, String image, String full_name_executor, String name_creator) {

        taskBehavior.initialize_task(location, name_task, address, dateText, floor, cabinet, letter,
                comment, date_done, executor, status, timeText, email, urgent, image, full_name_executor, name_creator);

    }

}
