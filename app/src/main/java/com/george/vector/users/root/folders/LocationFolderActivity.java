package com.george.vector.users.root.folders;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTED;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class LocationFolderActivity extends AppCompatActivity {

    private static final String TAG = "LocationFolderAct";

    MaterialToolbar toolbar_location_folder_root;
    MaterialCardView new_tasks_card_root, in_progress_tasks_card_root, archive_tasks_card_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_folder);

        toolbar_location_folder_root = findViewById(R.id.toolbar_location_folder_root);
        new_tasks_card_root = findViewById(R.id.new_tasks_card_root);
        in_progress_tasks_card_root = findViewById(R.id.in_progress_tasks_card_root);
        archive_tasks_card_root = findViewById(R.id.archive_tasks_card_root);

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get(LOCATION).toString();
        String executed = arguments.get(EXECUTED).toString();
        String email = arguments.get(EMAIL).toString();
        Log.d(TAG , "email: " + email);

        if(location.equals(OST_SCHOOL) || location.equals(BAR_SCHOOL))
            toolbar_location_folder_root.setTitle(getText(R.string.school));

        toolbar_location_folder_root.setNavigationOnClickListener(v -> onBackPressed());

        new_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, NEW_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        in_progress_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        archive_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(LOCATION, location);
            intent.putExtra(FOLDER, ARCHIVE_TASKS);
            intent.putExtra(EXECUTED, executed);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

    }
}