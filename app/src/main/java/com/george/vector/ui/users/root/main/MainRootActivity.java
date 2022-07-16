package com.george.vector.ui.users.root.main;

import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.consts.Keys.USER_NOTIFICATIONS_OPTIONS;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Logs.TAG_MAIN_ROOT_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.george.vector.R;
import com.george.vector.common.notifications.SendNotification;
import com.george.vector.databinding.ActivityRootMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainRootActivity extends AppCompatActivity {

    String email;

    ActivityRootMainBinding binding;
    SharedPreferences sharedPreferences;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityRootMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentActivityRootMain);
        NavigationUI.setupWithNavController(binding.bottomRootNavigation, navController);

        String nameUser = sharedPreferences.getString(USER_PREFERENCES_NAME, "");
        String lastNameUser = sharedPreferences.getString(USER_PREFERENCES_LAST_NAME, "");
        email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");
        boolean notifications = sharedPreferences.getBoolean(USER_NOTIFICATIONS_OPTIONS, false);

        Log.d(TAG_MAIN_ROOT_ACTIVITY, "NOTIFICATIONS: " + notifications);

        if (!notifications) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Подключить уведомления")
                    .setMessage("Вам необходимо подключить уведомления о создании новой заявки.")
                    .setPositiveButton("Подключить", (dialog, which) -> {
                        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEW_TASKS_CREATE);
                        editor.putBoolean(USER_NOTIFICATIONS_OPTIONS, true);
                        editor.apply();
                        Log.d(TAG_NOTIFICATIONS, "NOTIFICATIONS STATE: " + notifications);

                        SendNotification sendNotification = new SendNotification();
                        sendNotification.sendNotification(
                                "Новый пользователь зарегестрирован на получение уведомлений",
                                nameUser + " " + lastNameUser,
                                TOPIC_NEW_TASKS_CREATE
                        );
                    })
                    .setCancelable(false)
                    .create();
            alertDialog.show();
        }
    }

}