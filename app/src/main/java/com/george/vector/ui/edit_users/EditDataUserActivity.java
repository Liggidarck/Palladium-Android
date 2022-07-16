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

    String name_user, last_name_user, patronymic_user, email_user, role_user, user_id, password;

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

        user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(user_id);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            name_user = value.getString("name");
            last_name_user = value.getString("last_name");
            patronymic_user = value.getString("patronymic");
            email_user = value.getString("email");
            role_user = value.getString("role");
            password = value.getString("password");
            Log.i(TAG, String.format("name: %s last_name: %s patronymic: %s email: %s", name_user, last_name_user, patronymic_user, email_user));

            Objects.requireNonNull(editDataUserBinding.textInputLayoutNameUserEdit.getEditText()).setText(name_user);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutLastNameUserEdit.getEditText()).setText(last_name_user);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutPatronymicUserEdit.getEditText()).setText(patronymic_user);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutEmailUserEdit.getEditText()).setText(email_user);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutRoleUserEdit.getEditText()).setText(role_user);
            Objects.requireNonNull(editDataUserBinding.textInputLayoutPasswordUserEdit.getEditText()).setText(password);
        });

        editDataUserBinding.btnSaveEditDataUser.setOnClickListener(v -> {
            name_user = Objects.requireNonNull(editDataUserBinding.textInputLayoutNameUserEdit.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(editDataUserBinding.textInputLayoutLastNameUserEdit.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(editDataUserBinding.textInputLayoutPatronymicUserEdit.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(editDataUserBinding.textInputLayoutEmailUserEdit.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(editDataUserBinding.textInputLayoutRoleUserEdit.getEditText()).getText().toString();
            password = Objects.requireNonNull(editDataUserBinding.textInputLayoutPasswordUserEdit.getEditText()).getText().toString();

            if(validateFields()) {
                editDataUserBinding.progressBarEditDataUser.setVisibility(View.VISIBLE);

                Map<String, Object> user = new HashMap<>();
                user.put("name", name_user);
                user.put("last_name", last_name_user);
                user.put("patronymic", patronymic_user);
                user.put("email", email_user);
                user.put("role", role_user);
                user.put("password", password);

                documentReference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.i(TAG, "update completed!");
                        editDataUserBinding.progressBarEditDataUser.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(this, MainUserActivity.class);
                        intent.putExtra("email", email_user);
                        startActivity(intent);
                    } else
                        Log.i(TAG, "Error: " + task.getException());
                    
                });

                documentReference.set(user)
                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + user_id))
                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e));
            }
        });
    }

    boolean validateFields(){
        Utils utils = new Utils();

        utils.clear_error(editDataUserBinding.textInputLayoutNameUserEdit);
        utils.clear_error(editDataUserBinding.textInputLayoutLastNameUserEdit);
        utils.clear_error(editDataUserBinding.textInputLayoutPatronymicUserEdit);
        utils.clear_error(editDataUserBinding.textInputLayoutEmailUserEdit);

        boolean checkName = utils.validate_field(name_user, editDataUserBinding.textInputLayoutNameUserEdit);
        boolean checkLastName = utils.validate_field(last_name_user, editDataUserBinding.textInputLayoutLastNameUserEdit);
        boolean checkPatronymic = utils.validate_field(patronymic_user, editDataUserBinding.textInputLayoutPatronymicUserEdit);
        boolean checkEmail = utils.validate_field(email_user, editDataUserBinding.textInputLayoutEmailUserEdit);

        return checkName & checkLastName & checkPatronymic & checkEmail;
    }

}