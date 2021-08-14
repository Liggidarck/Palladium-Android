package com.george.vector.users.root.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.root.main.location_fragments.bar_school.fragment_school_bar_archive_tasks;
import com.george.vector.users.root.main.location_fragments.bar_school.fragment_school_bar_new_tasks;
import com.george.vector.users.root.main.location_fragments.bar_school.fragment_school_bar_progress_tasks;
import com.george.vector.users.root.main.location_fragments.ost_school.fragment_school_ost_archive_tasks;
import com.george.vector.users.root.main.location_fragments.ost_school.fragment_school_ost_new_tasks;
import com.george.vector.users.root.main.location_fragments.ost_school.fragment_school_ost_progress_tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FolderRootActivity extends AppCompatActivity {

    private static final String TAG = "FolderRootActivity";

    MaterialToolbar toolbar_folder_root_activity;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_new");

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
        Log.d(TAG, "location: " + location);
        Log.d(TAG, "folder: " + folder);

        Fragment currentFragment = null;

        if(location.equals("ost_school") && folder.equals("Новые заявки")){
            Log.i(TAG, "Запуск фрагмента Школа новые заявки");
            currentFragment = new fragment_school_ost_new_tasks();
        }

        if(location.equals("ost_school") && folder.equals("В работе")) {
            Log.i(TAG, "Запуск фрагмента Школа заявки в работе");
            currentFragment = new fragment_school_ost_progress_tasks();
        }

        if(location.equals("ost_school") && folder.equals("Архив")) {
            Log.i(TAG, "Запуск фрагмента Школа заявки Архив");
            currentFragment = new fragment_school_ost_archive_tasks();
        }


        if(location.equals("bar_school") && folder.equals("Новые заявки")){
            Log.i(TAG, "Запуск фрагмента Школа bar новые заявки");
            currentFragment = new fragment_school_bar_new_tasks();
        }

        if(location.equals("bar_school") && folder.equals("В работе")) {
            Log.i(TAG, "Запуск фрагмента Школа bar заявки в работе");
            currentFragment = new fragment_school_bar_progress_tasks();
        }

        if(location.equals("bar_school") && folder.equals("Архив")) {
            Log.i(TAG, "Запуск фрагмента Школа bar заявки Архив");
            currentFragment = new fragment_school_bar_archive_tasks();
        }

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();

    }
}