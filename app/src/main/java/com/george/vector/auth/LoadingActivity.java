package com.george.vector.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.admin.MainAdminActivity;
import com.george.vector.caretaker.main.MainCaretakerActivity;
import com.george.vector.executor.main.MainExecutorActivity;
import com.george.vector.root.main.RootMainActivity;
import com.george.vector.user.main.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = "LoadingActivity";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            Log.i(TAG, "user id: " + userID);

            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, (value, error) -> {
                assert value != null;

                String check_role = value.getString("role");
                String check_email = value.getString("email");
                String permission = value.getString("permission");
                Log.d(TAG, "permission - " + permission);

                assert check_role != null;
                startApp(check_role, check_email, permission);

            });
        } else
            startActivity(new Intent(this, ActivityLogin.class));
    }

    void startApp(@NotNull String role, String email, String permission) {
        if (role.equals("Root"))
            startActivity(new Intent(this, RootMainActivity.class));

        if(role.equals("Завхоз")) {
            Intent intent = new Intent(this, MainCaretakerActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if(role.equals("Администратор")) {
            Intent intent = new Intent(this, MainAdminActivity.class);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if (role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("permission", permission);
            startActivity(intent);
        }

        if (role.equals("Исполнитель")) {
            Intent intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }

}