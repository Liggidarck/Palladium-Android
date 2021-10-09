package com.george.vector.auth;

import static com.george.vector.common.consts.Logs.TAG_FORGOT_PASSWORD_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_VALIDATE_FILED;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_forgot_password;
    Button change_password_btn;
    TextInputLayout email_forgot_password_text;

    String email;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        topAppBar_forgot_password = findViewById(R.id.topAppBar_forgot_password);
        change_password_btn = findViewById(R.id.change_password_btn);
        email_forgot_password_text = findViewById(R.id.email_forgot_password_text);

        firebaseAuth = FirebaseAuth.getInstance();

        topAppBar_forgot_password.setNavigationOnClickListener(v -> onBackPressed());

        change_password_btn.setOnClickListener(v -> {

            if (isOnline()) {
                email = Objects.requireNonNull(email_forgot_password_text.getEditText()).getText().toString();

                if (email.isEmpty()) {
                    Log.e(TAG_VALIDATE_FILED, "Error! Validation failed");
                    email_forgot_password_text.setError("Это поле не может быть пустым");
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(unused -> {
                                Log.i(TAG_FORGOT_PASSWORD_ACTIVITY, "Password recovery link has been sent");
                                Toast.makeText(this, "Ссылка для восстановления пароля отправлена", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG_FORGOT_PASSWORD_ACTIVITY, String.format("Error! %s", e));
                                Toast.makeText(this, String.format("Ошибка! %s", e), Toast.LENGTH_SHORT).show();
                            });
                }
            }

        });

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}