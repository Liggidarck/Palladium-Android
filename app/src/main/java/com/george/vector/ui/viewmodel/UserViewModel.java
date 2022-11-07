package com.george.vector.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.model.user.RegisterUserModel;
import com.george.vector.network.model.user.Role;
import com.george.vector.network.model.user.User;
import com.george.vector.network.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    public UserViewModel(@NonNull Application application, String token) {
        super(application);
        repository = new UserRepository(token);
    }

    public MutableLiveData<List<User>> getAllUsers() {
        return repository.getAllUsers();
    }

    public MutableLiveData<List<Role>> getAllRoles() {
        return repository.getAllRoles();
    }

    public MutableLiveData<List<User>> getUsersByRoleName(String role) {
        return repository.getUsersByRoleName(role);
    }

    public MutableLiveData<User> getUserById(long id) {
        return repository.getUserById(id);
    }

    public MutableLiveData<String> updateUser(RegisterUserModel user, long id) {
        return repository.editUser(user, id);
    }

    public MutableLiveData<String> deleteUser(long id) {
        return repository.deleteUser(id);
    }

}
