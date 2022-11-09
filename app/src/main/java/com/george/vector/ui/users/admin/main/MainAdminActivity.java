package com.george.vector.ui.users.admin.main;

import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.george.vector.BuildConfig;
import com.george.vector.R;
import com.george.vector.databinding.ActivityAdminMainBinding;
import com.george.vector.network.notifications.SendNotification;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.network.model.user.User;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainAdminActivity extends AppCompatActivity {

    private ActivityAdminMainBinding binding;

    public static final String TAG = MainAdminActivity.class.getSimpleName();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean theme = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("root_dark_theme", false);

        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if (!theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserDataViewModel preferencesViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentActivityRootMain);
        NavigationUI.setupWithNavController(binding.bottomRootNavigation, navController);

        User user = preferencesViewModel.getUser();
        String nameUser = user.getName();
        String lastNameUser = user.getLastName();
        boolean notifications = preferencesViewModel.getNotifications();

        Log.d(TAG, "onCreate: token: " + preferencesViewModel.getToken());

        if(BuildConfig.DEBUG) {
            return;
        }
        if (!notifications) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Подключить уведомления")
                    .setMessage("Вам необходимо подключить уведомления о создании новой заявки.")
                    .setPositiveButton("Подключить", (dialog, which) -> {
                        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEW_TASKS_CREATE);
                        preferencesViewModel.setNotifications(true);
                        Log.d(TAG, "Notifications state: " + notifications);

                        SendNotification sendNotification = new SendNotification();
                        sendNotification.sendNotification("Новый пользователь зарегестрирован на получение уведомлений",
                                nameUser + " " + lastNameUser, "", "", TOPIC_NEW_TASKS_CREATE);

                    })
                    .setCancelable(false)
                    .create();
            alertDialog.show();
        }


    }

}