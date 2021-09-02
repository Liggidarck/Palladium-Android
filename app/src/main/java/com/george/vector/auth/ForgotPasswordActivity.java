package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    String email;

    MaterialToolbar topAppBar_forgot_password;
    Button change_password_btn;
    TextInputLayout email_forgot_password_text;

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

                if (email.isEmpty())
                    email_forgot_password_text.setError("Это поле не может быть пустым");
                else {
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(unused -> {
                                Log.i(TAG, "Ссылка для восстановления пароля отправлена");
                                Toast.makeText(this, "Ссылка для восстановления пароля отправлена", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, String.format("Error! %s", e));
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