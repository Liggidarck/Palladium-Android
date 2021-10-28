package com.george.vector.auth;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LAST_NAME;
import static com.george.vector.common.consts.Keys.NAME;
import static com.george.vector.common.consts.Keys.PATRONYMIC;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.ROLE;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Keys.USER_DATA;
import static com.george.vector.common.consts.Keys.USER_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_ROLE;
import static com.george.vector.common.consts.Logs.TAG_LOADING_ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.databinding.ActivityLoadingBinding;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {

    FirebaseAuth firebase_auth;
    FirebaseFirestore firebase_firestore;

    String user_id, name, last_name, patronymic, email, permission, role;

    ActivityLoadingBinding loadingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(loadingBinding.getRoot());

        firebase_auth = FirebaseAuth.getInstance();
        firebase_firestore = FirebaseFirestore.getInstance();

        SharedPreferences mDataUser;
        mDataUser = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);

        if (mDataUser.contains(USER_DATA)) {
            name = mDataUser.getString(NAME, "");
            last_name = mDataUser.getString(LAST_NAME, "");
            patronymic = mDataUser.getString(PATRONYMIC, "");
            email = mDataUser.getString(EMAIL, "");
            permission = mDataUser.getString(USER_PERMISSION, "");
            role = mDataUser.getString(USER_ROLE, "");
        }

        if(firebase_auth.getCurrentUser() != null) {
            user_id = Objects.requireNonNull(firebase_auth.getCurrentUser()).getUid();
            Log.i(TAG_LOADING_ACTIVITY, "user id: " + user_id);

            DocumentReference documentReference = firebase_firestore.collection(USERS).document(user_id);
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