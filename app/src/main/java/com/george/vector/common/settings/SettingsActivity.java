package com.george.vector.common.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.george.vector.R;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootFutureMainActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    MaterialToolbar toolbar_root_toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_root_activity);

        toolbar_root_toolbar = findViewById(R.id.toolbar_root_toolbar);

        Bundle arguments = getIntent().getExtras();
        String permission = arguments.get(getString(R.string.permission)).toString();
        String email = arguments.get(getString(R.string.email)).toString();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (permission.equals("root")) {
            intent = new Intent(this, RootFutureMainActivity.class);
            intent.putExtra(getString(R.string.email), email);
        }

        if (permission.equals("executor")) {
            intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra(getString(R.string.email), email);
        }

        toolbar_root_toolbar.setNavigationOnClickListener(v -> startActivity(intent));

        if (savedInstanceState == null) {

            if (permission.equals("root"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsRootFragment())
                        .commit();

            if (permission.equals("executor"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsExecutorFragment())
                        .commit();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(intent);
    }

    public static class SettingsRootFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


    public static class SettingsExecutorFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.executor_preferences, rootKey);
        }
    }
}