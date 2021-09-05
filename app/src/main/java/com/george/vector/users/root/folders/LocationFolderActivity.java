package com.george.vector.users.root.folders;

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
        String location = arguments.get(getString(R.string.location)).toString();
        String executed = arguments.get("executed").toString();
        String email = arguments.get(getString(R.string.email)).toString();
        Log.d(TAG , "email: " + email);

        if(location.contentEquals(getText(R.string.ost_school)) || location.contentEquals(getText(R.string.bar_school)))
            toolbar_location_folder_root.setTitle(getText(R.string.school));

        toolbar_location_folder_root.setNavigationOnClickListener(v -> onBackPressed());

        new_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(getString(R.string.location), location);
            intent.putExtra(getString(R.string.folder), getString(R.string.new_tasks));
            intent.putExtra("executed", executed);
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        in_progress_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(getString(R.string.location), location);
            intent.putExtra(getString(R.string.folder), getString(R.string.in_progress_tasks));
            intent.putExtra("executed", executed);
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        archive_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            intent.putExtra(getString(R.string.location), location);
            intent.putExtra(getString(R.string.folder), getString(R.string.archive_tasks));
            intent.putExtra("executed", executed);
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

    }
}