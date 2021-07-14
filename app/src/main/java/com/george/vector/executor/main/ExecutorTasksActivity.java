package com.george.vector.executor.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.executor.main.fragments_location.bar_school.fragment_bar_school_new;
import com.george.vector.executor.main.fragments_location.bar_school.fragment_bar_school_progress;
import com.george.vector.executor.main.fragments_location.ost_school.fragment_ost_school_new;
import com.george.vector.executor.main.fragments_location.ost_school.fragment_ost_school_progress;
import com.google.android.material.appbar.MaterialToolbar;

public class ExecutorTasksActivity extends AppCompatActivity {

    private static final String TAG = "ExecutorTasks";
    MaterialToolbar toolbar_tasks_executor_activity;
    String location, task_folder, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor_tasks);

        toolbar_tasks_executor_activity = findViewById(R.id.toolbar_tasks_executor_activity);

        Bundle arguments = getIntent().getExtras();
        location = arguments.get("location").toString();
        task_folder = arguments.get("task_folder").toString();
        email = arguments.get("email").toString();

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

        Fragment currentFragment = null;
        if(location.equals("ost_school") && task_folder.equals("new_tasks")){
            Log.i(TAG, "Запуск фрагмента Школа новые заявки");
            currentFragment = new fragment_ost_school_new();

            Bundle email = new Bundle();
            email.putString("email", this.email);
            currentFragment.setArguments(email);
        }

        if(location.equals("ost_school") && task_folder.equals("progress_tasks")) {
            Log.i(TAG, "Запуск фрагмента Школа заявки в работе");
            currentFragment = new fragment_ost_school_progress();

            Bundle email = new Bundle();
            email.putString("email", this.email);
            currentFragment.setArguments(email);
        }


        if(location.equals("bar_school") && task_folder.equals("new_tasks")){
            Log.i(TAG, "Запуск фрагмента Школа bar новые заявки");
            currentFragment = new fragment_bar_school_new();

            Bundle email = new Bundle();
            email.putString("email", this.email);
            currentFragment.setArguments(email);
        }

        if(location.equals("bar_school") && task_folder.equals("progress_tasks")) {
            Log.i(TAG, "Запуск фрагмента Школа bar заявки в работе");
            currentFragment = new fragment_bar_school_progress();

            Bundle email = new Bundle();
            email.putString("email", this.email);
            currentFragment.setArguments(email);
        }

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_executor, currentFragment)
                .commit();
    }
}