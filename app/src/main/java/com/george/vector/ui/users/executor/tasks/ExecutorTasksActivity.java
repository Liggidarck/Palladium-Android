package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.STATUS;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityExecutorTasksBinding;
import com.george.vector.ui.users.executor.tasks.FragmentExecutorTasks;

public class ExecutorTasksActivity extends AppCompatActivity {

    private ActivityExecutorTasksBinding tasksBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        tasksBinding = ActivityExecutorTasksBinding.inflate(getLayoutInflater());
        setContentView(tasksBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String zone = arguments.getString(ZONE);
        String status = arguments.getString(STATUS);

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

        tasksBinding.toolbarTasks.setNavigationOnClickListener(v -> onBackPressed());
        tasksBinding.toolbarTasks.setTitle(textToolbar);

        Fragment fragmentTasks = new FragmentExecutorTasks();
        Bundle bundle = new Bundle();
        bundle.putString(ZONE, zone);
        bundle.putString(STATUS, status);
        fragmentTasks.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFolderTasks, fragmentTasks)
                .commit();
    }
}