package com.george.vector.users.root.folders;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTED;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.NEW_TASKS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.root.tasks.FragmentTasksRoot;
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
        String location = arguments.getString(LOCATION);
        String folder = arguments.getString(FOLDER);
        String executed = arguments.getString(EXECUTED);
        String email = arguments.getString(EMAIL);
        Log.d(TAG, "email: " + email);

        if(folder.equals(NEW_TASKS))
            text_toolbar = getString(R.string.new_tasks_text);

        if(folder.equals(IN_PROGRESS_TASKS))
            text_toolbar = getString(R.string.progress_tasks);

        if(folder.equals(ARCHIVE_TASKS))
            text_toolbar = getString(R.string.archive_tasks_text);

        if(folder.equals(COMPLETED_TASKS))
            text_toolbar = "Завершенные";

        toolbar_folder_root_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_folder_root_activity.setTitle(text_toolbar);
        Log.d(TAG, String.format("location: %s", location));
        Log.d(TAG, String.format("folder: %s", folder));

        Fragment currentFragment = new FragmentTasksRoot();
        Bundle bundle = new Bundle();
        bundle.putString(LOCATION, location);
        bundle.putString(FOLDER, folder);
        bundle.putString(EXECUTED, executed);
        bundle.putString(EMAIL, email);
        currentFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();
    }
}