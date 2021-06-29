package com.george.vector.сaretaker.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class FolderTaskCaretakerActivity extends AppCompatActivity {

    MaterialToolbar toolbar_location_folder_caretaker;
    MaterialCardView new_tasks_card_caretaker, task_progress_card_caretaker, archive_tasks_card_caretaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_task_caretaker);

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get("location").toString();

        toolbar_location_folder_caretaker = findViewById(R.id.toolbar_location_folder_caretaker);
        new_tasks_card_caretaker = findViewById(R.id.new_tasks_card_caretaker);
        task_progress_card_caretaker = findViewById(R.id.task_progress_card_caretaker);
        archive_tasks_card_caretaker = findViewById(R.id.archive_tasks_card_caretaker);

        toolbar_location_folder_caretaker.setNavigationOnClickListener(v -> onBackPressed());
        toolbar_location_folder_caretaker.setTitle(location);

    }
}