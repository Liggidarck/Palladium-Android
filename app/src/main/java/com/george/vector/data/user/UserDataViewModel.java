package com.george.vector.data.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.george.vector.network.model.User;

public class UserDataViewModel extends AndroidViewModel {

    private final UserPreferencesRepository repository;

    public UserDataViewModel(@NonNull Application application) {
        super(application);
        repository = new UserPreferencesRepository(application);
    }

    public void saveId(long id) {
        repository.saveId(id);
    }

    public long getId() {
        return repository.getId();
    }

    public void saveUser(User user) {
        repository.saveUser(user);
    }

    public User getUser() {
        return repository.getUser();
    }

    public void saveToken(String token) {
        repository.saveToken(token);
    }

    public String getToken() {
        return repository.getToken();
    }

    public boolean getNotifications() {
        return repository.getNotifications();
    }

    public void setNotifications(boolean notifications) {
        repository.setNotifications(notifications);
    }

}
