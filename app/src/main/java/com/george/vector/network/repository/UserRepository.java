package com.george.vector.network.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.api.FluffyFoxyClient;
import com.george.vector.network.api.UserInterface;
import com.george.vector.network.model.user.RegisterUserModel;
import com.george.vector.network.model.user.Role;
import com.george.vector.network.model.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserInterface userInterface;

    public static final String TAG = UserRepository.class.getSimpleName();

    public UserRepository(String token) {
        userInterface = FluffyFoxyClient.getFoxyTokenClient(token).create(UserInterface.class);
    }

    public MutableLiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        userInterface.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                Log.d(TAG, "getAllUsers: " + response.code());
                if (response.code() == 200) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                users.setValue(null);
                Log.e(TAG, "getAllUsers: ", t);
            }
        });

        return users;
    }

    public MutableLiveData<List<Role>> getAllRoles() {
        MutableLiveData<List<Role>> roles = new MutableLiveData<>();

        userInterface.getAllRoles().enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(@NonNull Call<List<Role>> call, @NonNull Response<List<Role>> response) {
                Log.d(TAG, "getAllRoles: " + response.code());
                if (response.code() == 200) {
                    roles.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Role>> call, @NonNull Throwable t) {
                roles.setValue(null);
                Log.e(TAG, "getAllRoles: ", t);
            }
        });

        return roles;
    }

    public MutableLiveData<List<User>> getUsersByRoleName(String role) {
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        userInterface.getUsersByRoleName(role).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                Log.d(TAG, "getUsersByRoleName: " + response.code());
                if (response.code() == 200) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                users.setValue(null);
                Log.e(TAG, "getUsersByRoleName: ", t);
            }
        });

        return users;
    }

    public MutableLiveData<User> getUserById(long id) {
        MutableLiveData<User> user = new MutableLiveData<>();

        userInterface.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                Log.d(TAG, "getUserById: " + response.code());
                if (response.code() == 200) {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                user.setValue(null);
                Log.e(TAG, "getUserById: ", t);
            }
        });

        return user;
    }

    public MutableLiveData<String> editUser(RegisterUserModel user, long id) {
        MutableLiveData<String> edit = new MutableLiveData<>();

        userInterface.editUser(user, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.d(TAG, "editUser: " + response.code());
                if(response.code() == 200) {
                    edit.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, "editUser: ", t);
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
                Log.d(TAG, "deleteUser: " + response.code());
                if(response.code() == 200) {
                    delete.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                delete.setValue(null);
                Log.e(TAG, "deleteUser: ", t);
            }
        });

        return delete;
    }


}
