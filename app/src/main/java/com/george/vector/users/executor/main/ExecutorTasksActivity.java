package com.george.vector.users.executor.main;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.george.vector.R;
import com.george.vector.users.executor.tasks.FragmentExecutorTasks;
import com.google.android.material.appbar.MaterialToolbar;

public class ExecutorTasksActivity extends AppCompatActivity {

    private static final String TAG = "ExecutorTasks";
    MaterialToolbar toolbar_tasks_executor_activity;
    String location, folder, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor_tasks);

        toolbar_tasks_executor_activity = findViewById(R.id.toolbar_tasks_executor_activity);

        Bundle arguments = getIntent().getExtras();
        location = arguments.get(LOCATION).toString();
        folder = arguments.get(FOLDER).toString();
        email = arguments.get(EMAIL).toString();

        String text_toolbar = null;
        switch (location) {
            case "ost_school":
            case "bar_school":
                text_toolbar = "Школа";
                break;

            case "ost_aist":
                text_toolbar = "Детский сад 'Аист'";
                break;

            case "ost_yagodka":
                text_toolbar = "Детский сад 'Ягодка'";
                break;

            case "bar_rucheek":
                text_toolbar = "Детский сад 'Ручеек'";
                break;

            case "bar_star":
                text_toolbar = "Детский сад 'Звездочка'";
                break;
        }

        toolbar_tasks_executor_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_tasks_executor_activity.setTitle(text_toolbar);

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