package com.george.vector.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityLoadingBinding;
import com.george.vector.network.model.Role;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;

import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    private ActivityLoadingBinding loadingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        loadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(loadingBinding.getRoot());

        UserDataViewModel preferencesViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        String token = preferencesViewModel.getToken();
        String username = preferencesViewModel.getUser().getUsername();

        String name = preferencesViewModel.getUser().getName();
        String lastname = preferencesViewModel.getUser().getLastName();
        String patronymic = preferencesViewModel.getUser().getPatronymic();
        String email = preferencesViewModel.getUser().getEmail();
        String zone = preferencesViewModel.getUser().getZone();

        List<Role> roleList = preferencesViewModel.getUser().getRole();

        if (token.equals(null)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Внимание!")
                    .setMessage("Необходимо войти в аккаунт снова. Если вы не помните совой логин, обратитесь в техническую поддрежку")
                    .setNegativeButton("Помощь", (dialog1, which) -> {

                        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", getString(R.string.email_developer), null));
                        intent.putExtra("android.intent.extra.SUBJECT", "Помощь с восстановлением доступа к приложению");
                        startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));

                    })
                    .setPositiveButton("ок", (dialog12, which) ->
                            startActivity(new Intent(this, LoginActivity.class)))
                    .create();
            dialog.show();
        }

        if (token != null & !roleList.get(0).equals(null)) {
            startApp(roleList.get(0).getName());
        }

    }

    void startApp(String role) {
        if (role.equals("ROLE_DEVELOPER"))
            startActivity(new Intent(this, MainRootActivity.class));

        if (role.equals("Пользователь"))
            startActivity(new Intent(this, MainUserActivity.class));

        if (role.equals("Исполнитель"))
            startActivity(new Intent(this, MainExecutorActivity.class));


        finish();
    }

}