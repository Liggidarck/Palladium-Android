package com.george.vector.root.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.root.main.location_fragments.fragment_school_ost_archive_tasks;
import com.george.vector.root.main.location_fragments.fragment_school_ost_new_tasks;
import com.george.vector.root.main.location_fragments.fragment_school_ost_progress_tasks;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderRootActivity extends AppCompatActivity {

    private static final String TAG = "FolderRootActivity";

    MaterialToolbar toolbar_folder_root_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_root);

        toolbar_folder_root_activity = findViewById(R.id.toolbar_folder_root_activity);

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get("data_location_folder").toString();
        String folder = arguments.get("folder").toString();

        toolbar_folder_root_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_folder_root_activity.setTitle(folder);
        Log.i(TAG, "location: " + location);
        Log.i(TAG, "folder: " + folder);

        Fragment currentFragment = null;

        if(location.equals("Школа") && folder.equals("Новые заявки")){
            Log.i(TAG, "Запуск фрагмента Школа новые заявки");
            currentFragment = new fragment_school_ost_new_tasks();
        }

        if(location.equals("Школа") && folder.equals("В работе")) {
            Log.i(TAG, "Запуск фрагмента Школа заявки в работе");
            currentFragment = new fragment_school_ost_progress_tasks();
        }

        if(location.equals("Школа") && folder.equals("Архив")) {
            Log.i(TAG, "Запуск фрагмента Школа заявки Архив");
            currentFragment = new fragment_school_ost_archive_tasks();
        }

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();

    }
}