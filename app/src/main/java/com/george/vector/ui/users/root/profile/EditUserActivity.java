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
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.EditUserActivityBinding;
import com.george.vector.network.model.user.EditUserModel;
import com.george.vector.network.model.user.Role;
import com.george.vector.network.model.user.User;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EditUserActivity extends AppCompatActivity {

    private long userID;
    private String nameUser;
    private String lastNameUser;
    private String patronymicUser;
    private String emailUser;
    private String roleUser;
    private String zoneUser;
    private String password;
    private String username;

    private EditUserActivityBinding binding;

    private UserViewModel userViewModel;

    private final NetworkUtils networkUtils = new NetworkUtils();
    private final TextValidatorUtils textValidator = new TextValidatorUtils();

    public static final String TAG = EditUserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = EditUserActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        userID = arguments.getLong("user_id");

        initViewModels();

        userViewModel.getUserById(userID).observe(this, user -> {
            List<Role> roles = user.getRoles();

            nameUser = user.getName();
            lastNameUser = user.getLastName();
            patronymicUser = user.getPatronymic();
            emailUser = user.getEmail();
            roleUser = roles.get(0).getName();
            zoneUser = user.getZone();
            password = user.getPassword();
            username = user.getUsername();

            requireNonNull(binding.textName.getEditText()).setText(nameUser);
            requireNonNull(binding.textLastName.getEditText()).setText(lastNameUser);
            requireNonNull(binding.textPatronymic.getEditText()).setText(patronymicUser);
            requireNonNull(binding.textEmail.getEditText()).setText(emailUser);
            requireNonNull(binding.textRole.getEditText()).setText(roleUser);
            requireNonNull(binding.textPermissions.getEditText()).setText(zoneUser);
            requireNonNull(binding.textUsername.getEditText()).setText(username);

            initFields();
        });

        binding.toolbarEditUser.setNavigationOnClickListener(v -> onBackPressed());
        binding.btnUpdateUser.setOnClickListener(v -> updateUser());

    }

    private void initViewModels() {
        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(UserViewModel.class);
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
        zoneUser = requireNonNull(binding.textPermissions.getEditText()).getText().toString();
        username = requireNonNull(binding.textUsername.getEditText()).getText().toString();

        if(!validateFields()) {
            return;
        }

        if(!networkUtils.isOnline(EditUserActivity.this)) {
            Snackbar.make(findViewById(R.id.coordinator_login_activity),
                    getString(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        binding.progressBarEditUser.setVisibility(View.VISIBLE);

        List<String> roles = new ArrayList<>();
        roles.add(roleUser);

        //todo: add edit user

        EditUserModel user = new EditUserModel(zoneUser, nameUser, lastNameUser, patronymicUser, emailUser,
                username, roles);

        userViewModel.updateUser(user, userID);

        binding.progressBarEditUser.setVisibility(View.INVISIBLE);
    }


    boolean validateFields() {
        return textValidator.isEmptyField(nameUser, binding.textName) &
                textValidator.isEmptyField(lastNameUser, binding.textLastName) &
                textValidator.isEmptyField(patronymicUser, binding.textPatronymic) &
                textValidator.isEmptyField(emailUser, binding.textEmail) &
                textValidator.isEmptyField(roleUser, binding.textRole) &
                textValidator.isEmptyField(zoneUser, binding.textPermissions) &
                textValidator.isEmptyField(username, binding.textUsername);
    }
}
