package com.george.vector.admin.edit_users;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.ErrorsUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_register;
    Button update_user_btn;
    TextInputLayout text_input_layout_name_user, text_input_layout_last_name_user,
            text_input_layout_patronymic_user, text_input_layout_email_user, text_input_layout_role_user;
    MaterialAutoCompleteTextView auto_complete_text_view_role_user;

    String name_user, last_name_user, patronymic_user, email_user, role_user, userID;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "EditUserActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_activity);

        topAppBar_register = findViewById(R.id.topAppBar_edit);
        text_input_layout_name_user = findViewById(R.id.text_input_layout_edit_name_user);
        text_input_layout_last_name_user = findViewById(R.id.text_input_layout_edit_last_name_user);
        text_input_layout_patronymic_user = findViewById(R.id.text_input_layout_edit_patronymic_user);
        text_input_layout_email_user = findViewById(R.id.text_input_layout_edit_email_user);
        text_input_layout_role_user = findViewById(R.id.text_input_layout_edit_role_user);
        auto_complete_text_view_role_user = findViewById(R.id.auto_complete_text_view_edit_role_user);
        update_user_btn = findViewById(R.id.update_user_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        userID = arguments.get("user_id").toString();
        Log.i(TAG, "id: " + userID);

        topAppBar_register.setNavigationOnClickListener(v -> onBackPressed());

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            name_user = value.getString("name");
            last_name_user = value.getString("last_name");
            patronymic_user = value.getString("patronymic");
            email_user = value.getString("email");
            role_user = value.getString("role");

            Log.i(TAG, "name: " + name_user);
            Log.i(TAG, "last_name: " + last_name_user);
            Log.i(TAG, "patronymic: " + patronymic_user);
            Log.i(TAG, "email: " + email_user);
            Log.i(TAG, "role: " + role_user);

            Objects.requireNonNull(text_input_layout_name_user.getEditText()).setText(name_user);
            Objects.requireNonNull(text_input_layout_last_name_user.getEditText()).setText(last_name_user);
            Objects.requireNonNull(text_input_layout_patronymic_user.getEditText()).setText(patronymic_user);
            Objects.requireNonNull(text_input_layout_email_user.getEditText()).setText(email_user);
            Objects.requireNonNull(text_input_layout_role_user.getEditText()).setText(role_user);

            String[] items = getResources().getStringArray(R.array.roles);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    EditUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            auto_complete_text_view_role_user.setAdapter(arrayAdapter);

        });

        update_user_btn.setOnClickListener(v -> {
            name_user = Objects.requireNonNull(text_input_layout_name_user.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(text_input_layout_last_name_user.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(text_input_layout_patronymic_user.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(text_input_layout_email_user.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(text_input_layout_role_user.getEditText()).getText().toString();


            if(validateFields()) {
                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();

                Map<String, Object> user = new HashMap<>();
                user.put("name", name_user);
                user.put("last_name", last_name_user);
                user.put("patronymic", patronymic_user);
                user.put("email", email_user);
                user.put("role", role_user);
                documentReference.set(user)
                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));


            }

        });

        clearErrors();

    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

        boolean checkName = errorsUtils.validate_field(name_user);
        boolean checkLastName = errorsUtils.validate_field(last_name_user);
        boolean checkPatronymic = errorsUtils.validate_field(patronymic_user);
        boolean checkEmail = errorsUtils.validate_field(email_user);
        boolean checkRole = errorsUtils.validate_field(role_user);

        if(checkName & checkLastName & checkPatronymic & checkEmail & checkRole)
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
