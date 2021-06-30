package com.george.vector.admin.tasks.sort_by_category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.admin.tasks.sort_by_category.fragments.ost_school.fragmentArchiveTasks;
import com.george.vector.admin.tasks.sort_by_category.fragments.ost_school.fragmentNewTasks;
import com.george.vector.admin.tasks.sort_by_category.fragments.ost_school.fragmentProgressTasks;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderActivity extends AppCompatActivity {

    MaterialToolbar toolbar_folder_activity;

    private static final String TAG = "Folder Activity";
    String section, permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        toolbar_folder_activity = findViewById(R.id.toolbar_folder_activity);

        Bundle arguments = getIntent().getExtras();
        section = arguments.get("section").toString();
        permission = arguments.get("permission").toString();

        toolbar_folder_activity.setNavigationOnClickListener(v -> onBackPressed());

        Fragment currentFragment = null;
        switch (section) {
            case "new tasks":
                Log.i(TAG, "Запуск фрагмента New Tasks");
                currentFragment = new fragmentNewTasks();

                Bundle args = new Bundle();
                args.putString("permission", permission);
                currentFragment.setArguments(args);

                toolbar_folder_activity.setTitle("Новые заявки");
                break;
            case "in progress tasks":
                Log.i(TAG, "Запуск фрагмента Progress Tasks");
                currentFragment = new fragmentProgressTasks();

                Bundle bundle = new Bundle();
                bundle.putString("permission", permission);
                currentFragment.setArguments(bundle);

                toolbar_folder_activity.setTitle("Заявки в работе");
                break;
            case "archive tasks":
                Log.i(TAG, "Запуск фрагмента Archive");
                currentFragment = new fragmentArchiveTasks();

                Bundle data = new Bundle();
                data.putString("permission", permission);
                currentFragment.setArguments(data);

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