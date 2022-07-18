package com.george.vector.network.repository;

import com.george.vector.network.model.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {

    CollectionReference collectionReference;

    public TaskRepository(String collection) {
        collectionReference = FirebaseFirestore.getInstance().collection(collection);
    }

    public void createTask(Task task) {
        collectionReference.add(task);
    }

    public void updateTask(String id, Task task) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("name_task", task.getName_task());
        taskMap.put("address", task.getAddress());
        taskMap.put("date_create", task.getDate_create());
        taskMap.put("floor", task.getFloor());
        taskMap.put("cabinet", task.getCabinet());
        taskMap.put("litera", task.getLitera());
        taskMap.put("comment", task.getComment());
        taskMap.put("date_done", task.getDate_done());
        taskMap.put("executor", task.getExecutor());
        taskMap.put("status", task.getStatus());
        taskMap.put("urgent", task.getUrgent());
        taskMap.put("time_create", task.getTime_create());
        taskMap.put("email_creator", task.getEmail_creator());
        taskMap.put("image", task.getImage());
        taskMap.put("fullNameExecutor", task.getFullNameExecutor());
        taskMap.put("name_creator", task.getNameCreator());
        collectionReference
                .document(id)
                .update(taskMap);
    }

    public void deleteTask(String id) {
        collectionReference.document(id).delete();
    }

}
