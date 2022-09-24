package com.george.vector.ui.users.root.profile;

import static com.george.vector.common.utils.consts.Keys.TOPIC_DEVELOP;
import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.utils.consts.Logs.TAG_NOTIFICATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.BuildConfig;
import com.george.vector.R;
import com.george.vector.common.notifications.SendNotification;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityProfileRootBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.auth.LoginActivity;
import com.george.vector.ui.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileRootActivity extends AppCompatActivity {

    String name, lastname, patronymic, email, role, permission;
    ActivityProfileRootBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityProfileRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        User user = userPrefViewModel.getUser();
        name = user.getName();
        lastname = user.getLast_name();
        patronymic = user.getPatronymic();
        email = user.getEmail();
        role = user.getRole();
        permission = user.getPermission();

        String charName = Character.toString(name.charAt(0));
        String charLastname = Character.toString(lastname.charAt(0));

        binding.nameRootProfile.setText(String.format("%s %s", name, lastname));
        binding.avaRootProfile.setText(String.format("%s%s", charName, charLastname));
        binding.externalDataUser.setText(String.format("%s %s", email, role));

        binding.toolbarProfileRoot.setNavigationOnClickListener(v -> onBackPressed());

        binding.layoutEditPersonProfile.setOnClickListener(v ->
                startActivity(new Intent(ProfileRootActivity.this, ListUsersActivity.class))
        );

        binding.settingsProfileBtn.setOnClickListener(v ->
            startActivity(new Intent(ProfileRootActivity.this, SettingsActivity.class))
        );

        binding.logoutBtn.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("ok", (dialog1, which) -> {
                        firebaseAuth.signOut();
                        userPrefViewModel.saveUser(new User("", "", "", "", "", "", ""));
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NEW_TASKS_CREATE);
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();


        });

        String versionName = "Версия: " + BuildConfig.VERSION_NAME;
        binding.versionAppTextView.setText(versionName);

        binding.developActivity.setOnClickListener(v -> {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_DEVELOP);

            SendNotification sendNotification = new SendNotification();
            sendNotification.sendNotification("NOTIFICATION FOR ADMINS",
                    "THIS IS DEVELOP CHANNEL", "", "", TOPIC_DEVELOP);
        });
    }
}