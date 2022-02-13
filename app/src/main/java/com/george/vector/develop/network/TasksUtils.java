package com.george.vector.develop.network;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TasksUtils {

    public void save(String collection, Task task) {
        CollectionReference taskReference = FirebaseFirestore.getInstance().collection(collection);
        taskReference.add(task);
    }

    public void deleteTask(String collection, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        documentReferenceTask.delete();
    }

}
