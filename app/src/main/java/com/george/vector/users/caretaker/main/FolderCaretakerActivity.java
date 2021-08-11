package com.george.vector.users.caretaker.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.users.caretaker.tasks.TaskCaretakerActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FolderCaretakerActivity extends AppCompatActivity {

    MaterialToolbar toolbar_caretaker_tasks;
    RecyclerView recycler_view_tasks_caretaker;

     FirebaseFirestore firebaseFirestore;
     CollectionReference taskRef;

     private TaskAdapter adapter;

     private static final String TAG = "FolderCaretaker";
     String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_caretaker);

        toolbar_caretaker_tasks = findViewById(R.id.toolbar_caretaker_tasks);
        recycler_view_tasks_caretaker = findViewById(R.id.recycler_view_tasks_caretaker);

        Bundle arguments = getIntent().getExtras();
        location = arguments.get("location").toString();
        String data_location_folder = arguments.get("data_location_folder").toString();

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

        if(location.equals("ost_school") & data_location_folder.equals("new tasks"))
            init_tasks("ost_school_new", "Новая заявка");

        if(location.equals("ost_school") & data_location_folder.equals("progress tasks"))
            init_tasks("ost_school_progress", "В работе");

        if(location.equals("ost_school") & data_location_folder.equals("archive tasks"))
            init_tasks("ost_school_archive", "Архив");



        if(location.equals("bar_school") & data_location_folder.equals("new tasks"))
            init_tasks("bar_school_new", "Новая заявка");

        if(location.equals("bar_school") & data_location_folder.equals("progress tasks"))
            init_tasks("bar_school_progress", "В работе");

        if(location.equals("bar_school") & data_location_folder.equals("archive tasks"))
            init_tasks("bar_school_archive", "Архив");

    }

    void init_tasks(String collection, String query_st) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        taskRef = firebaseFirestore.collection(collection);

        Query query = taskRef.whereEqualTo("status", query_st);

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recycler_view_tasks_caretaker.setHasFixedSize(true);
        recycler_view_tasks_caretaker.setLayoutManager(new LinearLayoutManager(FolderCaretakerActivity.this));
        recycler_view_tasks_caretaker.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            TaskUi task = documentSnapshot.toObject(TaskUi.class);
            String id = documentSnapshot.getId();
            String path = documentSnapshot.getReference().getPath();

            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(FolderCaretakerActivity.this, TaskCaretakerActivity.class);
            intent.putExtra("id_task_caretaker", id);
            intent.putExtra("collection", collection);
            intent.putExtra("location", location);
            startActivity(intent);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}