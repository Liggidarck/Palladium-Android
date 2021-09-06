package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootFutureMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ActivityLogin extends AppCompatActivity {

    TextInputLayout email_login_text_layout, password_login_text_layout;
    Button btn_login, btn_forgot_password;
    LinearProgressIndicator progress_bar_auth;
    CoordinatorLayout coordinator_login;

    String emailED, passwordED, userID;
    private static final String TAG = "LoginActivity";

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        email_login_text_layout = findViewById(R.id.email_login_text_layout);
        password_login_text_layout = findViewById(R.id.password_login_text_layout);
        progress_bar_auth = findViewById(R.id.progress_bar_auth);
        coordinator_login = findViewById(R.id.coordinator_login_activity);
        btn_forgot_password = findViewById(R.id.btn_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btn_login.setOnClickListener(v -> {
            emailED = Objects.requireNonNull(email_login_text_layout.getEditText()).getText().toString();
            passwordED = Objects.requireNonNull(password_login_text_layout.getEditText()).getText().toString();

            if(isOnline()) {
                if (validateFields()) {
                    progress_bar_auth.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(emailED, passwordED).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            progress_bar_auth.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "Login success");

                            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                            DocumentReference documentReference = firebaseFirestore.collection((String) getText(R.string.users)).document(userID);
                            documentReference.addSnapshotListener(this, (value, error) -> {
                                assert value != null;

                                String check_role = value.getString((String) getText(R.string.role));
                                String check_email = value.getString(getString(R.string.email));
                                String permission = value.getString(getString(R.string.permission));
                                Log.d(TAG, String.format("permission - %s", permission));

                                assert check_role != null;
                                startApp(check_role, check_email, permission);

                            });

                        } else {
                            progress_bar_auth.setVisibility(View.INVISIBLE);
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            assert error != null;
                            Snackbar.make(coordinator_login, error, Snackbar.LENGTH_LONG).show();
                        }

                    });
                }
            } else
                onStart();

        });

        btn_forgot_password.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));

    }

    void startApp(@NotNull String role, String email, String permission) {
        if (role.equals("Root")) {
            Intent intent = new Intent(this, RootFutureMainActivity.class);
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        }

        if (role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra((String) getText(R.string.email), email);
            intent.putExtra((String) getText(R.string.permission), permission);
            startActivity(intent);
        }

        if (role.equals("Исполнитель")) {
            Intent intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra((String) getText(R.string.email), email);
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
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(email_login_text_layout);
        utils.clear_error(password_login_text_layout);

        boolean checkEmail = utils.validate_field(emailED, email_login_text_layout);
        boolean checkPassword = utils.validate_field(passwordED, password_login_text_layout);
        return checkEmail & checkPassword;
    }

}