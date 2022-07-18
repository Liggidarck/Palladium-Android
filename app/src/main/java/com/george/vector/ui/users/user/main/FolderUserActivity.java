package com.george.vector.ui.users.user.main;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Logs.TAG_FOLDER_USER_ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.databinding.ActivityFolderUserBinding;
import com.george.vector.ui.users.user.tasks.FragmentTasksUser;

public class FolderUserActivity extends AppCompatActivity {

    String email, permission, folder, textToolbar;
    ActivityFolderUserBinding folderUserBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folderUserBinding = ActivityFolderUserBinding.inflate(getLayoutInflater());
        setContentView(folderUserBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        email = arguments.getString(EMAIL);
        permission = arguments.getString(PERMISSION);
        folder = arguments.getString(FOLDER);

        Log.d(TAG_FOLDER_USER_ACTIVITY, "email: " + email);
        Log.d(TAG_FOLDER_USER_ACTIVITY, "permission: " + permission);
        Log.d(TAG_FOLDER_USER_ACTIVITY, "folder: " + folder);

        if(folder.equals(NEW_TASKS))
            textToolbar = getString(R.string.new_tasks_text);

        if(folder.equals(IN_PROGRESS_TASKS))
            textToolbar = getString(R.string.progress_tasks);

        if(folder.equals(ARCHIVE_TASKS))
            textToolbar = getString(R.string.archive_tasks_text);

        if(folder.equals(COMPLETED_TASKS))
            textToolbar = getString(R.string.completed_tasks_text);

        folderUserBinding.toolbarFolderUserActivity.setNavigationOnClickListener(v -> onBackPressed());
        folderUserBinding.toolbarFolderUserActivity.setTitle(textToolbar);

        Fragment fragment_user_tasks = new FragmentTasksUser();
        Bundle data_user_tasks = new Bundle();
        data_user_tasks.putString(EMAIL, email);
        data_user_tasks.putString(PERMISSION, permission);
        data_user_tasks.putString(FOLDER, folder);
        fragment_user_tasks.setArguments(data_user_tasks);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_user, fragment_user_tasks)
                .commit();

    }
}