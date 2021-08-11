package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.george.vector.users.admin.MainAdminActivity;
import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.george.vector.users.caretaker.main.MainCaretakerActivity;
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
        coordinator_login = findViewById(R.id.coordinator_login);
        btn_forgot_password = findViewById(R.id.btn_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
                            Log.d(TAG, "permission - " + permission);

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
        btn_forgot_password.setOnClickListener(v -> show_forgot_password_dialog());
    }

    private void show_forgot_password_dialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgot_password_dialog);

        TextInputLayout ti_forgot_password = dialog.findViewById(R.id.ti_forgot_password);
        Button btn_ok_forgot_password = dialog.findViewById(R.id.btn_ok_forgot_password);
        Button cancel_dialog_password = dialog.findViewById(R.id.cancel_dialog_password);

        btn_ok_forgot_password.setOnClickListener(v -> {
            String email = Objects.requireNonNull(ti_forgot_password.getEditText()).getText().toString();

            if(email.isEmpty())
                ti_forgot_password.setError("Это поле не может быть пустым");
            else {
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused -> {
                            Log.i(TAG, "Ссылка для восстановления пароля отправлена");
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "Error! " + e));
            }


        });
        cancel_dialog_password.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    void startApp(@NotNull String role, String email, String permission) {
        if (role.equals("Root"))
            startActivity(new Intent(this, RootMainActivity.class));

        if(role.equals("Завхоз")) {
            Intent intent = new Intent(this, MainCaretakerActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if(role.equals("Администратор")) {
            Intent intent = new Intent(this, MainAdminActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if (role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if (role.equals("Исполнитель")) {
            Intent intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
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