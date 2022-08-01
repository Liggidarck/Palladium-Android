package com.george.vector.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityLoadingBinding;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.george.vector.ui.users.user.main.MainUserActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoadingActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ActivityLoadingBinding loadingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        loadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(loadingBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        UserDataViewModel preferencesViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        String name = preferencesViewModel.getUser().getName();
        String lastname = preferencesViewModel.getUser().getLast_name();
        String patronymic = preferencesViewModel.getUser().getPatronymic();
        String email = preferencesViewModel.getUser().getEmail();
        String permission = preferencesViewModel.getUser().getPermission();
        String role = preferencesViewModel.getUser().getRole();

        if(firebaseAuth.getCurrentUser() != null &
                (name.equals("") || lastname.equals("") || patronymic.equals("") || email.equals("")
                        || permission.equals("") || role.equals(""))) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Внимание!")
                    .setMessage("Необходимо войти в аккаунт снова. Если вы не помните совой логин, обратитесь в техническую поддрежку")
                    .setNegativeButton("Помощь", (dialog1, which) -> {

                        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "georgyfilatov@yandex.ru", null));
                        intent.putExtra("android.intent.extra.SUBJECT", "Помощь с восстановлением доступа к приложению");
                        startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));

                    })
                    .setPositiveButton("ок", (dialog12, which) -> {
                        firebaseAuth.signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                    })
                    .create();
            dialog.show();
        }

        if (firebaseAuth.getCurrentUser() != null & !role.equals("")) {
            startApp(role);
        }

        if(firebaseAuth.getCurrentUser() == null & role.equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

    void startApp(String role) {
        if (role.equals("Root"))
            startActivity(new Intent(this, MainRootActivity.class));

        if (role.equals("Пользователь"))
            startActivity(new Intent(this, MainUserActivity.class));

        if (role.equals("Исполнитель"))
            startActivity(new Intent(this, MainExecutorActivity.class));


        finish();
    }

}