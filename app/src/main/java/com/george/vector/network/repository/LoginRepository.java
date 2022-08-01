package com.george.vector.network.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

public class LoginRepository {

    FirebaseAuth firebaseAuth;

    public LoginRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public MutableLiveData<String> signIn(String email, String password) {
        MutableLiveData<String> id = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            String userId = firebaseAuth.getCurrentUser().getUid();
            id.setValue(userId);
        });
        return id;
    }

}
