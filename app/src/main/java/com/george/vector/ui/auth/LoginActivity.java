package com.george.vector.ui.auth;


import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.ROLE;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Keys.USER_NOTIFICATIONS_OPTIONS;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_ROLE;
import static com.george.vector.common.consts.Logs.TAG_LOGIN_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_VALIDATE_FILED;
import static com.george.vector.common.utils.Utils.validateEmail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityLoginBinding;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    SharedPreferences sharedPreferences;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String email, password, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        loginBinding.btnLogin.setOnClickListener(v -> {
            email = Objects.requireNonNull(loginBinding.emailLoginTextLayout.getEditText()).getText().toString();
            password = Objects.requireNonNull(loginBinding.passwordLoginTextLayout.getEditText()).getText().toString();

            if (!isOnline()) {
                onStart();
                return;
            }

            if (!validateFields()) {
                Log.e(TAG_VALIDATE_FILED, "Fields empty");
                return;
            }

            if (!validateEmail(email)) {
                Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                loginBinding.emailLoginTextLayout.setError("Некорректный формат e-mail");
            }

            login(email, password);

        });
    }

    void login(String login, String password) {
        loginBinding.progressBarAuth.setVisibility(View.VISIBLE);
        sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        firebaseAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG_LOGIN_ACTIVITY, "Login success");

                userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                DocumentReference documentReference = firebaseFirestore.collection(USERS).document(userId);
                documentReference.addSnapshotListener(this, (value, error) -> {
                    assert value != null;

                    String name = value.getString("name");
                    String last_name = value.getString("last_name");
                    String patronymic = value.getString("patronymic");
                    String role = value.getString(ROLE);
                    String email = value.getString(EMAIL);
                    String permission = value.getString(PERMISSION);

                    editor.putString(USER_PREFERENCES_NAME, name);
                    editor.putString(USER_PREFERENCES_LAST_NAME, last_name);
                    editor.putString(USER_PREFERENCES_PATRONYMIC, patronymic);
                    editor.putString(USER_PREFERENCES_EMAIL, email);
                    editor.putString(USER_PREFERENCES_ROLE, role);
                    editor.putString(USER_PREFERENCES_PERMISSION, permission);
                    editor.putBoolean(USER_NOTIFICATIONS_OPTIONS, false);

                    editor.apply();

                    assert role != null;
                    startApp(role);
                    loginBinding.progressBarAuth.setVisibility(View.INVISIBLE);
                });

            }

        }).addOnFailureListener(e -> {
            loginBinding.progressBarAuth.setVisibility(View.INVISIBLE);
            Snackbar.make(loginBinding.coordinatorLoginActivity, e.toString(), Snackbar.LENGTH_LONG).show();
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

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_login_activity), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        Log.i(TAG_LOGIN_ACTIVITY, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clearError(loginBinding.emailLoginTextLayout);
        utils.clearError(loginBinding.passwordLoginTextLayout);

        boolean checkEmail = utils.validateField(email, loginBinding.emailLoginTextLayout);
        boolean checkPassword = utils.validateField(password, loginBinding.passwordLoginTextLayout);
        return checkEmail & checkPassword;
    }

}