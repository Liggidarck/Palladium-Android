package com.george.vector.ui.users.root.main;

import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.utils.consts.Logs.TAG_MAIN_ROOT_ACTIVITY;
import static com.george.vector.common.utils.consts.Logs.TAG_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.george.vector.R;
import com.george.vector.common.notifications.SendNotification;
import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.ActivityRootMainBinding;
import com.george.vector.network.model.User;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainRootActivity extends AppCompatActivity {

    ActivityRootMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityRootMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserPreferencesViewModel preferencesViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentActivityRootMain);
        NavigationUI.setupWithNavController(binding.bottomRootNavigation, navController);

        User user = preferencesViewModel.getUser();
        String nameUser = user.getName();
        String lastNameUser = user.getLast_name();
        boolean notifications = preferencesViewModel.getNotifications();

        if (!notifications) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Подключить уведомления")
                    .setMessage("Вам необходимо подключить уведомления о создании новой заявки.")
                    .setPositiveButton("Подключить", (dialog, which) -> {
                        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEW_TASKS_CREATE);
                        preferencesViewModel.setNotifications(true);
                        Log.d(TAG_NOTIFICATIONS, "Notifications state: " + notifications);

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