package com.george.vector.ui.common.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityLoadingBinding;
import com.george.vector.network.model.user.Role;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.admin.main.MainAdminActivity;
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
        String email = preferencesViewModel.getUser().getEmail();
        List<Role> roleList = preferencesViewModel.getUser().getRoles();

        if (token == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (token == null & email != null) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Внимание!")
                    .setMessage("Необходимо войти в аккаунт снова. Ваши данные были изменены разработчиком. Обратитесь к разработчику любым удобным способом для получения новых данных.")
                    .setNegativeButton("Помощь", (dialog1, which) -> {
                        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", getString(R.string.email_developer), null));
                        intent.putExtra("android.intent.extra.SUBJECT", "Помощь с восстановлением доступа к приложению");
                        startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));

                        preferencesViewModel.deleteUserData();
                    })
                    .setPositiveButton("ок", (dialog12, which) ->
                            startActivity(new Intent(this, LoginActivity.class)))
                    .create();
            dialog.show();
        }

        if (token != null) {
            startApp(roleList.get(0).getName());
        }

    }

    void startApp(String role) {
        if (role.equals("ROLE_DEVELOPER") || role.equals("ROLE_ADMIN"))
            startActivity(new Intent(this, MainAdminActivity.class));

        if (role.equals("ROLE_USER"))
            startActivity(new Intent(this, MainUserActivity.class));

        if (role.equals("ROLE_EXECUTOR"))
            startActivity(new Intent(this, MainExecutorActivity.class));
        finish();
    }

}