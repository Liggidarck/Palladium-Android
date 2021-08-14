package com.george.vector.users.caretaker.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        String location = arguments.get((String) getText(R.string.location)).toString();

        toolbar_location_folder_caretaker = findViewById(R.id.toolbar_location_folder_caretaker);
        new_tasks_card_caretaker = findViewById(R.id.new_tasks_card_caretaker);
        task_progress_card_caretaker = findViewById(R.id.task_progress_card_caretaker);
        archive_tasks_card_caretaker = findViewById(R.id.archive_tasks_card_caretaker);

        toolbar_location_folder_caretaker.setNavigationOnClickListener(v -> onBackPressed());

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

        toolbar_location_folder_caretaker.setTitle(text_toolbar);

        new_tasks_card_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderCaretakerActivity.class);
            intent.putExtra((String) getText(R.string.folder), getText(R.string.new_tasks));
            intent.putExtra((String) getText(R.string.location), location);
            startActivity(intent);
        });

        task_progress_card_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderCaretakerActivity.class);
            intent.putExtra((String) getText(R.string.folder), getText(R.string.in_progress_tasks));
            intent.putExtra((String) getText(R.string.location), location);
            startActivity(intent);
        });

        archive_tasks_card_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(this, FolderCaretakerActivity.class);
            intent.putExtra((String) getText(R.string.folder), getText(R.string.archive_tasks));
            intent.putExtra((String) getText(R.string.location), location);
            startActivity(intent);
        });

    }
}