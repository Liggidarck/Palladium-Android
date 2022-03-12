package com.george.vector.users.root.profile;

import static com.george.vector.common.consts.Logs.TAG_EDIT_USER_ACTIVITY;

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
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity {

    String nameUser, lastNameUser, patronymicUser, emailUser, roleUser, permissionUser, userID, password;
    FirebaseFirestore firebaseFirestore;

    EditUserActivityBinding editUserActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

            Log.i(TAG_EDIT_USER_ACTIVITY, "name: " + nameUser);
            Log.i(TAG_EDIT_USER_ACTIVITY, "last_name: " + lastNameUser);
            Log.i(TAG_EDIT_USER_ACTIVITY, "patronymic: " + patronymicUser);
            Log.i(TAG_EDIT_USER_ACTIVITY, "email: " + emailUser);
            Log.i(TAG_EDIT_USER_ACTIVITY, "role: " + roleUser);

            Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditNameUser.getEditText()).setText(nameUser);
            Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditLastNameUser.getEditText()).setText(lastNameUser);
            Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditPatronymicUser.getEditText()).setText(patronymicUser);
            Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditEmailUser.getEditText()).setText(emailUser);
            Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditRoleUser.getEditText()).setText(roleUser);
            Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditPermissionUser.getEditText()).setText(permissionUser);

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

        editUserActivityBinding.updateUserBtn.setOnClickListener(v -> {
            nameUser = Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditNameUser.getEditText()).getText().toString();
            lastNameUser = Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditLastNameUser.getEditText()).getText().toString();
            patronymicUser = Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditPatronymicUser.getEditText()).getText().toString();
            emailUser = Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditEmailUser.getEditText()).getText().toString();
            roleUser = Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditRoleUser.getEditText()).getText().toString();
            permissionUser = Objects.requireNonNull(editUserActivityBinding.textInputLayoutEditPermissionUser.getEditText()).getText().toString();
            password = Objects.requireNonNull(editUserActivityBinding.textInputLayoutPasswordUser.getEditText()).getText().toString();

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

        });

        editUserActivityBinding.textInputLayoutPasswordUser.setEndIconOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            String email = editUserActivityBinding.textInputLayoutEditEmailUser.getEditText().getText().toString();
            String password = editUserActivityBinding.textInputLayoutPasswordUser.getEditText().getText().toString();

            ClipData clip = ClipData.newPlainText(null,email + " " + password);
            clipboard.setPrimaryClip(clip);

            Snackbar.make(editUserActivityBinding.baseLayout, "Логин и пароль пользователя скопированы", Snackbar.LENGTH_SHORT).show();
        });

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
