package com.george.vector.users.caretaker.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.users.admin.main.fragments.ost_school.fragment_school_ost_archive_tasks;
import com.george.vector.users.caretaker.main.fragments.bar_school.fragment_school_bar_archive_tasks;
import com.george.vector.users.caretaker.main.fragments.bar_school.fragment_school_bar_new_tasks;
import com.george.vector.users.caretaker.main.fragments.bar_school.fragment_school_bar_progress_tasks;
import com.george.vector.users.caretaker.main.fragments.ost_school.fragment_school_ost_new_tasks;
import com.george.vector.users.caretaker.main.fragments.ost_school.fragment_school_ost_progress_tasks;
import com.google.android.material.appbar.MaterialToolbar;

public class FolderCaretakerActivity extends AppCompatActivity {

    MaterialToolbar toolbar_caretaker_tasks;

     private static final String TAG = "FolderCaretaker";
     String location, folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_caretaker);

        toolbar_caretaker_tasks = findViewById(R.id.toolbar_caretaker_tasks);

        Bundle arguments = getIntent().getExtras();
        location = arguments.get((String) getText(R.string.location)).toString();
        folder = arguments.get((String) getText(R.string.folder)).toString();

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

        toolbar_caretaker_tasks.setTitle(text_toolbar);
        toolbar_caretaker_tasks.setNavigationOnClickListener(v -> onBackPressed());

        Fragment currentFragment = null;
        if(location.contentEquals(getText(R.string.ost_school)) && folder.contentEquals(getText(R.string.new_tasks)))
            currentFragment = new fragment_school_ost_new_tasks();

        if(location.contentEquals(getText(R.string.ost_school)) && folder.contentEquals(getText(R.string.in_progress_tasks)))
            currentFragment = new fragment_school_ost_progress_tasks();

        if(location.contentEquals(getText(R.string.ost_school)) && folder.contentEquals(getText(R.string.archive_tasks)))
            currentFragment = new fragment_school_ost_archive_tasks();

        if(location.contentEquals(getText(R.string.bar_school)) && folder.contentEquals(getText(R.string.new_tasks)))
            currentFragment = new fragment_school_bar_new_tasks();

        if(location.contentEquals(getText(R.string.bar_school)) && folder.contentEquals(getText(R.string.in_progress_tasks)))
            currentFragment = new fragment_school_bar_progress_tasks();

        if(location.contentEquals(getText(R.string.bar_school)) && folder.contentEquals(getText(R.string.archive_tasks)))
            currentFragment = new fragment_school_bar_archive_tasks();

        Log.i(TAG, String.format("Запуск фрагмента %s папки %s", location, folder));

        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_caretaker_folder, currentFragment)
                .commit();

    }


}