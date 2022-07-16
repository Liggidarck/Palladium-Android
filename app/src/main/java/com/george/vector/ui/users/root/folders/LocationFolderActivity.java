package com.george.vector.ui.users.root.folders;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTED;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.databinding.ActivityLocationFolderBinding;

public class LocationFolderActivity extends AppCompatActivity {

    ActivityLocationFolderBinding folderBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folderBinding = ActivityLocationFolderBinding.inflate(getLayoutInflater());
        setContentView(folderBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get(LOCATION).toString();
        String executed = arguments.get(EXECUTED).toString();
        String email = arguments.get(EMAIL).toString();

        if(location.equals(OST_SCHOOL))
            folderBinding.toolbarLocationFolderRoot.setTitle(getText(R.string.ost_text));

        if(location.equals(BAR_SCHOOL))
            folderBinding.toolbarLocationFolderRoot.setTitle(getText(R.string.bar_text));

        folderBinding.toolbarLocationFolderRoot.setNavigationOnClickListener(v -> onBackPressed());

        folderBinding.newTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, NEW_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        folderBinding.inProgressTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        folderBinding.completedTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, COMPLETED_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        folderBinding.archiveTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, ARCHIVE_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

    }
}