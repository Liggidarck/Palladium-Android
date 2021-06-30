package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.george.vector.admin.MainAdminActivity;
import com.george.vector.R;
import com.george.vector.common.utils.ErrorsUtils;
import com.george.vector.root.main.RootMainActivity;
import com.george.vector.user.main.MainUserActivity;
import com.george.vector.caretaker.main.MainCaretakerActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ActivityLogin extends AppCompatActivity {

    // Все View элементы
    TextInputLayout email_login_text_layout, password_login_text_layout;
    Button btn_login;
    LinearProgressIndicator progress_bar_auth;
    CoordinatorLayout coordinator_login;

    // Все глобальные переменные
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
        coordinator_login = findViewById(R.id.coordinator_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        boolean check = isOnline();
        Log.d(TAG, "check internet: " + check);

        if(firebaseAuth.getCurrentUser() != null) {
            progress_bar_auth.setVisibility(View.VISIBLE);
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, (value, error) -> {
                assert value != null;

                String check_role = value.getString("role");
                String check_email = value.getString("email");
                String permission = value.getString("permission");
                Log.d(TAG, "ROLE - " + check_role);

                assert check_role != null;
                startApp(check_role, check_email, permission);

            });

        }

        btn_login.setOnClickListener(v -> {
            emailED = Objects.requireNonNull(email_login_text_layout.getEditText()).getText().toString();
            passwordED = Objects.requireNonNull(password_login_text_layout.getEditText()).getText().toString();

            if(validateFields()) {
                progress_bar_auth.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(emailED, passwordED).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        progress_bar_auth.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Login success");

                        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                        documentReference.addSnapshotListener(this, (value, error) -> {
                            assert value != null;

                            String check_role = value.getString("role");
                            String check_email = value.getString("email");
                            String permission = value.getString("permission");
                            Log.d(TAG, "ROLE - " + check_role);

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

        });

        clearErrors();

    }

    void startApp(String check_role,String check_email, String permission) {
        if (check_role.equals("Root"))
            startActivity(new Intent(this, RootMainActivity.class));

        if(check_role.equals("Завхоз")) {
            Intent intent = new Intent(this, MainCaretakerActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if(check_role.equals("Администратор")) {
            Intent intent = new Intent(this, MainAdminActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if (check_role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra("email", check_email);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }
    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

        boolean checkEmail = errorsUtils.validate_field(emailED);
        boolean checkPassword = errorsUtils.validate_field(passwordED);

        Log.i(TAG, "Email: " + checkEmail + " Password: " + checkPassword);

        if(checkEmail & checkPassword)
            return true;
        else {
            if(!checkEmail)
                email_login_text_layout.setError("Это поле не может быть пустым!");

            if(!checkPassword)
                password_login_text_layout.setError("Это поле не может быть пустым!");

            return false;
        }
    }

    void clearErrors() {
        Objects.requireNonNull(email_login_text_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                email_login_text_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(password_login_text_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password_login_text_layout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


}