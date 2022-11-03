package com.george.vector.ui.users.root.folders;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityFolderRootBinding;
import com.george.vector.ui.users.root.tasks.AddTaskRootActivity;
import com.george.vector.ui.users.root.tasks.FragmentTasksRoot;

public class FolderRootActivity extends AppCompatActivity {

    private ActivityFolderRootBinding binding;

    private String textToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityFolderRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String collection = arguments.getString(ZONE);
        String folder = arguments.getString(FOLDER);
        String executed = arguments.getString(EXECUTOR_EMAIL);

        if (folder.equals(NEW_TASKS))
            textToolbar = getString(R.string.new_tasks_text);

        if (folder.equals(IN_PROGRESS_TASKS))
            textToolbar = getString(R.string.progress_tasks);

        if (folder.equals(ARCHIVE_TASKS))
            textToolbar = getString(R.string.archive_tasks_text);

        if (folder.equals(COMPLETED_TASKS))
            textToolbar = getString(R.string.completed_tasks_text);

        binding.toolbarFolderRootActivity.setNavigationOnClickListener(v -> onBackPressed());

        binding.createTaskRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskRootActivity.class);
            intent.putExtra(ZONE, collection);
            startActivity(intent);
        });

        binding.toolbarFolderRootActivity.setTitle(textToolbar);

        Fragment currentFragment = new FragmentTasksRoot();
        Bundle bundle = new Bundle();
        bundle.putString(ZONE, collection);
        bundle.putString(FOLDER, folder);
        bundle.putString(EXECUTOR_EMAIL, executed);
        currentFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();
    }
}