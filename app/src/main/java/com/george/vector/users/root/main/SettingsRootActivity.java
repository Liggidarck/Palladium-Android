package com.george.vector.users.root.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsRootActivity extends AppCompatActivity {

    MaterialToolbar toolbar_root_toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_root_activity);

        toolbar_root_toolbar = findViewById(R.id.toolbar_root_toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        toolbar_root_toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, RootMainActivity.class)));

        if (savedInstanceState == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_root, new SettingsFragment())
                    .commit();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, RootMainActivity.class));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}