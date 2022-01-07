package com.george.vector.users.root.edit_users;

import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Logs.TAG_REGISTER_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_VALIDATE_FILED;
import static com.george.vector.common.utils.Utils.validateEmail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityRegisterUserBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RegisterUserActivity extends AppCompatActivity {

    String name_user, last_name_user, patronymic_user, email_user,
            password_user, role_user, permission_user, userID;

    FirebaseAuth mAuth1, mAuth2;
    FirebaseFirestore firebaseFirestore;

    ActivityRegisterUserBinding registerUserBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerUserBinding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(registerUserBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth1 = FirebaseAuth.getInstance();

        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://school-2122.firebaseio.com")
                    .setApiKey("AIzaSyAAaS5-aMMTMBa6BWNBbM_0FHnlO5Ql328")
                    .setApplicationId("school-2122")
                    .build();

            FirebaseApp firebaseApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps(this);

            if (firebaseApps != null && !firebaseApps.isEmpty()) {

                for (FirebaseApp app : firebaseApps) {
                    if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                        firebaseApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, UUID.randomUUID().toString());
                }

            } else
                firebaseApp = FirebaseApp.initializeApp(this, firebaseOptions);

            mAuth2 = FirebaseAuth.getInstance(firebaseApp);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error! " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG_REGISTER_ACTIVITY, "Error! " + e.toString());
        }


        registerUserBinding.registerUserBtn.setOnClickListener(v -> {

            name_user = Objects.requireNonNull(registerUserBinding.textInputLayoutNameUser.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(registerUserBinding.textInputLayoutLastNameUser.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(registerUserBinding.textInputLayoutPatronymicUser.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(registerUserBinding.textInputLayoutEmailUser.getEditText()).getText().toString();
            password_user = Objects.requireNonNull(registerUserBinding.textInputLayoutPasswordUser.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(registerUserBinding.textInputLayoutRoleUser.getEditText()).getText().toString();
            permission_user = Objects.requireNonNull(registerUserBinding.textInputLayoutPermissionUser.getEditText()).getText().toString();

            if (validateFields()) {
                if (!validateEmail(email_user)) {
                    Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                    registerUserBinding.textInputLayoutEmailUser.setError("Некорректный формат e-mail");
                } else {
                    registerUserBinding.progressBarRegister.setVisibility(View.VISIBLE);

                    try {
                        mAuth2.createUserWithEmailAndPassword(email_user, password_user).addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                userID = Objects.requireNonNull(mAuth2.getCurrentUser()).getUid();
                                DocumentReference documentReference = firebaseFirestore.collection(USERS).document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name_user);
                                user.put("last_name", last_name_user);
                                user.put("patronymic", patronymic_user);
                                user.put("email", email_user);
                                user.put("role", role_user);
                                user.put("permission", permission_user);
                                user.put("password", password_user);

                                documentReference.set(user)
                                        .addOnSuccessListener(unused -> Log.d(TAG_REGISTER_ACTIVITY, "Success register user - " + userID))
                                        .addOnFailureListener(e -> Log.e(TAG_REGISTER_ACTIVITY, "Failure register user - " + e.toString()));

                                onBackPressed();

                            } else {
                                Toast.makeText(RegisterUserActivity.this, "Error" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG_REGISTER_ACTIVITY, "Error! " + task.getException().toString());
                            }

                            registerUserBinding.progressBarRegister.setVisibility(View.INVISIBLE);
                            Log.d(TAG_REGISTER_ACTIVITY, "Complete: " + task.getResult());
                            Toast.makeText(this, "Complete: " + task.getResult(), Toast.LENGTH_SHORT).show();

                        }).addOnFailureListener(e -> {
                            registerUserBinding.progressBarRegister.setVisibility(View.INVISIBLE);
                            Log.e(TAG_REGISTER_ACTIVITY, "Error: " + e.toString());
                            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG_REGISTER_ACTIVITY, "Error! " + e.toString());
                        Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }


                }
            }

        });

        initFields();

    }

    void initFields() {
        registerUserBinding.topAppBarRegister.setNavigationOnClickListener(v -> onBackPressed());

        String[] roles = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> arrayRoles = new ArrayAdapter<>(
                RegisterUserActivity.this,
                R.layout.dropdown_menu_categories,
                roles
        );

        registerUserBinding.autoCompleteTextViewRoleUser.setAdapter(arrayRoles);

        registerUserBinding.textInputLayoutRoleUser.getEditText().addTextChangedListener(new TextValidator(registerUserBinding.textInputLayoutRoleUser.getEditText()) {
            @Override
            public void validate(TextView text_view, String text) {
                Log.d(TAG_REGISTER_ACTIVITY, "text: " + text);
                if(text.equals("Root") || text.equals("Исполнитель"))
                    registerUserBinding.textInputLayoutPermissionUser.getEditText().setText("All");
                else {
                    registerUserBinding.textInputLayoutPermissionUser.getEditText().setText("");

                    String[] permissions = getResources().getStringArray(R.array.permissions);
                    ArrayAdapter<String> arrayPermissions = new ArrayAdapter<>(
                            RegisterUserActivity.this,
                            R.layout.dropdown_menu_categories,
                            permissions
                    );

                    registerUserBinding.autoCompleteTextViewPermissionUser.setAdapter(arrayPermissions);
                }
            }
        });


    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(registerUserBinding.textInputLayoutNameUser);
        utils.clear_error(registerUserBinding.textInputLayoutLastNameUser);
        utils.clear_error(registerUserBinding.textInputLayoutPatronymicUser);
        utils.clear_error(registerUserBinding.textInputLayoutEmailUser);
        utils.clear_error(registerUserBinding.textInputLayoutPasswordUser);
        utils.clear_error(registerUserBinding.textInputLayoutRoleUser);
        utils.clear_error(registerUserBinding.textInputLayoutPermissionUser);

        boolean checkName = utils.validate_field(name_user, registerUserBinding.textInputLayoutNameUser);
        boolean checkLastName = utils.validate_field(last_name_user, registerUserBinding.textInputLayoutLastNameUser);
        boolean checkPatronymic = utils.validate_field(patronymic_user, registerUserBinding.textInputLayoutPatronymicUser);
        boolean checkEmail = utils.validate_field(email_user, registerUserBinding.textInputLayoutEmailUser);
        boolean checkPassword = utils.validate_field(password_user, registerUserBinding.textInputLayoutPasswordUser);
        boolean checkRole = utils.validate_field(role_user, registerUserBinding.textInputLayoutRoleUser);
        boolean checkPermission = utils.validate_field(permission_user, registerUserBinding.textInputLayoutPermissionUser);

        return checkName & checkLastName & checkPatronymic & checkEmail & checkPassword & checkRole & checkPermission;
    }

}