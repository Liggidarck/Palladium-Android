package com.george.vector.users.root.main;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.consts.Keys.USER_NOTIFICATIONS_OPTIONS;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_ROLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityRootMainBinding;
import com.george.vector.notifications.SendNotification;
import com.george.vector.users.root.main.fragments.help.FragmentHelp;
import com.george.vector.users.root.main.fragments.home.FragmentHome;
import com.george.vector.users.root.main.fragments.tasks.FragmentTasks;
import com.google.firebase.messaging.FirebaseMessaging;

public class RootMainActivity extends AppCompatActivity {

    private static final String TAG = "RootMainActivity";
    String email;

    ActivityRootMainBinding rootMainBinding;

    SharedPreferences mDataUser;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        rootMainBinding = ActivityRootMainBinding.inflate(getLayoutInflater());
        setContentView(rootMainBinding.getRoot());

        mDataUser = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mDataUser.edit();

        String name_user = mDataUser.getString(USER_PREFERENCES_NAME, "");
        String last_name_user = mDataUser.getString(USER_PREFERENCES_LAST_NAME, "");
        String patronymic_user = mDataUser.getString(USER_PREFERENCES_PATRONYMIC, "");
        email = mDataUser.getString(USER_PREFERENCES_EMAIL, "");
        String role_user = mDataUser.getString(USER_PREFERENCES_ROLE, "");
        String permission_user = mDataUser.getString(USER_PREFERENCES_PERMISSION, "");
        boolean notifications = mDataUser.getBoolean(USER_NOTIFICATIONS_OPTIONS, false);

        Log.d(TAG, "NOTIFICATIONS: " + notifications);

        if (savedInstanceState == null) {
            Fragment fragmentHome = new FragmentHome();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, fragmentHome).commit();
        }

        if (!notifications) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Подключить уведомления")
                    .setMessage("Вам необходимо подключить уведомления о созданиии новой заявке.")
                    .setPositiveButton("Подключить", (dialog, which) -> {
                        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEW_TASKS_CREATE);
                        editor.putBoolean(USER_NOTIFICATIONS_OPTIONS, true);
                        editor.apply();
                        Log.d(TAG, "NOTIFICATIONS: " + notifications);
                        recreate();
                    })
                    .setCancelable(false)
                    .create();
            alertDialog.show();
        } else {
            SendNotification sendNotification = new SendNotification();
            sendNotification.sendNotification(
                    "Новый пользователь зарегестрирован на получение уведомлений",
                    name_user + " " + last_name_user,
                    TOPIC_NEW_TASKS_CREATE
            );
        }

        rootMainBinding.bottomRootNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.d(TAG, "Start home root fragment");
                    selectedFragment = new FragmentHome();

                    Bundle email = new Bundle();
                    email.putString(EMAIL, this.email);
                    selectedFragment.setArguments(email);

                    break;

                case R.id.nav_tasks:
                    Log.d(TAG, "Start tasks root fragment");
                    selectedFragment = new FragmentTasks();

                    Bundle bundle = new Bundle();
                    bundle.putString(EMAIL, this.email);
                    selectedFragment.setArguments(bundle);

                    break;

                case R.id.nav_help:
                    Log.d(TAG, "Start profile root fragment");
                    selectedFragment = new FragmentHelp();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, selectedFragment).commit();

            return true;
        });

    }

}