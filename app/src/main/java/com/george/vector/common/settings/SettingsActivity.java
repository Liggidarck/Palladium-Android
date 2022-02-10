package com.george.vector.common.settings;

import static com.george.vector.common.consts.Keys.PERMISSION;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.george.vector.R;
import com.george.vector.databinding.SettingsRootActivityBinding;

public class SettingsActivity extends AppCompatActivity {

    SettingsRootActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsRootActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String permission = arguments.getString(PERMISSION);

        binding.toolbarRootToolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (savedInstanceState == null) {

            if (permission.equals("root"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsRootFragment())
                        .commit();

            if (permission.equals("user"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsUserFragment())
                        .commit();

            if (permission.equals("executor"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsExecutorFragment())
                        .commit();

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