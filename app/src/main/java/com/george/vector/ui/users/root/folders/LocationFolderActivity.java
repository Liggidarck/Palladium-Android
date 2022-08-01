package com.george.vector.ui.users.root.folders;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.databinding.ActivityLocationFolderBinding;

public class LocationFolderActivity extends AppCompatActivity {

    ActivityLocationFolderBinding folderBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        folderBinding = ActivityLocationFolderBinding.inflate(getLayoutInflater());
        setContentView(folderBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String collection = arguments.get(COLLECTION).toString();
        String executed = arguments.get(EXECUTOR_EMAIL).toString();

        if(collection.equals(OST_SCHOOL))
            folderBinding.toolbarLocationFolderRoot.setTitle(getText(R.string.ost_text));

        if(collection.equals(BAR_SCHOOL))
            folderBinding.toolbarLocationFolderRoot.setTitle(getText(R.string.bar_text));

        folderBinding.toolbarLocationFolderRoot.setNavigationOnClickListener(v -> onBackPressed());

        folderBinding.newTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(FOLDER, NEW_TASKS);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EXECUTOR_EMAIL, executed);
            startActivity(intent);
        });

        folderBinding.inProgressTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EXECUTOR_EMAIL, executed);
            startActivity(intent);
        });

        folderBinding.completedTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(FOLDER, COMPLETED_TASKS);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EXECUTOR_EMAIL, executed);
            startActivity(intent);
        });

        folderBinding.archiveTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(FOLDER, ARCHIVE_TASKS);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(EXECUTOR_EMAIL, executed);
            startActivity(intent);
        });

    }
}