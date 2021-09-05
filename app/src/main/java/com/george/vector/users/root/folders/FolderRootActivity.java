package com.george.vector.users.root.folders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.root.folders.location_fragments.bar_school.fragment_school_bar_archive_tasks;
import com.george.vector.users.root.folders.location_fragments.bar_school.fragment_school_bar_new_tasks;
import com.george.vector.users.root.folders.location_fragments.bar_school.fragment_school_bar_progress_tasks;
import com.george.vector.users.root.folders.location_fragments.ost_school.executed.fragment_school_ost_executed_archive_tasks;
import com.george.vector.users.root.folders.location_fragments.ost_school.executed.fragment_school_ost_executed_new_tasks;
import com.george.vector.users.root.folders.location_fragments.ost_school.executed.fragment_school_ost_executed_progress_tasks;
import com.george.vector.users.root.folders.location_fragments.ost_school.full_access.fragment_school_ost_archive_tasks;
import com.george.vector.users.root.folders.location_fragments.ost_school.full_access.fragment_school_ost_new_tasks;
import com.george.vector.users.root.folders.location_fragments.ost_school.full_access.fragment_school_ost_progress_tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

public class FolderRootActivity extends AppCompatActivity {

    private static final String TAG = "FolderRootActivity";

    MaterialToolbar toolbar_folder_root_activity;
    String text_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_root);

        toolbar_folder_root_activity = findViewById(R.id.toolbar_folder_root_activity);

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get(getString(R.string.location)).toString();
        String folder = arguments.get(getString(R.string.folder)).toString();
        String executed = arguments.get("executed").toString();
        String email = arguments.get(getString(R.string.email)).toString();
        Log.d(TAG, "email: " + email);

        if(folder.equals(getString(R.string.new_tasks)))
            text_toolbar = getString(R.string.new_tasks_text);

        if(folder.equals(getString(R.string.in_progress_tasks)))
            text_toolbar = getString(R.string.progress_tasks);

        if(folder.equals(getString(R.string.archive_tasks)))
            text_toolbar = getString(R.string.archive_tasks_text);

        toolbar_folder_root_activity.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_folder_root_activity.setTitle(text_toolbar);
        Log.d(TAG, String.format("location: %s", location));
        Log.d(TAG, String.format("folder: %s", folder));

        Fragment currentFragment = null;

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.new_tasks)) && executed.equals("root")) {
            currentFragment = new fragment_school_ost_new_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.in_progress_tasks)) && executed.equals("root")) {
            currentFragment = new fragment_school_ost_progress_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.archive_tasks)) && executed.equals("root")) {
            currentFragment = new fragment_school_ost_archive_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }


        if(location.equals(getString(R.string.bar_school)) && folder.equals(getString(R.string.new_tasks)) && executed.equals("root")) {
            currentFragment = new fragment_school_bar_new_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        if(location.equals(getString(R.string.bar_school)) && folder.equals(getString(R.string.in_progress_tasks)) && executed.equals("root")) {
            currentFragment = new fragment_school_bar_progress_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        if(location.equals(getString(R.string.bar_school)) && folder.equals(getString(R.string.archive_tasks)) && executed.equals("root")) {
            currentFragment = new fragment_school_bar_archive_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }




        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.new_tasks)) && executed.equals("work")) {
            currentFragment = new fragment_school_ost_executed_new_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.in_progress_tasks)) && executed.equals("work")) {
            currentFragment = new fragment_school_ost_executed_progress_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.archive_tasks)) && executed.equals("work")) {
            currentFragment = new fragment_school_ost_executed_archive_tasks();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.email), email);
            currentFragment.setArguments(bundle);
        }

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_folder_root, currentFragment)
                .commit();

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.folder_root_coordinator), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}