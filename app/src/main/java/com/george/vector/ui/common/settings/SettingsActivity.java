package com.george.vector.ui.common.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        String role = userDataViewModel.getUser().getRoles().get(0).getName();

        binding.toolbarSettings.setNavigationOnClickListener(v -> onBackPressed());

        if (savedInstanceState == null) {

            if (role.equals("ROLE_DEVELOPER") || role.equals("ROLE_ADMIN"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsRootFragment())
                        .commit();

            if (role.equals("ROLE_USER"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsUserFragment())
                        .commit();

            if (role.equals("Исполнитель"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsExecutorFragment())
                        .commit();

        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String tag) {
        if (tag.equals("root_dark_theme")) {
            boolean theme = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getBoolean("root_dark_theme", false);

            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            if (!theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            recreate();
        }

        if (tag.equals("user_dark_theme")) {
            boolean theme = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getBoolean("user_dark_theme", false);

            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            if (!theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            recreate();
        }

        if (tag.equals("executor_dark_theme")) {
            boolean theme = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getBoolean("user_dark_theme", false);

            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            if (!theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            recreate();
        }

    }


    public static class SettingsRootFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public static class SettingsUserFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.user_preferences, rootKey);
        }
    }

    public static class SettingsExecutorFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.executor_preferences, rootKey);
        }
    }
}