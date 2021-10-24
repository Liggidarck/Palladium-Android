package com.george.vector.users.user.main;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.PERMISSION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.user.tasks.FragmentTasksUser;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderUserActivity extends AppCompatActivity {

    private static final String TAG = "FolderUserActivity";
    MaterialToolbar toolbar_folder_user_activity;

    String email, permission, collection, folder, text_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_user);

        toolbar_folder_user_activity = findViewById(R.id.toolbar_folder_user_activity);

        Bundle arguments = getIntent().getExtras();
        email = arguments.getString(EMAIL);
        permission = arguments.getString(PERMISSION);
        collection = arguments.getString(COLLECTION);
        folder = arguments.getString(FOLDER);

        Log.d(TAG, "email: " + email);
        Log.d(TAG, "permission: " + permission);
        Log.d(TAG, "collection: " + collection);
        Log.d(TAG, "folder: " + folder);

        if(folder.equals(NEW_TASKS))
            text_toolbar = getString(R.string.new_tasks_text);

        if(folder.equals(IN_PROGRESS_TASKS))
            text_toolbar = getString(R.string.progress_tasks);

        if(folder.equals(ARCHIVE_TASKS))
            text_toolbar = getString(R.string.archive_tasks_text);

        if(folder.equals(COMPLETED_TASKS))
            text_toolbar = "Завершенные";

        toolbar_folder_user_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_folder_user_activity.setTitle(text_toolbar);

        Fragment fragment_user_tasks = new FragmentTasksUser();
        Bundle data_user_tasks = new Bundle();
        data_user_tasks.putString(EMAIL, email);
        data_user_tasks.putString(PERMISSION, permission);
        data_user_tasks.putString(COLLECTION, collection);
        data_user_tasks.putString(FOLDER, folder);
        fragment_user_tasks.setArguments(data_user_tasks);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_user, fragment_user_tasks)
                .commit();

    }
}