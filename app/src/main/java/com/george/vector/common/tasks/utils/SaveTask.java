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
                                String date_create, String floor, String cabinet, String litera, String comment,
                                String date_complete, String executor, String status, String time_create,
                                String email, boolean urgent) {

        if(location.equals("ost_school")) {
            switch (status){
                case "Новая заявка":
                    save("ost_school_new", name_task, address,
                            date_create, floor, cabinet, litera, comment, date_complete, executor, status,
                            time_create, email, urgent);
                    break;
                case "В работе":
                    save("ost_school_progress", name_task, address,
                            date_create, floor, cabinet, litera, comment, date_complete, executor, status,
                            time_create, email, urgent);
                    break;
                case "Архив":
                    save("ost_school_archive", name_task, address,
                            date_create, floor, cabinet, litera, comment, date_complete, executor, status,
                            time_create, email, urgent);
                    break;
            }
        }

        if(location.equals("bar_school")) {
            switch (status) {
                case "Новая заявка":
                    save("bar_school_new", name_task, address,
                            date_create, floor, cabinet, litera, comment, date_complete, executor, status,
                            time_create, email, urgent);
                    break;
                case "В работе":
                    save("bar_school_progress", name_task, address,
                            date_create, floor, cabinet, litera, comment, date_complete, executor, status,
                            time_create, email, urgent);
                    break;
                case "Архив":
                    save("bar_school_archive", name_task, address,
                            date_create, floor, cabinet, litera, comment, date_complete, executor, status,
                            time_create, email, urgent);
                    break;
            }
        }
    }

    void save(String collection, String name_task, String address, String date_create,
              String floor, String cabinet, String litera, String comment, String date_complete, String executor,
              String status, String time_create, String email, boolean urgent) {

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        taskRef.add(new TaskUi(name_task, address, date_create, floor, cabinet, litera, comment, date_complete,
                executor, status, time_create, email, urgent));

        taskRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");
            } else {
                Log.e(TAG, "Error: " + task.getException());
            }
        });

    }

}