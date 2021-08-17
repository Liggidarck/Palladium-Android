package com.george.vector.common.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.george.vector.R;
import com.george.vector.users.admin.main.MainAdminActivity;
import com.george.vector.users.caretaker.main.MainCaretakerActivity;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    MaterialToolbar toolbar_root_toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Intent intent = null;
    String _permission;

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

        if (permission.equals("root"))
            intent = new Intent(this, RootMainActivity.class);

        if (permission.equals("executor")) {
            intent = new Intent(this, MainExecutorActivity.class);
            intent.putExtra(getString(R.string.email), email);
        }

        if (permission.equals("admin") || permission.equals("caretaker")) {
            if (permission.equals("admin"))
                intent = new Intent(this, MainAdminActivity.class);

            if (permission.equals("caretaker"))
                intent = new Intent(this, MainCaretakerActivity.class);

            getPermission();
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

            if (permission.equals("admin") || permission.equals("caretaker"))
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, new SettingsAdminExecutorFragment())
                        .commit();

        }

    }

    void getPermission() {
        String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection(getString(R.string.users)).document(userID);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            _permission = value.getString(getString(R.string.permission));
            Log.d(TAG, String.format("Permission: %s", _permission));

            intent.putExtra(getString(R.string.permission), _permission);
        });
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

    public static class SettingsAdminExecutorFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.admin_caretaker_preferences, rootKey);
        }
    }
}