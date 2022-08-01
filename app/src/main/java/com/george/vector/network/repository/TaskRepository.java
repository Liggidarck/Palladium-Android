package com.george.vector.network.repository;

import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {

    final CollectionReference collectionReference;
    DocumentReference documentReference;
    String collection;

    public TaskRepository(String collection) {
        collectionReference = FirebaseFirestore.getInstance().collection(collection);
        this.collection = collection;
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

    public MutableLiveData<Task> getTask(String id) {
        MutableLiveData<Task> taskMutable = new MutableLiveData<>();
        documentReference = FirebaseFirestore.getInstance().collection(collection).document(id);
        documentReference.addSnapshotListener((value, error) -> {
            String address = value.getString("address");
            String floor = value.getString("floor");
            String cabinet = value.getString("cabinet");
            String letter = value.getString("litera");
            String nameTask = value.getString("name_task");
            String comment = value.getString("comment");
            String status = value.getString("status");
            String dateCreate = value.getString("date_create");
            String timeCreate = value.getString("time_create");
            String imageId = value.getString("image");
            String emailCreator = value.getString("email_creator");
            String emailExecutor = value.getString("executor");
            String dateDone = value.getString("date_done");
            String fullNameExecutor = value.getString("fullNameExecutor");
            String fullNameCreator = value.getString("nameCreator");
            boolean urgent = value.getBoolean("urgent");
            Task task = new Task(nameTask, address, dateCreate, floor,
                    cabinet, letter, comment, dateDone, emailExecutor, status,
                    timeCreate, emailCreator, urgent, imageId, fullNameExecutor, fullNameCreator);

            taskMutable.setValue(task);
        });

        return taskMutable;
    }

}
