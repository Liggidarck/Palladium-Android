package com.george.vector.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    LoginRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository();
    }

    public MutableLiveData<String> signIn(String  email, String password) {
        return repository.signIn(email, password);
    }

    public MutableLiveData<String> getUserId() {
        return repository.getUserId();
    }

}
