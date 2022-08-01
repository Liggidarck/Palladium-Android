package com.george.vector.data.preferences;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.george.vector.network.model.User;

public class UserPreferencesViewModel extends AndroidViewModel {

    UserPreferencesRepository repository;

    public UserPreferencesViewModel(@NonNull Application application) {
        super(application);
        repository = new UserPreferencesRepository(application);
    }

    public void saveUser(User user) {
        repository.saveUser(user);
    }

    public User getUser() {
        return repository.getUser();
    }

    public boolean getNotifications() {
        return repository.getNotifications();
    }

    public void setNotifications(boolean notifications) {
        repository.setNotifications(notifications);
    }

}
