package com.george.vector.ui.users.root.profile;

import static com.george.vector.common.utils.consts.Logs.TAG_REGISTER_ACTIVITY;
import static com.george.vector.common.utils.consts.Logs.TAG_VALIDATE_FILED;
import static com.george.vector.common.utils.TextValidatorUtils.validateEmail;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.databinding.ActivityRegisterUserBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RegisterUserActivity extends AppCompatActivity {

    String name;
    String lastname;
    String patronymic;
    String email;
    String password;
    String role;
    String permission;
    String userID;

    FirebaseAuth mAuth1, mAuth2;

    ActivityRegisterUserBinding binding;
    UserViewModel userViewModel;

    TextValidatorUtils textValidatorUtils = new TextValidatorUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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

        binding.registerUserBtn.setOnClickListener(v -> {
            name = Objects.requireNonNull(binding.textInputLayoutNameUser.getEditText()).getText().toString();
            lastname = Objects.requireNonNull(binding.textInputLayoutLastNameUser.getEditText()).getText().toString();
            patronymic = Objects.requireNonNull(binding.textInputLayoutPatronymicUser.getEditText()).getText().toString();
            email = Objects.requireNonNull(binding.textInputLayoutEmailUser.getEditText()).getText().toString();
            password = Objects.requireNonNull(binding.textInputLayoutPasswordUser.getEditText()).getText().toString();
            role = Objects.requireNonNull(binding.textInputLayoutRoleUser.getEditText()).getText().toString();
            permission = Objects.requireNonNull(binding.textInputLayoutPermissionUser.getEditText()).getText().toString();

            if(!validateFields()) {
                return;
            }

            if (validateEmail(email)) {
                Log.e(TAG_VALIDATE_FILED, "Email validation failed");
                binding.textInputLayoutEmailUser.setError("Некорректный формат e-mail");
                return;
            }

            binding.progressBarRegister.setVisibility(View.VISIBLE);
            try {
                mAuth2.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (permission.equals("ОП Остафьево"))
                            permission = "ost_school";

                        if (permission.equals("ОП Аист"))
                            permission = "ost_aist";

                        if (permission.equals("ОП Ягодка"))
                            permission = "ost_yagodka";

                        if (permission.equals("ОП Барыши"))
                            permission = "bar_school";

                        if (permission.equals("ОП Ручеек"))
                            permission = "bar_rucheek";

                        if (permission.equals("ОП Звездочка"))
                            permission = "bar_star";


                        userID = Objects.requireNonNull(mAuth2.getCurrentUser()).getUid();
                        userViewModel.saveUser(new User(name, lastname, patronymic,
                                email, role, permission, password));

                        onBackPressed();

                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Error" + Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG_REGISTER_ACTIVITY, "Error! " + task.getException().toString());
                    }

                    binding.progressBarRegister.setVisibility(View.INVISIBLE);

                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG_REGISTER_ACTIVITY, "Error! " + e);
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });

        initFields();

    }

    boolean validateFields() {
        String lastName = binding.textInputLayoutLastNameUser.getEditText().getText().toString();
        String name = binding.textInputLayoutNameUser.getEditText().getText().toString();
        String patronymic = binding.textInputLayoutPatronymicUser.getEditText().getText().toString();
        String email = binding.textInputLayoutEmailUser.getEditText().getText().toString();
        String password = binding.textInputLayoutPasswordUser.getEditText().getText().toString();
        String role = binding.textInputLayoutRoleUser.getEditText().getText().toString();
        String permission = binding.textInputLayoutPermissionUser.getEditText().getText().toString();

        return textValidatorUtils.isEmptyField(lastName, binding.textInputLayoutLastNameUser) &
                textValidatorUtils.isEmptyField(name, binding.textInputLayoutNameUser) &
                textValidatorUtils.isEmptyField(patronymic, binding.textInputLayoutPatronymicUser) &
                textValidatorUtils.isEmptyField(email, binding.textInputLayoutEmailUser) &
                textValidatorUtils.isEmptyField(password, binding.textInputLayoutPasswordUser) &
                textValidatorUtils.isEmptyField(role, binding.textInputLayoutRoleUser) &
                textValidatorUtils.isEmptyField(permission, binding.textInputLayoutPermissionUser);
    }

    void initFields() {
        binding.topAppBarRegister.setNavigationOnClickListener(v -> onBackPressed());

        String[] roles = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> arrayRoles = new ArrayAdapter<>(
                RegisterUserActivity.this,
                R.layout.dropdown_menu_categories,
                roles
        );

        binding.autoCompleteTextViewRoleUser.setAdapter(arrayRoles);

        binding.textInputLayoutRoleUser.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (text.toString().equals("Root") || text.toString().equals("Исполнитель"))
                    binding.textInputLayoutPermissionUser.getEditText().setText(R.string.root_permission);
                else {
                    binding.textInputLayoutPermissionUser.getEditText().setText("");

                    String[] permissions = getResources().getStringArray(R.array.permissions);
                    ArrayAdapter<String> arrayPermissions = new ArrayAdapter<>(
                            RegisterUserActivity.this,
                            R.layout.dropdown_menu_categories,
                            permissions
                    );

                    binding.autoCompleteTextViewPermissionUser.setAdapter(arrayPermissions);
                }
            }
        });
    }
}