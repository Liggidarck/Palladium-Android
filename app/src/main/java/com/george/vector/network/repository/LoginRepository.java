package com.george.vector.network.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

public class LoginRepository {

    FirebaseAuth firebaseAuth;

    public LoginRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public MutableLiveData<String > getUserId() {
        MutableLiveData<String> userId = new MutableLiveData<>();
        userId.setValue(firebaseAuth.getCurrentUser().getUid());
        return userId;
    }

    public MutableLiveData<String> signIn(String email, String password) {
        MutableLiveData<String> id = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(documentReference -> {
            if (documentReference.isSuccessful()) {
                String userId = firebaseAuth.getCurrentUser().getUid();
                id.setValue(userId);
            }
        }).addOnFailureListener(e -> {
            id.setValue("error");
        });
        return id;
    }

}
