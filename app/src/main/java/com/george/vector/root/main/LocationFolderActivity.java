package com.george.vector.root.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class LocationFolderActivity extends AppCompatActivity {

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
        String location = arguments.get("location").toString();

        toolbar_location_folder_root.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_location_folder_root.setTitle(location);

        new_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            String folder = "Новые заявки";
            intent.putExtra("data_location_folder", location);
            intent.putExtra("folder", folder);
            startActivity(intent);
        });

        in_progress_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            String folder = "В работе";
            intent.putExtra("data_location_folder", location);
            intent.putExtra("folder", folder);
            startActivity(intent);
        });

        archive_tasks_card_root.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderRootActivity.class);
            String folder = "Архив";
            intent.putExtra("data_location_folder", location);
            intent.putExtra("folder", folder);
            startActivity(intent);
        });

    }
}