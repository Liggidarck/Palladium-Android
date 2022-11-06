package com.george.vector.ui.users.executor;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.databinding.ActivityEditDataUserBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class EditDataUserActivity extends AppCompatActivity {

    ActivityEditDataUserBinding binding;
    TextValidatorUtils textValidator = new TextValidatorUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityEditDataUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.toolbarEditDataUser.setNavigationOnClickListener(v -> onBackPressed());


        binding.btnSaveUser.setOnClickListener(v -> {
            binding.progressBarEditDataUser.setVisibility(View.VISIBLE);
            String nameUser = requireNonNull(binding.textNameUser.getEditText()).getText().toString();
            String lastNameUser = requireNonNull(binding.textLastNameUser.getEditText()).getText().toString();
            String patronymicUser = requireNonNull(binding.textPatronymicUser.getEditText()).getText().toString();
            String emailUser = requireNonNull(binding.textEmailUser.getEditText()).getText().toString();
            String roleUser = requireNonNull(binding.textRoleUser.getEditText()).getText().toString();
            String password = requireNonNull(binding.textPasswordUser.getEditText()).getText().toString();

            if (!validateFields()) {
                return;
            }

            binding.progressBarEditDataUser.setVisibility(View.INVISIBLE);
            onBackPressed();
        });

    }

    boolean validateFields() {
        String nameUser = binding.textNameUser.getEditText().getText().toString();
        String lastNameUser = binding.textLastNameUser.getEditText().getText().toString();
        String patronymicUser = binding.textPatronymicUser.getEditText().getText().toString();
        String emailUser = binding.textEmailUser.getEditText().getText().toString();
        String roleUser = binding.textRoleUser.getEditText().getText().toString();
        String password = binding.textPasswordUser.getEditText().getText().toString();

        return textValidator.isEmptyField(nameUser, binding.textNameUser) &
                textValidator.isEmptyField(lastNameUser, binding.textLastNameUser) &
                textValidator.isEmptyField(patronymicUser, binding.textPatronymicUser) &
                textValidator.isEmptyField(emailUser, binding.textEmailUser) &
                textValidator.isEmptyField(roleUser, binding.textRoleUser) &
                textValidator.isEmptyField(password, binding.textPasswordUser);
    }

}