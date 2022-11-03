package com.george.vector.network.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.api.FluffyFoxyClient;
import com.george.vector.network.api.UserInterface;
import com.george.vector.network.model.Role;
import com.george.vector.network.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserInterface userInterface;

    public UserRepository(String token) {
        userInterface = FluffyFoxyClient.getFoxyTokenClient(token).create(UserInterface.class);
    }

    public MutableLiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        userInterface.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.code() == 200) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                users.setValue(null);
            }
        });

        return users;
    }

    public MutableLiveData<List<Role>> getAllRoles() {
        MutableLiveData<List<Role>> roles = new MutableLiveData<>();

        userInterface.getAllRoles().enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(@NonNull Call<List<Role>> call, @NonNull Response<List<Role>> response) {
                if (response.code() == 200) {
                    roles.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Role>> call, @NonNull Throwable t) {
                roles.setValue(null);
            }
        });

        return roles;
    }

    public MutableLiveData<List<User>> getUsersByRoleName(String role) {
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        userInterface.getUsersByRoleName(role).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.code() == 200) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                users.setValue(null);
            }
        });

        return users;
    }

    public MutableLiveData<User> getUserById(long id) {
        MutableLiveData<User> user = new MutableLiveData<>();

        userInterface.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200) {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                user.setValue(null);
            }
        });

        return user;
    }

    public MutableLiveData<String> editUser(User user, long id) {
        MutableLiveData<String> edit = new MutableLiveData<>();

        userInterface.editUser(user, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.code() == 200) {
                    edit.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                edit.setValue(null);
            }
        });

        return edit;
    }

    public MutableLiveData<String> deleteUser(long id) {
        MutableLiveData<String> delete = new MutableLiveData<>();

        userInterface.deleteUser(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.code() == 200) {
                    delete.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                delete.setValue(null);
            }
        });

        return delete;
    }


}
