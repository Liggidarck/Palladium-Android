package com.george.vector.ui.common.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityLoginBinding;
import com.george.vector.network.model.user.Role;
import com.george.vector.network.model.user.User;
import com.george.vector.network.request.LoginRequest;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.george.vector.ui.viewmodel.AuthViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserDataViewModel userDataViewModel;

    private TextValidatorUtils textValidator = new TextValidatorUtils();
    private NetworkUtils networkUtils = new NetworkUtils();

    public static final String TAG = LoadingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {

            String username = Objects.requireNonNull(binding.textUsername.getEditText()).getText().toString();
            String password = Objects.requireNonNull(binding.textPassword.getEditText()).getText().toString();

            LoginRequest loginRequest = new LoginRequest(username, password);

            if (!networkUtils.isOnline(this)) {
                onStart();
                return;
            }

            if (!validateFields()) {
                return;
            }

            login(loginRequest);
        });
    }

    void login(LoginRequest loginRequest) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Идет поиск пользователя...");
        progressDialog.show();



        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.login(loginRequest).observe(this, response -> {

            Log.d(TAG, "login: " + response.getToken());
            Log.d(TAG, "login: " + response.getId());

            String token = response.getToken();
            long id = response.getId();

            String username = response.getUsername();

            String name = response.getName();
            String lastName = response.getLastName();
            String patronymic = response.getPatronymic();
            String email = response.getEmail();
            String zone = response.getZone();

            List<String> role = response.getRoles();

            List<Role> roleList = new ArrayList<>();
            roleList.add(new Role(0, role.get(0)));

            Log.d(TAG, "login: " + roleList.get(0).getName());

            userDataViewModel.saveToken(token);
            userDataViewModel.saveId(id);
            userDataViewModel.saveUser(new User(zone, name, lastName, patronymic,
                    email, loginRequest.getPassword(), username, roleList));

            progressDialog.dismiss();
            startApp(role.get(0));
        });

    }

    void startApp(@NotNull String role) {
        if (role.equals("ROLE_DEVELOPER"))
            startActivity(new Intent(this, MainRootActivity.class));

        if (role.equals("Пользователь"))
            startActivity(new Intent(this, MainUserActivity.class));

        if (role.equals("Исполнитель"))
            startActivity(new Intent(this, MainExecutorActivity.class));

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!networkUtils.isOnline(LoginActivity.this))
            Snackbar.make(findViewById(R.id.coordinator_login_activity),
                    getString(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
    }

    boolean validateFields() {
        String email = binding.textUsername.getEditText().getText().toString();
        String password = binding.textPassword.getEditText().getText().toString();
        return textValidator.isEmptyField(email, binding.textUsername) &
                textValidator.isEmptyField(password, binding.textPassword);
    }

}