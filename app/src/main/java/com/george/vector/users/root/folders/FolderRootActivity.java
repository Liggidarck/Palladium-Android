package com.george.vector.users.root.folders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderRootActivity extends AppCompatActivity {

    private static final String TAG = "FolderRootActivity";

    MaterialToolbar toolbar_folder_root_activity;
    String text_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_root);

        toolbar_folder_root_activity = findViewById(R.id.toolbar_folder_root_activity);

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get(getString(R.string.location)).toString();
        String folder = arguments.get(getString(R.string.folder)).toString();
        String executed = arguments.get("executed").toString();
        String email = arguments.get(getString(R.string.email)).toString();
        Log.d(TAG, "email: " + email);

        if(folder.equals(getString(R.string.new_tasks)))
            text_toolbar = getString(R.string.new_tasks_text);

        if(folder.equals(getString(R.string.in_progress_tasks)))
            text_toolbar = getString(R.string.progress_tasks);

        if(folder.equals(getString(R.string.archive_tasks)))
            text_toolbar = getString(R.string.archive_tasks_text);

        toolbar_folder_root_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_folder_root_activity.setTitle(text_toolbar);
        Log.d(TAG, String.format("location: %s", location));
        Log.d(TAG, String.format("folder: %s", folder));

        Fragment currentFragment = new FragmentTasksRoot();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.location), location);
        bundle.putString(getString(R.string.folder), folder);
        bundle.putString("executed", executed);
        bundle.putString(getString(R.string.email), email);
        currentFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();
    }
}