package com.george.vector.common.tasks.utils;

import android.util.Log;

import com.george.vector.common.tasks.ui.TaskUi;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class SaveTask implements TaskBehavior{

    private static final String TAG = "SaveTaskUser";

    @Override
    public void initialize_task(@NotNull String location, String name_task, String address,
                                String dateText, String floor, String cabinet, String comment,
                                String date_done, String executor, String status, String timeText,
                                String email, String uri_image) {

        if(location.equals("ost_school")) {
            switch (status){
                case "Новая заявка":
                    save("ost_school_new", name_task, address,
                            dateText, floor, cabinet, comment, date_done, executor, status,
                            timeText, email, uri_image);
                    break;
                case "В работе":
                    save("ost_school_progress", name_task, address,
                            dateText, floor, cabinet, comment, date_done, executor, status,
                            timeText, email, uri_image);
                    break;
                case "Архив":
                    save("ost_school_archive", name_task, address,
                            dateText, floor, cabinet, comment, date_done, executor, status,
                            timeText, email, uri_image);
                    break;
            }
        }

        if(location.equals("bar_school")) {
            switch (status) {
                case "Новая заявка":
                    save("bar_school_new", name_task, address,
                            dateText, floor, cabinet, comment, date_done, executor, status,
                            timeText, email, uri_image);
                    break;
                case "В работе":
                    save("bar_school_progress", name_task, address,
                            dateText, floor, cabinet, comment, date_done, executor, status,
                            timeText, email, uri_image);
                    break;
                case "Архив":
                    save("ost_school_archive", name_task, address,
                            dateText, floor, cabinet, comment, date_done, executor, status,
                            timeText, email, uri_image);
                    break;
            }
        }
    }

    void save(String collection, String name_task, String address, String dateText,
              String floor, String cabinet, String comment, String date_done, String executor,
              String status, String timeText, String email, String uri_image) {

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        taskRef.add(new TaskUi(name_task, address, dateText, floor, cabinet, comment, date_done,
                executor, status, timeText, email, uri_image));

        taskRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");
            } else {
                Log.e(TAG, "Error: " + task.getException());
            }
        });

    }

}