package com.george.vector.common.edit_users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.user.main.MainUserActivity;
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
            Log.i(TAG, String.format("name: %s last_name: %s patronymic: %s email: %s", name_user, last_name_user, patronymic_user, email_user));

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
                    } else
                        Log.i(TAG, "Error: " + task.getException());
                    
                });

                documentReference.set(user)
                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));
            }
        });
    }

    boolean validateFields(){
        Utils utils = new Utils();

        utils.clear_error(text_input_layout_name_user_edit);
        utils.clear_error(text_input_layout_last_name_user_edit);
        utils.clear_error(text_input_layout_patronymic_user_edit);
        utils.clear_error(text_input_layout_email_user_edit);

        boolean checkName = utils.validate_field(name_user, text_input_layout_name_user_edit);
        boolean checkLastName = utils.validate_field(last_name_user, text_input_layout_last_name_user_edit);
        boolean checkPatronymic = utils.validate_field(patronymic_user, text_input_layout_patronymic_user_edit);
        boolean checkEmail = utils.validate_field(email_user, text_input_layout_email_user_edit);

        return checkName & checkLastName & checkPatronymic & checkEmail;
    }

}