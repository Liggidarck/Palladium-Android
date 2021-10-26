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
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email_login_text_layout, password_login_text_layout;
    Button btn_login;
    LinearProgressIndicator progress_bar_auth;
    CoordinatorLayout coordinator_login;

    String email, password, user_id;

    FirebaseAuth firebase_auth;
    FirebaseFirestore firebase_firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        email_login_text_layout = findViewById(R.id.email_login_text_layout);
        password_login_text_layout = findViewById(R.id.password_login_text_layout);
        progress_bar_auth = findViewById(R.id.progress_bar_auth);
        coordinator_login = findViewById(R.id.coordinator_login_activity);

        firebase_auth = FirebaseAuth.getInstance();
        firebase_firestore = FirebaseFirestore.getInstance();

        btn_login.setOnClickListener(v -> {
            email = Objects.requireNonNull(email_login_text_layout.getEditText()).getText().toString();
            password = Objects.requireNonNull(password_login_text_layout.getEditText()).getText().toString();
            progress_bar_auth.setVisibility(View.VISIBLE);

            if(isOnline()) {

                if (validateFields()) {

                    if(!validateEmail(email)) {

                        Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                        email_login_text_layout.setError("Не корректный формат e-mail");

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
                                    progress_bar_auth.setVisibility(View.INVISIBLE);
                                });

                            }

                        }).addOnFailureListener(e -> {
                            progress_bar_auth.setVisibility(View.INVISIBLE);
                            Snackbar.make(coordinator_login, e.toString(), Snackbar.LENGTH_LONG).show();
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

        utils.clear_error(email_login_text_layout);
        utils.clear_error(password_login_text_layout);

        boolean checkEmail = utils.validate_field(email, email_login_text_layout);
        boolean checkPassword = utils.validate_field(password, password_login_text_layout);
        return checkEmail & checkPassword;
    }

}