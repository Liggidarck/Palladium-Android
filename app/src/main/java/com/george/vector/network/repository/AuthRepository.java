package com.george.vector.network.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.api.FluffyFoxyAuthClient;
import com.george.vector.network.api.AuthInterface;
import com.george.vector.network.model.Message;
import com.george.vector.network.model.user.RegisterUserModel;
import com.george.vector.network.request.LoginRequest;
import com.george.vector.network.responce.LoginResponse;

import java.nio.file.Path;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    AuthInterface authInterface;

    public static final String TAG = AuthRepository.class.getSimpleName();

    public AuthRepository() {
        authInterface = FluffyFoxyAuthClient.getAuthClient().create(AuthInterface.class);
    }

    public MutableLiveData<LoginResponse> login(LoginRequest loginRequest) {
        MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();

        authInterface.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.d(TAG, "login code: " + response.code());

                if (response.code() == 200) {
                    loginResponseMutableLiveData.setValue(response.body());
                } else {
                    loginResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginResponseMutableLiveData.postValue(null);
                Log.e(TAG, "onFailure: auth: ", t);
            }
        });

        return loginResponseMutableLiveData;
    }

    public MutableLiveData<Message> register(RegisterUserModel user) {
        MutableLiveData<Message> status = new MutableLiveData<>();

        authInterface.register(user).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: code: " + response.code());
                if (response.code() == 200) {
                    status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                status.postValue(null);
                Log.e(TAG, "onFailure: register: ", t);
            }
        });

        return status;
    }

}
