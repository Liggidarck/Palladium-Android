package com.george.vector.auth;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.ROLE;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Logs.TAG_LOGIN_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_VALIDATE_FILED;
import static com.george.vector.common.utils.Utils.validateEmail;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityLoginBinding;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    String email, password, user_id;

    FirebaseAuth firebase_auth;
    FirebaseFirestore firebase_firestore;

    ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginActivity);
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        firebase_auth = FirebaseAuth.getInstance();
        firebase_firestore = FirebaseFirestore.getInstance();

        loginBinding.btnLogin.setOnClickListener(v -> {
            email = Objects.requireNonNull(loginBinding.emailLoginTextLayout.getEditText()).getText().toString();
            password = Objects.requireNonNull(loginBinding.passwordLoginTextLayout.getEditText()).getText().toString();
            loginBinding.progressBarAuth.setVisibility(View.VISIBLE);

            if(isOnline()) {

                if (validateFields()) {

                    if(!validateEmail(email)) {
                        Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                        loginBinding.emailLoginTextLayout.setError("Некорректный формат e-mail");
                    } else {

                        firebase_auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                Log.d(TAG_LOGIN_ACTIVITY, "Login success");

                                user_id = Objects.requireNonNull(firebase_auth.getCurrentUser()).getUid();

                                DocumentReference documentReference = firebase_firestore.collection(USERS).document(user_id);
                                documentReference.addSnapshotListener(this, (value, error) -> {
                                    assert value != null;

                                    String name = value.getString("name");
                                    String last_name = value.getString("last_name");
                                    String patronymic = value.getString("patronymic");
                                    String role = value.getString(ROLE);
                                    String email = value.getString(EMAIL);
                                    String permission = value.getString(PERMISSION);
                                    Log.d(TAG_LOGIN_ACTIVITY, String.format("permission - %s", permission));
                                    Log.d(TAG_LOGIN_ACTIVITY, String.format("role - %s", role));
                                    Log.d(TAG_LOGIN_ACTIVITY, String.format("email - %s", email));

                                    assert role != null;
                                    startApp(name, last_name, patronymic, role, email, permission);
                                    loginBinding.progressBarAuth.setVisibility(View.INVISIBLE);
                                });

                            }

                        }).addOnFailureListener(e -> {
                            loginBinding.progressBarAuth.setVisibility(View.INVISIBLE);
                            Snackbar.make(loginBinding.coordinatorLoginActivity, e.toString(), Snackbar.LENGTH_LONG).show();
                        });


                    }


                }

            } else
                onStart();

        });


    }

    void startApp(@NotNull String name, String last_name, String patronymic, String role, String email, String permission) {
        if (role.equals("Root")) {
            Intent intent = new Intent(this, RootMainActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        }

        if (role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);
        }

        if (role.equals("Исполнитель")) {
            Intent intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        }
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

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_login_activity), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG_LOGIN_ACTIVITY, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(loginBinding.emailLoginTextLayout);
        utils.clear_error(loginBinding.passwordLoginTextLayout);

        boolean checkEmail = utils.validate_field(email, loginBinding.emailLoginTextLayout);
        boolean checkPassword = utils.validate_field(password, loginBinding.passwordLoginTextLayout);
        return checkEmail & checkPassword;
    }

}