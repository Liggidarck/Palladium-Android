package com.george.vector.local_admin.tasks.sort_by_category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.local_admin.tasks.sort_by_category.fragments.fragmentArchiveTasks;
import com.george.vector.local_admin.tasks.sort_by_category.fragments.fragmentNewTasks;
import com.george.vector.local_admin.tasks.sort_by_category.fragments.fragmentProgressTasks;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderActivity extends AppCompatActivity {

    MaterialToolbar toolbar_folder_activity;

    private static final String TAG = "Folder Activity";
    String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        toolbar_folder_activity = findViewById(R.id.toolbar_folder_activity);

        Bundle arguments = getIntent().getExtras();
        section = arguments.get("section").toString();

        toolbar_folder_activity.setNavigationOnClickListener(v -> onBackPressed());

        Fragment currentFragment = null;
        switch (section) {
            case "new tasks":
                Log.i(TAG, "Запуск фрагмента New Tasks");
                currentFragment = new fragmentNewTasks();
                toolbar_folder_activity.setTitle("Новые заявки");
                break;
            case "in progress tasks":
                Log.i(TAG, "Запуск фрагмента Progress Tasks");
                currentFragment = new fragmentProgressTasks();
                toolbar_folder_activity.setTitle("Заявки в работе");
                break;
            case "archive tasks":
                Log.i(TAG, "Запуск фрагмента Archive");
                currentFragment = new fragmentArchiveTasks();
                toolbar_folder_activity.setTitle("Архив");
                break;
        }
        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder, currentFragment)
                .commit();


    }
}