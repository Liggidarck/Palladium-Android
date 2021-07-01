package com.george.vector.executor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
        location = arguments.get("location").toString();
        email = arguments.get("email").toString();

        new_tasks_card_executor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("task_folder", "new_tasks");
            intent.putExtra("email", email);
            startActivity(intent);
        });

        in_progress_tasks_card_executor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExecutorTasksActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("task_folder", "progress_tasks");
            intent.putExtra("email", email);
            startActivity(intent);
        });

    }
}