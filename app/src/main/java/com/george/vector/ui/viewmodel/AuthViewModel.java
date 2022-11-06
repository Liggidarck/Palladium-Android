package com.george.vector.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.Message;
import com.george.vector.network.model.user.RegisterUserModel;
import com.george.vector.network.repository.AuthRepository;
import com.george.vector.network.request.LoginRequest;
import com.george.vector.network.responce.LoginResponse;

public class AuthViewModel extends AndroidViewModel {

    AuthRepository repository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository();
    }

    public MutableLiveData<LoginResponse> login(LoginRequest loginRequest) {
        return repository.login(loginRequest);
    }

    public MutableLiveData<Message> register(RegisterUserModel user) {
        return repository.register(user);
    }

}
