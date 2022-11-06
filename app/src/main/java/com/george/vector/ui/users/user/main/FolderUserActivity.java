package com.george.vector.ui.users.user.main;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityFolderUserBinding;
import com.george.vector.ui.users.user.tasks.FragmentTasksUser;

public class FolderUserActivity extends AppCompatActivity {

    String email, zone, folder, textToolbar;
    ActivityFolderUserBinding folderUserBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        folderUserBinding = ActivityFolderUserBinding.inflate(getLayoutInflater());
        setContentView(folderUserBinding.getRoot());

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        Bundle arguments = getIntent().getExtras();
        folder = arguments.getString(STATUS);

        email = userDataViewModel.getUser().getEmail();
        zone = userDataViewModel.getUser().getZone();


        if (folder.equals(NEW_TASKS))
            textToolbar = getString(R.string.new_tasks_text);

        if (folder.equals(IN_PROGRESS_TASKS))
            textToolbar = getString(R.string.progress_tasks);

        if (folder.equals(ARCHIVE_TASKS))
            textToolbar = getString(R.string.archive_tasks_text);

        if (folder.equals(COMPLETED_TASKS))
            textToolbar = getString(R.string.completed_tasks_text);

        folderUserBinding.toolbarFolderUserActivity.setNavigationOnClickListener(v -> onBackPressed());
        folderUserBinding.toolbarFolderUserActivity.setTitle(textToolbar);

        Fragment fragmentTasksUser = new FragmentTasksUser();
        Bundle dataUserTasks = new Bundle();
        dataUserTasks.putString(STATUS, folder);
        fragmentTasksUser.setArguments(dataUserTasks);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_user, fragmentTasksUser)
                .commit();

    }
}