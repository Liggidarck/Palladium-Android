package com.george.vector.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.george.vector.R;
import com.george.vector.common.utils.ErrorsUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditDataUserActivity extends AppCompatActivity {

    private static final String TAG = "EditDataUserActivity";
    MaterialToolbar toolbar;
    LinearProgressIndicator progress_bar_edit_data_user;

    TextInputLayout text_input_layout_name_user_edit, text_input_layout_last_name_user_edit,
            text_input_layout_patronymic_user_edit, text_input_layout_email_user_edit, text_input_layout_role_user_edit;

    Button btn_save_edit_data_user;

    String name_user, last_name_user, patronymic_user, email_user, role_user, userID;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_user);

        toolbar = findViewById(R.id.topAppBar_edit_data_user);
        progress_bar_edit_data_user = findViewById(R.id.progress_bar_edit_data_user);
        text_input_layout_name_user_edit = findViewById(R.id.text_input_layout_name_user_edit);
        text_input_layout_last_name_user_edit = findViewById(R.id.text_input_layout_last_name_user_edit);
        text_input_layout_patronymic_user_edit = findViewById(R.id.text_input_layout_patronymic_user_edit);
        text_input_layout_email_user_edit = findViewById(R.id.text_input_layout_email_user_edit);
        btn_save_edit_data_user = findViewById(R.id.btn_save_edit_data_user);
        text_input_layout_role_user_edit = findViewById(R.id.text_input_layout_role_user_edit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            name_user = value.getString("name");
            last_name_user = value.getString("last_name");
            patronymic_user = value.getString("patronymic");
            email_user = value.getString("email");
            role_user = value.getString("role");
            Log.i(TAG, "name: " + name_user + " last_name: " + last_name_user + " patr: " + patronymic_user + " email: " + email_user);

            Objects.requireNonNull(text_input_layout_name_user_edit.getEditText()).setText(name_user);
            Objects.requireNonNull(text_input_layout_last_name_user_edit.getEditText()).setText(last_name_user);
            Objects.requireNonNull(text_input_layout_patronymic_user_edit.getEditText()).setText(patronymic_user);
            Objects.requireNonNull(text_input_layout_email_user_edit.getEditText()).setText(email_user);
            Objects.requireNonNull(text_input_layout_role_user_edit.getEditText()).setText(role_user);
        });

        btn_save_edit_data_user.setOnClickListener(v -> {
            name_user = Objects.requireNonNull(text_input_layout_name_user_edit.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(text_input_layout_last_name_user_edit.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(text_input_layout_patronymic_user_edit.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(text_input_layout_email_user_edit.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(text_input_layout_role_user_edit.getEditText()).getText().toString();

            if(validateFields()) {
                progress_bar_edit_data_user.setVisibility(View.VISIBLE);

                Map<String, Object> user = new HashMap<>();
                user.put("name", name_user);
                user.put("last_name", last_name_user);
                user.put("patronymic", patronymic_user);
                user.put("email", email_user);
                user.put("role", role_user);

                documentReference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.i(TAG, "update completed!");
                        progress_bar_edit_data_user.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(this, MainUserActivity.class);
                        intent.putExtra("email", email_user);
                        startActivity(intent);
                    } else {
                        Log.i(TAG, "Error: " + task.getException());
                    }

                });

                documentReference.set(user)
                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));
            }
        });

        clearErrors();
    }

    boolean validateFields(){
        ErrorsUtils errorsUtils = new ErrorsUtils();

        boolean checkName = errorsUtils.validate_field(name_user);
        boolean checkLastName = errorsUtils.validate_field(last_name_user);
        boolean checkPatronymic = errorsUtils.validate_field(patronymic_user);
        boolean checkEmail = errorsUtils.validate_field(email_user);

        if(checkName & checkLastName & checkPatronymic & checkEmail)
            return true;
        else {

            if(!checkName)
                text_input_layout_name_user_edit.setError("Это поле не может быьт пустым");

            if(!checkLastName)
                text_input_layout_last_name_user_edit.setError("Это поле не может быть пустым");

            if(!checkPatronymic)
                text_input_layout_patronymic_user_edit.setError("Это поле не может быть пустым");

            if(!checkEmail)
                text_input_layout_email_user_edit.setError("Это  поле не может быть пустым");

            return false;
        }

    }

    void clearErrors() {
        Objects.requireNonNull(text_input_layout_name_user_edit.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_user_edit.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_last_name_user_edit.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_last_name_user_edit.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_patronymic_user_edit.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_patronymic_user_edit.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_email_user_edit.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_email_user_edit.setError(null);
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