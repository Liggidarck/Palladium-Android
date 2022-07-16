package com.george.vector.ui.users.root.folders;

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

import com.george.vector.R;
import com.george.vector.databinding.ActivityFolderRootBinding;
import com.george.vector.ui.users.root.tasks.FragmentTasksRoot;

public class FolderRootActivity extends AppCompatActivity {

    ActivityFolderRootBinding folderRootBinding;

    String textToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folderRootBinding = ActivityFolderRootBinding.inflate(getLayoutInflater());
        setContentView(folderRootBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String location = arguments.getString(LOCATION);
        String folder = arguments.getString(FOLDER);
        String executed = arguments.getString(EXECUTED);
        String email = arguments.getString(EMAIL);

        if(folder.equals(NEW_TASKS))
            textToolbar = getString(R.string.new_tasks_text);

        if(folder.equals(IN_PROGRESS_TASKS))
            textToolbar = getString(R.string.progress_tasks);

        if(folder.equals(ARCHIVE_TASKS))
            textToolbar = getString(R.string.archive_tasks_text);

        if(folder.equals(COMPLETED_TASKS))
            textToolbar = getString(R.string.completed_tasks_text);

        folderRootBinding.toolbarFolderRootActivity.setNavigationOnClickListener(v -> onBackPressed());
        folderRootBinding.toolbarFolderRootActivity.setTitle(textToolbar);

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