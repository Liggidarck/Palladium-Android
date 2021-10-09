package com.george.vector.auth;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.ROLE;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Logs.TAG_LOADING_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {

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
            Log.i(TAG_LOADING_ACTIVITY, "user id: " + userID);

            DocumentReference documentReference = firebaseFirestore.collection(USERS).document(userID);
            documentReference.addSnapshotListener(this, (value, error) -> {
                assert value != null;

                String role = value.getString(ROLE);
                String email = value.getString(EMAIL);
                String permission = value.getString(PERMISSION);
                Log.d(TAG_LOADING_ACTIVITY, "permission - " + permission);
                Log.d(TAG_LOADING_ACTIVITY, "email - " + email);
                Log.d(TAG_LOADING_ACTIVITY, "role - " + role);

                assert role != null;
                startApp(role, email, permission);

            });
        } else
            startActivity(new Intent(this, LoginActivity.class));
    }

    void startApp(@NotNull String role, String email, String permission) {
        if (role.equals("Root")) {
            Intent intent = new Intent(this, RootMainActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        }

        if (role.equals("Пользователь")) {
            Intent intent = new Intent(this, MainUserActivity.class);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);
        }

        if (role.equals("Исполнитель")) {
            Intent intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        }
    }

}