package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityRegisterUser extends AppCompatActivity {

    MaterialToolbar topAppBar_register;

    Button register_user_btn;

    LinearProgressIndicator progress_bar_register;

    TextInputLayout text_input_layout_name_user, text_input_layout_last_name_user,
            text_input_layout_patronymic_user, text_input_layout_email_user,
            text_input_layout_password_user, text_input_layout_role_user, text_input_layout_permission_user;

    MaterialAutoCompleteTextView auto_complete_text_view_role_user, auto_complete_text_view_permission_user;

    String name_user, last_name_user, patronymic_user, email_user, password_user, role_user, permission_user, userID;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        topAppBar_register = findViewById(R.id.topAppBar_register);
        register_user_btn = findViewById(R.id.register_user_btn);
        text_input_layout_name_user = findViewById(R.id.text_input_layout_name_user);
        text_input_layout_last_name_user = findViewById(R.id.text_input_layout_last_name_user);
        text_input_layout_patronymic_user = findViewById(R.id.text_input_layout_patronymic_user);
        text_input_layout_email_user = findViewById(R.id.text_input_layout_email_user);
        text_input_layout_password_user = findViewById(R.id.text_input_layout_password_user);
        text_input_layout_role_user = findViewById(R.id.text_input_layout_role_user);
        auto_complete_text_view_role_user = findViewById(R.id.auto_complete_text_view_role_user);
        progress_bar_register = findViewById(R.id.progress_bar_register);
        text_input_layout_permission_user = findViewById(R.id.text_input_layout_permission_user);
        auto_complete_text_view_permission_user = findViewById(R.id.auto_complete_text_view_permission_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        topAppBar_register.setNavigationOnClickListener(v -> onBackPressed());

        String[] items = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ActivityRegisterUser.this,
                R.layout.dropdown_menu_categories,
                items
        );

        auto_complete_text_view_role_user.setAdapter(arrayAdapter);

        String[] permissions = getResources().getStringArray(R.array.permissions);
        ArrayAdapter<String> arrayAdapterPermissions = new ArrayAdapter<>(
                ActivityRegisterUser.this,
                R.layout.dropdown_menu_categories,
                permissions
        );

        auto_complete_text_view_permission_user.setAdapter(arrayAdapterPermissions);

        register_user_btn.setOnClickListener(v -> {
            name_user = Objects.requireNonNull(text_input_layout_name_user.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(text_input_layout_last_name_user.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(text_input_layout_patronymic_user.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(text_input_layout_email_user.getEditText()).getText().toString();
            password_user = Objects.requireNonNull(text_input_layout_password_user.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(text_input_layout_role_user.getEditText()).getText().toString();
            permission_user = Objects.requireNonNull(text_input_layout_permission_user.getEditText()).getText().toString();

            if(validateFields()) {
                progress_bar_register.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email_user, password_user).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name_user);
                        user.put("last_name", last_name_user);
                        user.put("patronymic", patronymic_user);
                        user.put("email", email_user);
                        user.put("role", role_user);
                        user.put("permission", permission_user);
                        documentReference.set(user)
                                .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                                .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));

                        onBackPressed();

                    } else
                        Toast.makeText(ActivityRegisterUser.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                    progress_bar_register.setVisibility(View.INVISIBLE);

                });
            }

        });

        clearErrors();
    }
    boolean validateFields() {
        Utils utils = new Utils();

        boolean checkName = utils.validate_field(name_user);
        boolean checkLastName = utils.validate_field(last_name_user);
        boolean checkPatronymic = utils.validate_field(patronymic_user);
        boolean checkEmail = utils.validate_field(email_user);
        boolean checkPassword = utils.validate_field(password_user);
        boolean checkRole = utils.validate_field(role_user);

        if(checkName & checkLastName & checkPatronymic & checkEmail & checkPassword & checkRole)
            return true;
        else {

            if(!checkName)
                text_input_layout_name_user.setError("Это поле не может быьт пустым");

            if(!checkLastName)
                text_input_layout_last_name_user.setError("Это поле не может быть пустым");

            if(!checkPatronymic)
                text_input_layout_patronymic_user.setError("Это поле не может быть пустым");

            if(!checkEmail)
                text_input_layout_email_user.setError("Это  поле не может быть пустым");

            if(!checkPassword)
                text_input_layout_password_user.setError("Это поле не может быть пустым");

            if(!checkRole)
                text_input_layout_role_user.setError("Это поле не может быть пустым");

            return false;
        }

    }

    void clearErrors() {
        Objects.requireNonNull(text_input_layout_name_user.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_user.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_last_name_user.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_last_name_user.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_patronymic_user.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_patronymic_user.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_email_user.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_email_user.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_password_user.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_password_user.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_role_user.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_role_user.setError(null);
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