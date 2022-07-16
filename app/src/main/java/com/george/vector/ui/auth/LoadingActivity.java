package com.george.vector.ui.auth;

import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_ROLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.databinding.ActivityLoadingBinding;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.RootMainActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoadingActivity extends AppCompatActivity {

    FirebaseAuth firebase_auth;
    FirebaseFirestore firebase_firestore;

    String name, last_name, patronymic, email, permission, role;
    private static final String TAG = "LoadingActivity";

    ActivityLoadingBinding loadingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(loadingBinding.getRoot());

        firebase_auth = FirebaseAuth.getInstance();
        firebase_firestore = FirebaseFirestore.getInstance();

        SharedPreferences mDataUser;
        mDataUser = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);

        name = mDataUser.getString(USER_PREFERENCES_NAME, "");
        last_name = mDataUser.getString(USER_PREFERENCES_LAST_NAME, "");
        patronymic = mDataUser.getString(USER_PREFERENCES_PATRONYMIC, "");
        email = mDataUser.getString(USER_PREFERENCES_EMAIL, "");
        permission = mDataUser.getString(USER_PREFERENCES_PERMISSION, "");
        role = mDataUser.getString(USER_PREFERENCES_ROLE, "");

        Log.d(TAG, "Auth id: " + firebase_auth.getUid());
        Log.d(TAG, "name: " + name);

        if(firebase_auth.getCurrentUser() != null &
                (name.equals("") || last_name.equals("") || patronymic.equals("") ||
                        email.equals("") || permission.equals("") || role.equals(""))) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Внимание!")
                    .setMessage("Необходимо войти в аккаунт снова. Если вы не помните совой логин, обратитесь в техническую поддрежку")
                    .setNegativeButton("Помощь", (dialog1, which) -> {

                        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "liggidarck@gmail.com", null));
                        intent.putExtra("android.intent.extra.SUBJECT", "Помощь с восстановлением доступа к приложению");
                        startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));

                    })
                    .setPositiveButton("ок", (dialog12, which) -> {
                        firebase_auth.signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                    })
                    .create();
            dialog.show();
        }

        if (firebase_auth.getCurrentUser() != null & !role.equals("")) {
            startApp(role);
        }

        if(firebase_auth.getCurrentUser() == null & role.equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    void startApp(String role) {
        if (role.equals("Root"))
            startActivity(new Intent(this, RootMainActivity.class));

        if (role.equals("Пользователь"))
            startActivity(new Intent(this, MainUserActivity.class));

        if (role.equals("Исполнитель"))
            startActivity(new Intent(this, MainExecutorActivity.class));


        finish();
    }

}