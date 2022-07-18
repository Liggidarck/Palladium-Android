package com.george.vector.ui.users.root.profile;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.consts.Keys.USER_NOTIFICATIONS_OPTIONS;
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
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.BuildConfig;
import com.george.vector.R;
import com.george.vector.ui.auth.LoginActivity;
import com.george.vector.ui.settings.SettingsActivity;
import com.george.vector.databinding.ActivityProfileRootBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileRootActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    SharedPreferences mDataUser;
    String name, lastName, patronymic, email, role, permission;

    ActivityProfileRootBinding profileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileRootBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        mDataUser = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mDataUser.edit();

        name = mDataUser.getString(USER_PREFERENCES_NAME, "");
        lastName = mDataUser.getString(USER_PREFERENCES_LAST_NAME, "");
        patronymic = mDataUser.getString(USER_PREFERENCES_PATRONYMIC, "");
        email = mDataUser.getString(USER_PREFERENCES_EMAIL, "");
        role = mDataUser.getString(USER_PREFERENCES_ROLE, "");
        permission = mDataUser.getString(USER_PREFERENCES_PERMISSION, "");

        String _name = Character.toString(name.charAt(0));
        String _last_name = Character.toString(lastName.charAt(0));

        profileBinding.nameRootProfile.setText(String.format("%s %s", name, lastName));
        profileBinding.avaRootProfile.setText(String.format("%s%s", _name, _last_name));
        profileBinding.externalDataUser.setText(String.format("%s %s", email, role));

        profileBinding.toolbarProfileRoot.setNavigationOnClickListener(v -> onBackPressed());
        profileBinding.layoutNewPersonProfile.setOnClickListener(v -> startActivity(new Intent(ProfileRootActivity.this, RegisterUserActivity.class)));
        profileBinding.layoutEditPersonProfile.setOnClickListener(v -> startActivity(new Intent(ProfileRootActivity.this, ListUsersActivity.class)));

        profileBinding.settingsProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileRootActivity.this, SettingsActivity.class);
            intent.putExtra(PERMISSION, "root");
            intent.putExtra(EMAIL, "null");
            startActivity(intent);
        });

        profileBinding.logoutBtn.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("ok", (dialog1, which) -> {
                        firebaseAuth.signOut();

                        editor.putString(USER_PREFERENCES_NAME, "");
                        editor.putString(USER_PREFERENCES_LAST_NAME, "");
                        editor.putString(USER_PREFERENCES_PATRONYMIC, "");
                        editor.putString(USER_PREFERENCES_EMAIL, "");
                        editor.putString(USER_PREFERENCES_ROLE, "");
                        editor.putString(USER_PREFERENCES_PERMISSION, "");
                        editor.putBoolean(USER_NOTIFICATIONS_OPTIONS, false);
                        editor.apply();

                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NEW_TASKS_CREATE);

                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();


        });

        String versionName = "Версия: " + BuildConfig.VERSION_NAME;
        profileBinding.versionAppTextView.setText(versionName);
    }
}