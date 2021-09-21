package com.george.vector.auth;

import static com.george.vector.common.utils.Utils.validateEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActivityRegisterUser extends AppCompatActivity {

    MaterialToolbar topAppBar_register;

    Button register_user_btn;

    LinearProgressIndicator progress_bar_register;

    TextInputLayout text_layout_name_user, text_layout_last_name_user,
            text_layout_patronymic_user, text_layout_email_user,
            text_layout_password_user, text_layout_role_user, text_layout_permission_user;

    MaterialAutoCompleteTextView complete_text_role_user, complete_text_permission_user;

    String name_user, last_name_user, patronymic_user, email_user, password_user, role_user, permission_user, userID;

    FirebaseAuth mAuth1, mAuth2;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        topAppBar_register = findViewById(R.id.topAppBar_register);
        register_user_btn = findViewById(R.id.register_user_btn);
        text_layout_name_user = findViewById(R.id.text_input_layout_name_user);
        text_layout_last_name_user = findViewById(R.id.text_input_layout_last_name_user);
        text_layout_patronymic_user = findViewById(R.id.text_input_layout_patronymic_user);
        text_layout_email_user = findViewById(R.id.text_input_layout_email_user);
        text_layout_password_user = findViewById(R.id.text_input_layout_password_user);
        text_layout_role_user = findViewById(R.id.text_input_layout_role_user);
        complete_text_role_user = findViewById(R.id.auto_complete_text_view_role_user);
        progress_bar_register = findViewById(R.id.progress_bar_register);
        text_layout_permission_user = findViewById(R.id.text_input_layout_permission_user);
        complete_text_permission_user = findViewById(R.id.auto_complete_text_view_permission_user);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth1 = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://school-2122.firebaseio.com")
                .setApiKey("AIzaSyAAaS5-aMMTMBa6BWNBbM_0FHnlO5Ql328")
                .setApplicationId("school-2122").build();

        FirebaseApp firebaseApp;

        boolean hasBeenInitialized = false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(this);
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                hasBeenInitialized = true;
                Log.d(TAG, "Init!");
                firebaseApp = app;
                mAuth2 = FirebaseAuth.getInstance(firebaseApp);
            }

        }

        if (!hasBeenInitialized) {
            firebaseApp = FirebaseApp.initializeApp(this, firebaseOptions);
            Log.d(TAG, "Init if!");
            mAuth2 = FirebaseAuth.getInstance(firebaseApp);
        }

        topAppBar_register.setNavigationOnClickListener(v -> onBackPressed());

        String[] items = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ActivityRegisterUser.this,
                R.layout.dropdown_menu_categories,
                items
        );

        complete_text_role_user.setAdapter(arrayAdapter);

        String[] permissions = getResources().getStringArray(R.array.permissions);
        ArrayAdapter<String> arrayAdapterPermissions = new ArrayAdapter<>(
                ActivityRegisterUser.this,
                R.layout.dropdown_menu_categories,
                permissions
        );

        complete_text_permission_user.setAdapter(arrayAdapterPermissions);

        register_user_btn.setOnClickListener(v -> {

            name_user = Objects.requireNonNull(text_layout_name_user.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(text_layout_last_name_user.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(text_layout_patronymic_user.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(text_layout_email_user.getEditText()).getText().toString();
            password_user = Objects.requireNonNull(text_layout_password_user.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(text_layout_role_user.getEditText()).getText().toString();
            permission_user = Objects.requireNonNull(text_layout_permission_user.getEditText()).getText().toString();

            if (validateFields()) {
                if (!validateEmail(email_user)) {
                    Log.d(TAG, "Email non-correct");
                    text_layout_email_user.setError("Не корректный формат e-mail");
                } else {
                    progress_bar_register.setVisibility(View.VISIBLE);

                    mAuth2.createUserWithEmailAndPassword(email_user, password_user).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            userID = Objects.requireNonNull(mAuth2.getCurrentUser()).getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name_user);
                            user.put("last_name", last_name_user);
                            user.put("patronymic", patronymic_user);
                            user.put("email", email_user);
                            user.put("role", role_user);
                            user.put("permission", permission_user);
                            user.put("password", password_user);

                            documentReference.set(user)
                                    .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                                    .addOnFailureListener(e -> Log.e("TAG", "Failure - " + e.toString()));

                            onBackPressed();

                        } else
                            Toast.makeText(ActivityRegisterUser.this, "Error" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        progress_bar_register.setVisibility(View.INVISIBLE);

                    });
                }
            }

        });

    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(text_layout_name_user);
        utils.clear_error(text_layout_last_name_user);
        utils.clear_error(text_layout_patronymic_user);
        utils.clear_error(text_layout_email_user);
        utils.clear_error(text_layout_password_user);
        utils.clear_error(text_layout_role_user);
        utils.clear_error(text_layout_permission_user);

        boolean checkName = utils.validate_field(name_user, text_layout_name_user);
        boolean checkLastName = utils.validate_field(last_name_user, text_layout_last_name_user);
        boolean checkPatronymic = utils.validate_field(patronymic_user, text_layout_patronymic_user);
        boolean checkEmail = utils.validate_field(email_user, text_layout_email_user);
        boolean checkPassword = utils.validate_field(password_user, text_layout_password_user);
        boolean checkRole = utils.validate_field(role_user, text_layout_role_user);
        boolean checkPermission = utils.validate_field(permission_user, text_layout_permission_user);

        return checkName & checkLastName & checkPatronymic & checkEmail & checkPassword & checkRole & checkPermission;
    }

}