package com.george.vector.ui.users.root.profile;

import static java.util.Objects.requireNonNull;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.databinding.EditUserActivityBinding;
import com.george.vector.network.model.User;
import com.george.vector.network.viewmodel.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

public class EditUserActivity extends AppCompatActivity {

    private String nameUser;
    private String lastNameUser;
    private String patronymicUser;
    private String emailUser;
    private String roleUser;
    private String permissionUser;
    private String userID;
    private String password;

    private EditUserActivityBinding binding;

    private UserViewModel userViewModel;

    NetworkUtils networkUtils = new NetworkUtils();
    TextValidatorUtils textValidator = new TextValidatorUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = EditUserActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        userID = arguments.get("user_id").toString();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser(userID).observe(this, user -> {
            nameUser = user.getName();
            lastNameUser = user.getLast_name();
            patronymicUser = user.getPatronymic();
            emailUser = user.getEmail();
            roleUser = user.getRole();
            permissionUser = user.getPermission();
            password = user.getPassword();

            requireNonNull(binding.textName.getEditText()).setText(nameUser);
            requireNonNull(binding.textLastName.getEditText()).setText(lastNameUser);
            requireNonNull(binding.textPatronymic.getEditText()).setText(patronymicUser);
            requireNonNull(binding.textEmail.getEditText()).setText(emailUser);
            requireNonNull(binding.textRole.getEditText()).setText(roleUser);
            requireNonNull(binding.textPermissions.getEditText()).setText(permissionUser);

            if (emailUser.equals("api@2122.pro"))
                binding.textPassword.getEditText().setText("Пароль? Какой пароль? ¯\\_(ツ)_/¯");
            else
                binding.textPassword.getEditText().setText(password);

            initFields();
        });

        binding.toolbarEditUser.setNavigationOnClickListener(v -> onBackPressed());
        binding.btnUpdateUser.setOnClickListener(v -> updateUser());
        binding.textPassword.setEndIconOnClickListener(v -> copyUserData());
    }

    private void initFields() {
        String[] items = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                EditUserActivity.this,
                R.layout.dropdown_menu_categories,
                items
        );

        binding.autoCompleteTextViewEditRoleUser.setAdapter(arrayAdapter);

        String[] permissions = getResources().getStringArray(R.array.permissions);
        ArrayAdapter<String> arrayAdapterPermission = new ArrayAdapter<>(
                EditUserActivity.this,
                R.layout.dropdown_menu_categories,
                permissions
        );

        binding.autoCompletePermissions.setAdapter(arrayAdapterPermission);
    }

    private void updateUser() {
        nameUser = requireNonNull(binding.textName.getEditText()).getText().toString();
        lastNameUser = requireNonNull(binding.textLastName.getEditText()).getText().toString();
        patronymicUser = requireNonNull(binding.textPatronymic.getEditText()).getText().toString();
        emailUser = requireNonNull(binding.textEmail.getEditText()).getText().toString();
        roleUser = requireNonNull(binding.textRole.getEditText()).getText().toString();
        permissionUser = requireNonNull(binding.textPermissions.getEditText()).getText().toString();
        password = requireNonNull(binding.textPassword.getEditText()).getText().toString();

        if(!validateFields()) {
            return;
        }

        if(!networkUtils.isOnline(EditUserActivity.this)) {
            Snackbar.make(findViewById(R.id.coordinator_login_activity),
                    getString(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        binding.progressBarEditUser.setVisibility(View.VISIBLE);

        userViewModel.updateUser(userID, new User(nameUser, lastNameUser, patronymicUser,
                emailUser, roleUser, permissionUser, password));

        binding.progressBarEditUser.setVisibility(View.INVISIBLE);
    }

    private void copyUserData() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        String email = binding.textEmail.getEditText().getText().toString();
        String password = binding.textPassword.getEditText().getText().toString();

        ClipData clip = ClipData.newPlainText(null, email + " " + password);
        clipboard.setPrimaryClip(clip);

        Snackbar.make(binding.baseLayout, "Логин и пароль пользователя скопированы", Snackbar.LENGTH_SHORT).show();
    }

    boolean validateFields() {
        return textValidator.isEmptyField(nameUser, binding.textName) &
                textValidator.isEmptyField(lastNameUser, binding.textLastName) &
                textValidator.isEmptyField(patronymicUser, binding.textPatronymic) &
                textValidator.isEmptyField(emailUser, binding.textEmail) &
                textValidator.isEmptyField(roleUser, binding.textRole) &
                textValidator.isEmptyField(permissionUser, binding.textPermissions) &
                textValidator.isEmptyField(password, binding.textPassword);
    }
}
