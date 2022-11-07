package com.george.vector.ui.users.root.profile;

import static com.george.vector.common.utils.consts.Keys.TOPIC_DEVELOP;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.BuildConfig;
import com.george.vector.R;
import com.george.vector.network.notifications.SendNotification;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityProfileRootBinding;
import com.george.vector.network.model.user.Role;
import com.george.vector.network.model.user.User;
import com.george.vector.ui.common.auth.LoginActivity;
import com.george.vector.ui.common.settings.SettingsActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class ProfileRootActivity extends AppCompatActivity {

    private ActivityProfileRootBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityProfileRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        User user = userPrefViewModel.getUser();

        String name = user.getName();
        String lastname = user.getLastName();
        String email = user.getEmail();
        List<Role> roleList = user.getRoles();
        String role = roleList.get(0).getName();

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
                        userPrefViewModel.deleteUserData();
                        startActivity(new Intent(this, LoginActivity.class));
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();
        });

        binding.layoutRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
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