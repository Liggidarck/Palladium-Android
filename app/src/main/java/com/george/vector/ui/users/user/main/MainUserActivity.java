package com.george.vector.ui.users.user.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityMainUserBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.auth.LoginActivity;
import com.george.vector.ui.users.user.main.fragments.home.BottomSheetProfileUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainUserActivity extends AppCompatActivity implements BottomSheetProfileUser.stateBtnListener {

    ActivityMainUserBinding binding;
    FirebaseAuth firebaseAuth;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean theme = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("user_dark_theme", false);

        if(theme) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        }

        if(!theme) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        NavController navController = Navigation.findNavController(this, R.id.navHostFragmentActivityUserMain);
        NavigationUI.setupWithNavController(binding.bottomUserNavigation, navController);
    }

    @Override
    public void getButton(String button) {
        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        if (button.equals("logoutBtnUser")) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("ok", (dialog1, which) -> {
                        firebaseAuth.signOut();
                        userPrefViewModel.saveUser(new User("", "", "",
                                "", "", "", ""));
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();
        }
    }
}