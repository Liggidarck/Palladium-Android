package com.george.vector.ui.viewmodel;

import static com.george.vector.common.utils.consts.Keys.USERS;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.User;
import com.george.vector.network.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(USERS);
    }

    public MutableLiveData<User> getUser(String userId) {
        return repository.getUser(userId);
    }

    public void updateUser(String id, User user) {
        repository.updateUser(id, user);
    }

    public void saveUser(User user) {
        repository.saveUser(user);
    }

}
