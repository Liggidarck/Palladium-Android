package com.george.vector.ui.users.root.profile;

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

    String nameUser, lastNameUser, patronymicUser, emailUser,
            passwordUser, roleUser, permissionUser, userID;

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
            Toast.makeText(this, "Error! " + e, Toast.LENGTH_SHORT).show();
            Log.e(TAG_REGISTER_ACTIVITY, "Error! " + e);
        }


        registerUserBinding.registerUserBtn.setOnClickListener(v -> {

            nameUser = Objects.requireNonNull(registerUserBinding.textInputLayoutNameUser.getEditText()).getText().toString();
            lastNameUser = Objects.requireNonNull(registerUserBinding.textInputLayoutLastNameUser.getEditText()).getText().toString();
            patronymicUser = Objects.requireNonNull(registerUserBinding.textInputLayoutPatronymicUser.getEditText()).getText().toString();
            emailUser = Objects.requireNonNull(registerUserBinding.textInputLayoutEmailUser.getEditText()).getText().toString();
            passwordUser = Objects.requireNonNull(registerUserBinding.textInputLayoutPasswordUser.getEditText()).getText().toString();
            roleUser = Objects.requireNonNull(registerUserBinding.textInputLayoutRoleUser.getEditText()).getText().toString();
            permissionUser = Objects.requireNonNull(registerUserBinding.textInputLayoutPermissionUser.getEditText()).getText().toString();

            if (!validateFields()) {
                return;
            }

            if (!validateEmail(emailUser)) {
                Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                registerUserBinding.textInputLayoutEmailUser.setError("Некорректный формат e-mail");
                return;
            }


            registerUserBinding.progressBarRegister.setVisibility(View.VISIBLE);

            try {
                mAuth2.createUserWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        if (permissionUser.equals("ОП Остафьево"))
                            permissionUser = "ost_school";

                        if (permissionUser.equals("ОП Аист"))
                            permissionUser = "ost_aist";

                        if (permissionUser.equals("ОП Ягодка"))
                            permissionUser = "ost_yagodka";


                        if (permissionUser.equals("ОП Барыши"))
                            permissionUser = "bar_school";

                        if (permissionUser.equals("ОП Ручеек"))
                            permissionUser = "bar_rucheek";

                        if (permissionUser.equals("ОП Звездочка"))
                            permissionUser = "bar_star";

                        Log.d(TAG_REGISTER_ACTIVITY, "permission: " + permissionUser);


                        userID = Objects.requireNonNull(mAuth2.getCurrentUser()).getUid();
                        DocumentReference documentReference = firebaseFirestore.collection(USERS).document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", nameUser);
                        user.put("last_name", lastNameUser);
                        user.put("patronymic", patronymicUser);
                        user.put("email", emailUser);
                        user.put("role", roleUser);
                        user.put("permission", permissionUser);
                        user.put("password", passwordUser);

                        documentReference.set(user)
                                .addOnSuccessListener(unused -> Log.d(TAG_REGISTER_ACTIVITY, "Success register user - " + userID))
                                .addOnFailureListener(e -> Log.e(TAG_REGISTER_ACTIVITY, "Failure register user - " + e));

                        onBackPressed();

                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Error" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG_REGISTER_ACTIVITY, "Error! " + task.getException().toString());
                    }

                    registerUserBinding.progressBarRegister.setVisibility(View.INVISIBLE);

                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG_REGISTER_ACTIVITY, "Error! " + e);
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
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
                if (text.equals("Root") || text.equals("Исполнитель"))
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

        utils.clearError(registerUserBinding.textInputLayoutNameUser);
        utils.clearError(registerUserBinding.textInputLayoutLastNameUser);
        utils.clearError(registerUserBinding.textInputLayoutPatronymicUser);
        utils.clearError(registerUserBinding.textInputLayoutEmailUser);
        utils.clearError(registerUserBinding.textInputLayoutPasswordUser);
        utils.clearError(registerUserBinding.textInputLayoutRoleUser);
        utils.clearError(registerUserBinding.textInputLayoutPermissionUser);

        boolean checkName = utils.validateField(nameUser, registerUserBinding.textInputLayoutNameUser);
        boolean checkLastName = utils.validateField(lastNameUser, registerUserBinding.textInputLayoutLastNameUser);
        boolean checkPatronymic = utils.validateField(patronymicUser, registerUserBinding.textInputLayoutPatronymicUser);
        boolean checkEmail = utils.validateField(emailUser, registerUserBinding.textInputLayoutEmailUser);
        boolean checkPassword = utils.validateField(passwordUser, registerUserBinding.textInputLayoutPasswordUser);
        boolean checkRole = utils.validateField(roleUser, registerUserBinding.textInputLayoutRoleUser);
        boolean checkPermission = utils.validateField(permissionUser, registerUserBinding.textInputLayoutPermissionUser);

        return checkName & checkLastName & checkPatronymic & checkEmail & checkPassword & checkRole & checkPermission;
    }

}