package com.george.vector.ui.edit_users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityEditDataUserBinding;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditDataUserActivity extends AppCompatActivity {

    private static final String TAG = "EditDataUserActivity";

    String nameUser, lastNameUser, patronymicUser, emailUser, roleUser, userId, password;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ActivityEditDataUserBinding editDataUserBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editDataUserBinding = ActivityEditDataUserBinding.inflate(getLayoutInflater());
        setContentView(editDataUserBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        editDataUserBinding.topAppBarEditDataUser.setNavigationOnClickListener(v -> onBackPressed());

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            nameUser = value.getString("name");
            lastNameUser = value.getString("last_name");
            patronymicUser = value.getString("patronymic");
            emailUser = value.getString("email");
            roleUser = value.getString("role");
            password = value.getString("password");
            Log.i(TAG, String.format("name: %s last_name: %s patronymic: %s email: %s", nameUser, lastNameUser, patronymicUser, emailUser));

            Objects.requireNonNull(editDataUserBinding.textInputLayoutNameUserEdit.getEditText()).setText(nameUser);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutLastNameUserEdit.getEditText()).setText(lastNameUser);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutPatronymicUserEdit.getEditText()).setText(patronymicUser);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutEmailUserEdit.getEditText()).setText(emailUser);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutRoleUserEdit.getEditText()).setText(roleUser);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutPasswordUserEdit.getEditText()).setText(password);
        });

        editDataUserBinding.btnSaveEditDataUser.setOnClickListener(v -> {
            nameUser = Objects.requireNonNull(editDataUserBinding.textInputLayoutNameUserEdit.getEditText()).getText().toString();
            lastNameUser = Objects.requireNonNull(editDataUserBinding.textInputLayoutLastNameUserEdit.getEditText()).getText().toString();
            patronymicUser = Objects.requireNonNull(editDataUserBinding.textInputLayoutPatronymicUserEdit.getEditText()).getText().toString();
            emailUser = Objects.requireNonNull(editDataUserBinding.textInputLayoutEmailUserEdit.getEditText()).getText().toString();
            roleUser = Objects.requireNonNull(editDataUserBinding.textInputLayoutRoleUserEdit.getEditText()).getText().toString();
            password = Objects.requireNonNull(editDataUserBinding.textInputLayoutPasswordUserEdit.getEditText()).getText().toString();

            if(validateFields()) {
                editDataUserBinding.progressBarEditDataUser.setVisibility(View.VISIBLE);

                Map<String, Object> user = new HashMap<>();
                user.put("name", nameUser);
                user.put("last_name", lastNameUser);
                user.put("patronymic", patronymicUser);
                user.put("email", emailUser);
                user.put("role", roleUser);
                user.put("password", password);

                documentReference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.i(TAG, "update completed!");
                        editDataUserBinding.progressBarEditDataUser.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(this, MainUserActivity.class);
                        intent.putExtra("email", emailUser);
                        startActivity(intent);
                    } else
                        Log.i(TAG, "Error: " + task.getException());
                    
                });

                documentReference.set(user)
                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userId))
                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e));
            }
        });
    }

    boolean validateFields(){
        Utils utils = new Utils();

        utils.clearError(editDataUserBinding.textInputLayoutNameUserEdit);
        utils.clearError(editDataUserBinding.textInputLayoutLastNameUserEdit);
        utils.clearError(editDataUserBinding.textInputLayoutPatronymicUserEdit);
        utils.clearError(editDataUserBinding.textInputLayoutEmailUserEdit);

        boolean checkName = utils.validateField(nameUser, editDataUserBinding.textInputLayoutNameUserEdit);
        boolean checkLastName = utils.validateField(lastNameUser, editDataUserBinding.textInputLayoutLastNameUserEdit);
        boolean checkPatronymic = utils.validateField(patronymicUser, editDataUserBinding.textInputLayoutPatronymicUserEdit);
        boolean checkEmail = utils.validateField(emailUser, editDataUserBinding.textInputLayoutEmailUserEdit);

        return checkName & checkLastName & checkPatronymic & checkEmail;
    }

}