package com.george.vector.ui.users.executor.main;

import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.FOLDER;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityExecutorTasksBinding;
import com.george.vector.ui.users.executor.tasks.FragmentExecutorTasks;

public class ExecutorTasksActivity extends AppCompatActivity {

    String collection, folder, email;
    ActivityExecutorTasksBinding tasksBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        tasksBinding = ActivityExecutorTasksBinding.inflate(getLayoutInflater());
        setContentView(tasksBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        collection = arguments.getString(COLLECTION);
        folder = arguments.getString(FOLDER);

        String textToolbar = null;
        switch (collection) {
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

        tasksBinding.toolbarTasks.setNavigationOnClickListener(v -> onBackPressed());
        tasksBinding.toolbarTasks.setTitle(textToolbar);

        Fragment fragmentTasks = new FragmentExecutorTasks();

        Bundle bundle = new Bundle();
        bundle.putString(COLLECTION, collection);
        bundle.putString(FOLDER, folder);
        fragmentTasks.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFolderTasks, fragmentTasks)
                .commit();
    }
}