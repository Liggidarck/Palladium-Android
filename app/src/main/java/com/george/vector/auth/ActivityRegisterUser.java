package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityRegisterUser extends AppCompatActivity {

    MaterialToolbar topAppBar_register;
    Button register_user_btn;
    TextInputLayout text_input_layout_name_user, text_input_layout_last_name_user,
            text_input_layout_patronymic_user, text_input_layout_email_user,
            text_input_layout_password_user, text_input_layout_role_user;
    MaterialAutoCompleteTextView auto_complete_text_view_role_user;

    String name_user, last_name_user, patronymic_user, email_user, password_user, role_user, userID;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        topAppBar_register = findViewById(R.id.topAppBar_register);
        register_user_btn = findViewById(R.id.register_user_btn);
        text_input_layout_name_user = findViewById(R.id.text_input_layout_name_user);
        text_input_layout_last_name_user = findViewById(R.id.text_input_layout_last_name_user);
        text_input_layout_patronymic_user = findViewById(R.id.text_input_layout_patronymic_user);
        text_input_layout_email_user = findViewById(R.id.text_input_layout_email_user);
        text_input_layout_password_user = findViewById(R.id.text_input_layout_password_user);
        text_input_layout_role_user = findViewById(R.id.text_input_layout_role_user);
        auto_complete_text_view_role_user = findViewById(R.id.auto_complete_text_view_role_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        topAppBar_register.setNavigationOnClickListener(v -> onBackPressed());

        String[] items = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ActivityRegisterUser.this,
                R.layout.dropdown_menu_categories,
                items
        );

        auto_complete_text_view_role_user.setAdapter(arrayAdapter);

        register_user_btn.setOnClickListener(v -> {
            name_user = Objects.requireNonNull(text_input_layout_name_user.getEditText()).getText().toString();
            last_name_user = Objects.requireNonNull(text_input_layout_last_name_user.getEditText()).getText().toString();
            patronymic_user = Objects.requireNonNull(text_input_layout_patronymic_user.getEditText()).getText().toString();
            email_user = Objects.requireNonNull(text_input_layout_email_user.getEditText()).getText().toString();
            password_user = Objects.requireNonNull(text_input_layout_password_user.getEditText()).getText().toString();
            role_user = Objects.requireNonNull(text_input_layout_role_user.getEditText()).getText().toString();


            firebaseAuth.createUserWithEmailAndPassword(email_user, password_user).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(ActivityRegisterUser.this, "User Added", Toast.LENGTH_LONG).show();

                    userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("name", name_user);
                    user.put("last_name", last_name_user);
                    user.put("patronymic", patronymic_user);
                    user.put("email", email_user);
                    user.put("role", role_user);
                    documentReference.set(user)
                            .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user - " + userID))
                            .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));

                } else {
                    Toast.makeText(ActivityRegisterUser.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                }


            });

        });

    }
}