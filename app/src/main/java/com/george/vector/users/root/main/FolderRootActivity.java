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
    String text_toolbar;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_root);

        toolbar_folder_root_activity = findViewById(R.id.toolbar_folder_root_activity);

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get(getString(R.string.location)).toString();
        String folder = arguments.get(getString(R.string.folder)).toString();

        if(folder.equals(getString(R.string.new_tasks)))
            text_toolbar = getString(R.string.new_tasks_text);

        if(folder.equals(getString(R.string.in_progress_tasks)))
            text_toolbar = getString(R.string.progress_tasks);

        if(folder.equals(getString(R.string.archive_tasks)))
            text_toolbar = getString(R.string.archive_tasks_text);

        toolbar_folder_root_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_folder_root_activity.setTitle(text_toolbar);
        Log.d(TAG, "location: " + location);
        Log.d(TAG, "folder: " + folder);

        Fragment currentFragment = null;

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.new_tasks)))
            currentFragment = new fragment_school_ost_new_tasks();

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.in_progress_tasks)))
            currentFragment = new fragment_school_ost_progress_tasks();

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.archive_tasks)))
            currentFragment = new fragment_school_ost_archive_tasks();


        if(location.equals(getString(R.string.bar_school)) && folder.equals(getString(R.string.new_tasks)))
            currentFragment = new fragment_school_bar_new_tasks();

        if(location.equals(getString(R.string.bar_school)) && folder.equals(getString(R.string.in_progress_tasks)))
            currentFragment = new fragment_school_bar_progress_tasks();

        if(location.equals(getString(R.string.bar_school)) && folder.equals(getString(R.string.archive_tasks)))
            currentFragment = new fragment_school_bar_archive_tasks();

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();

    }
}