package com.george.vector.common.tasks.utils;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteTask{

    public void delete_task(String collection, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();;
        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        documentReferenceTask.delete();
    }
}
