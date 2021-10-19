package com.george.vector.common.edit_users;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_register;
    TextInputLayout text_input_layout_name_user, text_input_layout_last_name_user,
            text_input_layout_patronymic_user, text_input_layout_email_user,
            text_input_layout_role_user, text_input_layout_edit_permission_user, text_input_layout_password_user;
    MaterialAutoCompleteTextView auto_complete_text_view_role_user, auto_complete_text_view_edit_permission_user;
    Button update_user_btn;
    LinearProgressIndicator progress_bar_edit_user;

    String name_user, last_name_user, patronymic_user, email_user, role_user, permission_user, userID, password;

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
        progress_bar_edit_user = findViewById(R.id.progress_bar_edit_user);
        text_input_layout_edit_permission_user = findViewById(R.id.text_input_layout_edit_permission_user);
        auto_complete_text_view_edit_permission_user = findViewById(R.id.auto_complete_text_view_edit_permission_user);
        text_input_layout_password_user = findViewById(R.id.text_input_layout_password_user);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        userID = arguments.get("user_id").toString();
        Log.d(TAG, String.format("id: %s", userID));

        topAppBar_register.setNavigationOnClickListener(v -> onBackPressed());

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            name_user = value.getString("name");
            last_name_user = value.getString("last_name");
            patronymic_user = value.getString("patronymic");
            email_user = value.getString("email");
            role_user = value.getString("role");
            permission_user = value.getString("permission");
            password = value.getString("password");

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
            Objects.requireNonNull(text_input_layout_edit_permission_user.getEditText()).setText(permission_user);

            if (email_user.equals("api@2122.pro"))
                text_input_layout_password_user.getEditText().setText("Я не знаю никаких паролей ¯\\_(ツ)_/¯");
            else
                text_input_layout_password_user.getEditText().setText(password);

            String[] items = getResources().getStringArray(R.array.roles);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    EditUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            auto_complete_text_view_role_user.setAdapter(arrayAdapter);

            String[] permissions = getResources().getStringArray(R.array.permissions);
            ArrayAdapter<String> arrayAdapterPermission = new ArrayAdapter<>(
                    EditUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    permissions
            );

            auto_complete_text_view_edit_permission_user.setAdapter(arrayAdapterPermission);

        });

        update_user_btn.setOnClickListener(v -> {
            name_user = Objects.requireNonNull(text_input_layout_name_user.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(text_input_layout_last_name_user.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(text_input_layout_patronymic_user.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(text_input_layout_email_user.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(text_input_layout_role_user.getEditText()).getText().toString();
            permission_user = Objects.requireNonNull(text_input_layout_edit_permission_user.getEditText()).getText().toString();
            password = text_input_layout_password_user.getEditText().getText().toString();

            if (validateFields()) {
                progress_bar_edit_user.setVisibility(View.VISIBLE);

                Map<String, Object> user = new HashMap<>();
                user.put("name", name_user);
                user.put("last_name", last_name_user);
                user.put("patronymic", patronymic_user);
                user.put("email", email_user);
                user.put("role", role_user);
                user.put("permission", permission_user);
                user.put("password", password);

                documentReference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "update completed!");
                        progress_bar_edit_user.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(this, ListUsersActivity.class));
                    } else
                        Log.i(TAG, "Error: " + task.getException());
                });

                documentReference.set(user)
                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));

            }

        });

        text_input_layout_password_user.setEndIconOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            String email = text_input_layout_email_user.getEditText().getText().toString();
            String password = text_input_layout_password_user.getEditText().getText().toString();

            ClipData clip = ClipData.newPlainText(null,email + " " + password);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getApplicationContext(),"Логин и пароль пользователя скопированы",Toast.LENGTH_SHORT).show();
        });

    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(text_input_layout_name_user);
        utils.clear_error(text_input_layout_last_name_user);
        utils.clear_error(text_input_layout_patronymic_user);
        utils.clear_error(text_input_layout_email_user);
        utils.clear_error(text_input_layout_role_user);

        boolean checkName = utils.validate_field(name_user, text_input_layout_name_user);
        boolean checkLastName = utils.validate_field(last_name_user, text_input_layout_last_name_user);
        boolean checkPatronymic = utils.validate_field(patronymic_user, text_input_layout_patronymic_user);
        boolean checkEmail = utils.validate_field(email_user, text_input_layout_email_user);
        boolean checkRole = utils.validate_field(role_user, text_input_layout_role_user);

        return checkName & checkLastName & checkPatronymic & checkEmail & checkRole;
    }
}
