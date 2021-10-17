package com.george.vector.common.settings;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    MaterialToolbar toolbar_root_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_root_activity);

        toolbar_root_toolbar = findViewById(R.id.toolbar_root_toolbar);

        Bundle arguments = getIntent().getExtras();
        String permission = arguments.getString(PERMISSION);
        String email = arguments.getString(EMAIL);

        toolbar_root_toolbar.setNavigationOnClickListener(v -> onBackPressed());

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