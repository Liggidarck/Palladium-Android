package com.george.vector.common.tasks.utils;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteTask{

    public void delete_task(String collection, String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        documentReferenceTask.delete();
    }
}
