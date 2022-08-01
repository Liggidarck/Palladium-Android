package com.george.vector.ui.auth;

import static com.george.vector.common.utils.TextValidatorUtils.validateEmail;
import static com.george.vector.common.utils.consts.Logs.TAG_VALIDATE_FILED;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.ActivityLoginBinding;
import com.george.vector.network.model.User;
import com.george.vector.network.viewmodel.UserViewModel;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    UserPreferencesViewModel userPreferencesViewModel;
    UserViewModel userViewModel;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    TextValidatorUtils textValidator = new TextValidatorUtils();
    NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPreferencesViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.btnLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.emailLoginTextLayout.getEditText()).getText().toString();
            String password = Objects.requireNonNull(binding.passwordLoginTextLayout.getEditText()).getText().toString();

            if (!networkUtils.isOnline(this)) {
                onStart();
                return;
            }

            if (!validateFields()) {
                Log.e(TAG_VALIDATE_FILED, "Fields empty");
                return;
            }

            if (!validateEmail(email)) {
                Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                binding.emailLoginTextLayout.setError("Некорректный формат e-mail");
            }

            login(email, password);

        });
    }

    void login(String login, String password) {
        binding.progressBarAuth.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(authResultTask -> {
            if (authResultTask.isSuccessful()) {
                String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                userViewModel.getUser(userId).observe(LoginActivity.this, user -> {
                    String name = user.getName();
                    String lastName = user.getLast_name();
                    String patronymic = user.getPatronymic();
                    String role = user.getRole();
                    String email = user.getEmail();
                    String permission = user.getPermission();

                    userPreferencesViewModel.saveUser(new User(name, lastName, patronymic,
                            email, role, permission, password));

                    binding.progressBarAuth.setVisibility(View.INVISIBLE);
                    startApp(role);
                });
            }
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
        String email = binding.emailLoginTextLayout.getEditText().getText().toString();
        String password = binding.passwordLoginTextLayout.getEditText().getText().toString();
        return textValidator.isEmptyField(email, binding.emailLoginTextLayout) &
                textValidator.isEmptyField(password, binding.passwordLoginTextLayout);
    }

}