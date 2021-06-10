package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.george.vector.admin.MainAdminActivity;
import com.george.vector.R;
import com.george.vector.user.MainUserActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityLogin extends AppCompatActivity {

    // Все View элементы
    TextInputLayout email_login_text_layout, password_login_text_layout;
    ImageView sign_in_btn;

    // Все глобальные переменные
    String emailED, passwordED, userID;
    private static final String TAG = "Login Activity";

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sign_in_btn = findViewById(R.id.sign_in_btn);
        email_login_text_layout = findViewById(R.id.email_login_text_layout);
        password_login_text_layout = findViewById(R.id.password_login_text_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(ActivityLogin.this, "Start Application", Toast.LENGTH_SHORT).show();

            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, (value, error) -> {
                assert value != null;

                String check_role = value.getString("role");
                Log.d(TAG, "ROLE - " + check_role);

                assert check_role != null;
                if(check_role.equals("Администратор"))
                    startActivity(new Intent(this, MainAdminActivity.class));

            });

        }

        sign_in_btn.setOnClickListener(v -> {
            emailED = Objects.requireNonNull(email_login_text_layout.getEditText()).getText().toString();
            passwordED = Objects.requireNonNull(password_login_text_layout.getEditText()).getText().toString();

            firebaseAuth.signInWithEmailAndPassword(emailED, passwordED).addOnCompleteListener(task -> {

                if(task.isSuccessful()) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                    userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                    documentReference.addSnapshotListener(this, (value, error) -> {
                        assert value != null;

                        String check_role = value.getString("role");
                        Log.d(TAG, "ROLE - " + check_role);

                        assert check_role != null;
                        if(check_role.equals("Администратор"))
                            startActivity(new Intent(this, MainAdminActivity.class));

                        if(check_role.equals("Пользователь"))
                            startActivity(new Intent(this, MainUserActivity.class));
                    });

                } else {
                    Toast.makeText(this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        });

    }
}