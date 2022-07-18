package com.george.vector.ui.users.user.main;

import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_COLLECTION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_ROLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.george.vector.R;
import com.george.vector.databinding.ActivityMainUserBinding;
import com.george.vector.ui.auth.LoginActivity;
import com.george.vector.ui.users.user.main.fragments.home.BottomSheetProfileUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainUserActivity extends AppCompatActivity implements BottomSheetProfileUser.stateBtnListener {

    FirebaseAuth firebaseAuth;

    ActivityMainUserBinding binding;

    SharedPreferences sharedPreferences;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentActivityUserMain);
        NavigationUI.setupWithNavController(binding.bottomUserNavigation, navController);
    }

    @Override
    public void getButton(String button) {
        sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (button.equals("logoutBtnUser")) {
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
                        editor.apply();

                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();
        }
    }
}