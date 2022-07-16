package com.george.vector.network.utilsLegacy;

import android.util.Log;

import com.george.vector.network.model.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class SaveTask implements TaskBehavior{

    private static final String TAG = "SaveTaskUser";

    @Override
    public void initialize_task(@NotNull String location, String name_task, String address,
                                String date_create, String floor, String cabinet, String letter, String comment,
                                String date_complete, String executor, String status, String time_create,
                                String email, boolean urgent, String image, String full_name_executor, String name_creator) {

        if(location.equals("ost_school")) {
            switch (status){
                case "Новая заявка":
                    save("ost_school_new", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
                case "В работе":
                    save("ost_school_progress", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
                case "Завершенная заявка":
                    save("ost_school_completed", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
                case "Архив":
                    save("ost_school_archive", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
            }
        }

        if(location.equals("bar_school")) {
            switch (status) {
                case "Новая заявка":
                    save("bar_school_new", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
                case "В работе":
                    save("bar_school_progress", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
                case "Завершенная заявка":
                    save("bar_school_completed", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
                case "Архив":
                    save("bar_school_archive", name_task, address,
                            date_create, floor, cabinet, letter, comment, date_complete, executor, status,
                            time_create, email, urgent, image, full_name_executor, name_creator);
                    break;
            }
        }
    }

    void save(String collection, String name_task, String address, String date_create,
              String floor, String cabinet, String letter, String comment, String date_complete, String executor,
              String status, String time_create, String email, boolean urgent, String image, String full_name_executor, String name_creator) {

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        taskRef.add(new Task(name_task, address, date_create, floor, cabinet, letter, comment, date_complete,
                executor, status, time_create, email, urgent, image, full_name_executor, name_creator));

        taskRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");
            } else {
                Log.e(TAG, "Error: " + task.getException());
            }
        });

    }

}