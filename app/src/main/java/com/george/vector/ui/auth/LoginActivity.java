package com.george.vector.ui.auth;

import static com.george.vector.common.utils.TextValidatorUtils.validateEmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityLoginBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.george.vector.ui.viewmodel.LoginViewModel;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    UserDataViewModel userDataViewModel;
    UserViewModel userViewModel;

    TextValidatorUtils textValidator = new TextValidatorUtils();
    NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.textEmail.getEditText()).getText().toString();
            String password = Objects.requireNonNull(binding.textPassword.getEditText()).getText().toString();

            if (!networkUtils.isOnline(this)) {
                onStart();
                return;
            }

            if (!validateFields()) {
                return;
            }

            if (validateEmail(email)) {
                binding.textEmail.setError("Некорректный формат e-mail");
                return;
            }

            signIn(email, password);

        });
    }

    void signIn(String login, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Идет поиск пользователя...");
        progressDialog.show();

        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.signIn(login, password).observe(this, id -> {
            if (id.equals("error")) {
                Snackbar.make(binding.coordinatorLoginActivity, "Ошибка. Пользователь не найден",
                                Snackbar.LENGTH_LONG)
                        .show();
                progressDialog.dismiss();
                return;
            }

            userViewModel.getUser(id).observe(LoginActivity.this, user -> {
                String name = user.getName();
                String lastName = user.getLast_name();
                String patronymic = user.getPatronymic();
                String role = user.getRole();
                String email = user.getEmail();
                String permission = user.getPermission();

                userDataViewModel.saveUser(new User(name, lastName, patronymic,
                        email, role, permission, password));

                progressDialog.dismiss();
                startApp(role);
            });
        });
    }

    void startApp(@NotNull String role) {
        if (role.equals("Root"))
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
        String email = binding.textEmail.getEditText().getText().toString();
        String password = binding.textPassword.getEditText().getText().toString();
        return textValidator.isEmptyField(email, binding.textEmail) &
                textValidator.isEmptyField(password, binding.textPassword);
    }

}