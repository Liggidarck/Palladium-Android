package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.databinding.ActivityFolderExecutorBinding;
import com.george.vector.ui.users.executor.tasks.ExecutorTasksActivity;

public class FolderExecutorActivity extends AppCompatActivity {

    private String zone;
    private ActivityFolderExecutorBinding executorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        executorBinding = ActivityFolderExecutorBinding.inflate(getLayoutInflater());
        setContentView(executorBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        zone = arguments.getString(ZONE);

        String textToolbar = null;
        switch (zone) {
            case OST_SCHOOL:
                textToolbar = getString(R.string.ost_text);
                break;

            case BAR_SCHOOL:
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

        executorBinding.toolbarFolder.setNavigationOnClickListener(v -> onBackPressed());
        executorBinding.toolbarFolder.setTitle(textToolbar);

        executorBinding.cardNewTasks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra(ZONE, zone);
            intent.putExtra(STATUS, NEW_TASKS);
            startActivity(intent);
        });

        executorBinding.cardCompletedTasks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra(ZONE, zone);
            intent.putExtra(STATUS, COMPLETED_TASKS);
            startActivity(intent);
        });

        executorBinding.cardInProgressTasks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra(ZONE, zone);
            intent.putExtra(STATUS, IN_PROGRESS_TASKS);
            startActivity(intent);
        });

    }
}