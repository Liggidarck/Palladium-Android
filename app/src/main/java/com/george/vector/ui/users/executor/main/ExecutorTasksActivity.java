package com.george.vector.ui.users.executor.main;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.ActivityExecutorTasksBinding;
import com.george.vector.ui.users.executor.tasks.FragmentExecutorTasks;

public class ExecutorTasksActivity extends AppCompatActivity {

    String location, folder, email;
    ActivityExecutorTasksBinding tasksBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        tasksBinding = ActivityExecutorTasksBinding.inflate(getLayoutInflater());
        setContentView(tasksBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        location = arguments.getString(LOCATION);
        folder = arguments.getString(FOLDER);
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

        tasksBinding.toolbarTasksExecutorActivity.setNavigationOnClickListener(v -> onBackPressed());
        tasksBinding.toolbarTasksExecutorActivity.setTitle(textToolbar);

        Fragment currentFragment = new FragmentExecutorTasks();

        Bundle bundle = new Bundle();
        bundle.putString(LOCATION, location);
        bundle.putString(FOLDER, folder);
        bundle.putString(EMAIL, this.email);
        currentFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_executor, currentFragment)
                .commit();
    }
}