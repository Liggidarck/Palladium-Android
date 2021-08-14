package com.george.vector.users.admin.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.admin.main.fragments.bar_school.fragment_school_bar_archive_tasks;
import com.george.vector.users.admin.main.fragments.bar_school.fragment_school_bar_new_tasks;
import com.george.vector.users.admin.main.fragments.bar_school.fragment_school_bar_progress_tasks;
import com.george.vector.users.admin.main.fragments.ost_school.fragment_school_ost_archive_tasks;
import com.george.vector.users.admin.main.fragments.ost_school.fragment_school_ost_new_tasks;
import com.george.vector.users.admin.main.fragments.ost_school.fragment_school_ost_progress_tasks;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderAdminActivity extends AppCompatActivity {

    MaterialToolbar toolbar_folder_activity;

    private static final String TAG = "FolderAdminActivity";
    String folder, permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_folder);

        toolbar_folder_activity = findViewById(R.id.toolbar_folder_activity);

        Bundle arguments = getIntent().getExtras();
        folder = arguments.get((String) getText(R.string.folder)).toString();
        permission = arguments.get((String) getText(R.string.permission)).toString();
        Log.d(TAG, String.format("Folder: %s", folder));
        Log.d(TAG, String.format("Permission: %s", permission));

        toolbar_folder_activity.setNavigationOnClickListener(v -> onBackPressed());

        Fragment currentFragment = null;

        if(permission.contentEquals(getText(R.string.ost_school)) && folder.contentEquals(getText(R.string.new_tasks)))
            currentFragment = new fragment_school_ost_new_tasks();


        if(permission.contentEquals(getText(R.string.ost_school)) && folder.contentEquals(getText(R.string.in_progress_tasks)))
            currentFragment = new fragment_school_ost_progress_tasks();


        if(permission.contentEquals(getText(R.string.ost_school)) && folder.contentEquals(getText(R.string.archive_tasks)))
            currentFragment = new fragment_school_ost_archive_tasks();


        if(permission.contentEquals(getText(R.string.bar_school)) && folder.contentEquals(getText(R.string.new_tasks)))
            currentFragment = new fragment_school_bar_new_tasks();

        if(permission.contentEquals(getText(R.string.bar_school)) && folder.contentEquals(getText(R.string.in_progress_tasks)))
            currentFragment = new fragment_school_bar_progress_tasks();

        if(permission.contentEquals(getText(R.string.bar_school)) && folder.contentEquals(getText(R.string.archive_tasks)))
            currentFragment = new fragment_school_bar_archive_tasks();

        Log.i(TAG, String.format("Запуск фрагмента %s папки %s", permission, folder));

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_admin_folder, currentFragment)
                .commit();

    }
}