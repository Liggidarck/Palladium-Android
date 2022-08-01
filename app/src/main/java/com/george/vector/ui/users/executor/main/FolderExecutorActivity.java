package com.george.vector.ui.users.executor.main;

import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.LOCATION;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.george.vector.R;
import com.george.vector.databinding.ActivityFolderExecutorBinding;

public class FolderExecutorActivity extends AppCompatActivity {

    String location, email;
    ActivityFolderExecutorBinding executorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        executorBinding = ActivityFolderExecutorBinding.inflate(getLayoutInflater());
        setContentView(executorBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        location = arguments.getString(LOCATION);
        email = arguments.getString(EMAIL);

        String textToolbar = null;
        switch (location) {
            case "ost_school":
                textToolbar = getString(R.string.ost_text);
                break;

            case "bar_school":
                textToolbar = getString(R.string.bar_text);
                break;

            case "ost_aist":
                textToolbar = getString(R.string.ost_stork_text);
                break;

            case "ost_yagodka":
                textToolbar = getString(R.string.ost_berry_text);
                break;

            case "bar_rucheek":
                textToolbar = getString(R.string.bar_stream_text);
                break;

            case "bar_star":
                textToolbar = getString(R.string.bar_star_text);
                break;
        }

        executorBinding.toolbarFolderExecutorActivity.setNavigationOnClickListener(v -> onBackPressed());
        executorBinding.toolbarFolderExecutorActivity.setTitle(textToolbar);

        executorBinding.newTasksCardExecutor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, NEW_TASKS);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        executorBinding.completedTasksCardExecutor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, COMPLETED_TASKS);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        executorBinding.inProgressTasksCardExecutor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

    }
}