package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.george.vector.admin.MainAdminActivity;
import com.george.vector.R;
import com.george.vector.admin.tasks.sort_by_category.FolderActivity;
import com.george.vector.common.ErrorsUtils;
import com.george.vector.user.MainUserActivity;
import com.george.vector.worker.MainWorkerActivity;
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
                String check_email = value.getString("email");
                Log.d(TAG, "ROLE - " + check_role);

                assert check_role != null;
                if(check_role.equals("Администратор"))
                    startActivity(new Intent(this, MainAdminActivity.class));

                if (check_role.equals("Пользователь")) {
                    Intent intent = new Intent(this, MainUserActivity.class);
                    intent.putExtra("email", check_email);
                    startActivity(intent);
                }

                if (check_role.equals("Исполнитель")) {
                    Intent intent = new Intent(this, MainWorkerActivity.class);
                    intent.putExtra("email", check_email);
                    startActivity(intent);
                }

            });

        }

        sign_in_btn.setOnClickListener(v -> {
            emailED = Objects.requireNonNull(email_login_text_layout.getEditText()).getText().toString();
            passwordED = Objects.requireNonNull(password_login_text_layout.getEditText()).getText().toString();

            if(validateFields()) {

                firebaseAuth.signInWithEmailAndPassword(emailED, passwordED).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                        documentReference.addSnapshotListener(this, (value, error) -> {
                            assert value != null;

                            String check_role = value.getString("role");
                            String check_email = value.getString("email");
                            Log.d(TAG, "ROLE - " + check_role);

                            assert check_role != null;
                            if (check_role.equals("Администратор"))
                                startActivity(new Intent(this, MainAdminActivity.class));

                            if (check_role.equals("Пользователь")) {
                                Intent intent = new Intent(this, MainUserActivity.class);
                                intent.putExtra("email", check_email);
                                startActivity(intent);
                            }
                        });

                    } else {
                        Toast.makeText(this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }

        });

        clearErrors();

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


}