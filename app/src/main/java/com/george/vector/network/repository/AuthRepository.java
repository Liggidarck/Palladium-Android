package com.george.vector.network.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.vector.network.api.FluffyFoxyAuthClient;
import com.george.vector.network.api.AuthInterface;
import com.george.vector.network.model.User;
import com.george.vector.network.request.LoginRequest;
import com.george.vector.network.responce.LoginResponse;

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
                Log.d(TAG, "onResponse: " + response.code());

                if(response.code() == 200) {
                    loginResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginResponseMutableLiveData.postValue(null);
            }
        });

        return loginResponseMutableLiveData;
    }

    public MutableLiveData<String> register(User user) {
        MutableLiveData<String> status = new MutableLiveData<>();

        authInterface.register(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.code() == 200) {
                    status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                status.postValue(null);
            }
        });

        return status;
    }

}
