package com.george.vector.ui.users.root.profile;

import static com.george.vector.common.consts.Logs.TAG_EDIT_USER_ACTIVITY;
import static java.util.Objects.requireNonNull;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.EditUserActivityBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    String nameUser, lastNameUser, patronymicUser, emailUser, roleUser, permissionUser, userID, password;
    FirebaseFirestore firebaseFirestore;

    EditUserActivityBinding editUserActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        editUserActivityBinding = EditUserActivityBinding.inflate(getLayoutInflater());
        setContentView(editUserActivityBinding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        userID = arguments.get("user_id").toString();

        editUserActivityBinding.topAppBarEdit.setNavigationOnClickListener(v -> onBackPressed());

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            nameUser = value.getString("name");
            lastNameUser = value.getString("last_name");
            patronymicUser = value.getString("patronymic");
            emailUser = value.getString("email");
            roleUser = value.getString("role");
            permissionUser = value.getString("permission");
            password = value.getString("password");

            requireNonNull(editUserActivityBinding.textInputLayoutEditNameUser.getEditText()).setText(nameUser);
            requireNonNull(editUserActivityBinding.textInputLayoutEditLastNameUser.getEditText()).setText(lastNameUser);
            requireNonNull(editUserActivityBinding.textInputLayoutEditPatronymicUser.getEditText()).setText(patronymicUser);
            requireNonNull(editUserActivityBinding.textInputLayoutEditEmailUser.getEditText()).setText(emailUser);
            requireNonNull(editUserActivityBinding.textInputLayoutEditRoleUser.getEditText()).setText(roleUser);
            requireNonNull(editUserActivityBinding.textInputLayoutEditPermissionUser.getEditText()).setText(permissionUser);

            if (emailUser.equals("api@2122.pro"))
                editUserActivityBinding.textInputLayoutPasswordUser.getEditText().setText("Пароль? Какой пароль? ¯\\_(ツ)_/¯");
            else
                editUserActivityBinding.textInputLayoutPasswordUser.getEditText().setText(password);

            String[] items = getResources().getStringArray(R.array.roles);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    EditUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            editUserActivityBinding.autoCompleteTextViewEditRoleUser.setAdapter(arrayAdapter);

            String[] permissions = getResources().getStringArray(R.array.permissions);
            ArrayAdapter<String> arrayAdapterPermission = new ArrayAdapter<>(
                    EditUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    permissions
            );

            editUserActivityBinding.autoCompleteTextViewEditPermissionUser.setAdapter(arrayAdapterPermission);

        });

        editUserActivityBinding.updateUserBtn.setOnClickListener(v -> updateUser(documentReference));

        editUserActivityBinding.textInputLayoutPasswordUser.setEndIconOnClickListener(v -> copyUserData());

    }

    private void updateUser(DocumentReference documentReference) {
        nameUser = requireNonNull(editUserActivityBinding.textInputLayoutEditNameUser.getEditText()).getText().toString();
        lastNameUser = requireNonNull(editUserActivityBinding.textInputLayoutEditLastNameUser.getEditText()).getText().toString();
        patronymicUser = requireNonNull(editUserActivityBinding.textInputLayoutEditPatronymicUser.getEditText()).getText().toString();
        emailUser = requireNonNull(editUserActivityBinding.textInputLayoutEditEmailUser.getEditText()).getText().toString();
        roleUser = requireNonNull(editUserActivityBinding.textInputLayoutEditRoleUser.getEditText()).getText().toString();
        permissionUser = requireNonNull(editUserActivityBinding.textInputLayoutEditPermissionUser.getEditText()).getText().toString();
        password = requireNonNull(editUserActivityBinding.textInputLayoutPasswordUser.getEditText()).getText().toString();

        if (validateFields()) {
            editUserActivityBinding.progressBarEditUser.setVisibility(View.VISIBLE);

            Map<String, Object> user = new HashMap<>();
            user.put("name", nameUser);
            user.put("last_name", lastNameUser);
            user.put("patronymic", patronymicUser);
            user.put("email", emailUser);
            user.put("role", roleUser);
            user.put("permission", permissionUser);
            user.put("password", password);

            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(TAG_EDIT_USER_ACTIVITY, "update completed!");
                    editUserActivityBinding.progressBarEditUser.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(this, ListUsersActivity.class));
                } else
                    Log.e(TAG_EDIT_USER_ACTIVITY, "Error: " + task.getException());
            });

            documentReference.set(user)
                    .addOnSuccessListener(unused -> Log.d(TAG_EDIT_USER_ACTIVITY, "onSuccess: user - " + userID))
                    .addOnFailureListener(e -> Log.e("TAG", "Failure - " + e));

        }
    }

    private void copyUserData() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        String email = editUserActivityBinding.textInputLayoutEditEmailUser.getEditText().getText().toString();
        String password = editUserActivityBinding.textInputLayoutPasswordUser.getEditText().getText().toString();

        ClipData clip = ClipData.newPlainText(null,email + " " + password);
        clipboard.setPrimaryClip(clip);

        Snackbar.make(editUserActivityBinding.baseLayout, "Логин и пароль пользователя скопированы", Snackbar.LENGTH_SHORT).show();
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clearError(editUserActivityBinding.textInputLayoutEditNameUser);
        utils.clearError(editUserActivityBinding.textInputLayoutEditLastNameUser);
        utils.clearError(editUserActivityBinding.textInputLayoutEditPatronymicUser);
        utils.clearError(editUserActivityBinding.textInputLayoutEditEmailUser);
        utils.clearError(editUserActivityBinding.textInputLayoutEditRoleUser);

        boolean checkName = utils.validateField(nameUser, editUserActivityBinding.textInputLayoutEditNameUser);
        boolean checkLastName = utils.validateField(lastNameUser, editUserActivityBinding.textInputLayoutEditLastNameUser);
        boolean checkPatronymic = utils.validateField(patronymicUser, editUserActivityBinding.textInputLayoutEditPatronymicUser);
        boolean checkEmail = utils.validateField(emailUser, editUserActivityBinding.textInputLayoutEditEmailUser);
        boolean checkRole = utils.validateField(roleUser, editUserActivityBinding.textInputLayoutEditRoleUser);

        return checkName & checkLastName & checkPatronymic & checkEmail & checkRole;
    }
}
