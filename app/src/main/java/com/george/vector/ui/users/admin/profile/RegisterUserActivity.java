package com.george.vector.ui.users.admin.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.databinding.ActivityRegisterUserBinding;
import com.george.vector.network.model.user.RegisterUserModel;
import com.george.vector.ui.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserActivity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;

    private AuthViewModel authViewModel;

    public static final String TAG = RegisterUserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBarRegister.setNavigationOnClickListener(v -> onBackPressed());

        initViewModels();
        initFields();

        binding.registerUserBtn.setOnClickListener(v -> {
            binding.progressBarRegister.setVisibility(View.VISIBLE);
            String zone = binding.textInputLayoutPermissionUser.getEditText().getText().toString();
            String name = binding.textInputLayoutNameUser.getEditText().getText().toString();
            String lastName = binding.textInputLayoutLastNameUser.getEditText().getText().toString();
            String patronymic = binding.textInputLayoutPatronymicUser.getEditText().getText().toString();
            String email = binding.textInputLayoutEmail.getEditText().getText().toString();
            String username = binding.textInputLayoutUsername.getEditText().getText().toString();
            String password = binding.textInputLayoutPasswordUser.getEditText().getText().toString();
            String role = binding.textInputLayoutRoleUser.getEditText().getText().toString();

            List<String> roles = new ArrayList<>();
            roles.add(role);

            RegisterUserModel userModel = new RegisterUserModel(zone, name, lastName, patronymic,
                    email, password, username, roles);

            authViewModel.register(userModel).observe(this, message -> {
                String messageStr = message.getMessage();
                binding.progressBarRegister.setVisibility(View.INVISIBLE);

                if (messageStr.equals("User registered successfully!")) {
                    Toast.makeText(this,
                                    "User registered successfully!",
                                    Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

                if (messageStr.equals("Error: Email is already in use!")) {
                    Toast.makeText(this,
                                    "Error: Email is already in use!",
                                    Toast.LENGTH_SHORT).show();
                }

                if (messageStr.equals("Error: Username is already taken!")) {
                    Toast.makeText(this,
                                    "Error: Username is already taken!",
                                    Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initFields() {
        String[] rolesStrings = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> rolesAdapter = new ArrayAdapter<>(
                RegisterUserActivity.this,
                R.layout.dropdown_menu_categories,
                rolesStrings
        );
        binding.autoCompleteTextViewRoleUser.setAdapter(rolesAdapter);

        String[] zones = getResources().getStringArray(R.array.zones);
        ArrayAdapter<String> zonesAdapter = new ArrayAdapter<>(
                RegisterUserActivity.this,
                R.layout.dropdown_menu_categories,
                zones
        );
        binding.autoCompleteTextViewPermissionUser.setAdapter(zonesAdapter);
    }

    private void initViewModels() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

}