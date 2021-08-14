package com.george.vector.users.executor.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class FolderExecutorActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    MaterialCardView new_tasks_card_executor, in_progress_tasks_card_executor;

    String location, email;
    private static final String TAG = "FolderExecutorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_executor);

        toolbar = findViewById(R.id.toolbar_folder_executor_activity);
        new_tasks_card_executor = findViewById(R.id.new_tasks_card_executor);
        in_progress_tasks_card_executor = findViewById(R.id.in_progress_tasks_card_executor);

        Bundle arguments = getIntent().getExtras();
        location = arguments.get((String) getText(R.string.location)).toString();
        email = arguments.get((String) getText(R.string.email)).toString();

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

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle(text_toolbar);

        new_tasks_card_executor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra((String) getText(R.string.location), location);
            intent.putExtra((String) getText(R.string.folder), getText(R.string.new_tasks));
            intent.putExtra((String) getText(R.string.email), email);
            startActivity(intent);
        });

        in_progress_tasks_card_executor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra((String) getText(R.string.location), location);
            intent.putExtra((String) getText(R.string.folder), getText(R.string.in_progress_tasks));
            intent.putExtra((String) getText(R.string.email), email);
            startActivity(intent);
        });

    }
}