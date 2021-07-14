package com.george.vector.admin.tasks.sort_by_category.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.admin.tasks.TaskAdminActivity;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class fragmentProgressTasks extends Fragment {

    private static final String TAG = "fragmentProgressTasks";
    FirebaseFirestore db;
    CollectionReference taskRef;

    private TaskAdapter adapter;
    RecyclerView recyclerview_in_progress_admin;
    String collection, permission;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_tasks, container, false);

        recyclerview_in_progress_admin = view.findViewById(R.id.recyclerview_in_progress_admin);

        Bundle args = getArguments();
        assert args != null;
        permission = args.getString("permission");

        if(permission.equals("ost_school")) {
            collection = "ost_school_progress";
            init(collection);
        }

        if(permission.equals("bar_school")) {
            collection = "bar_school_progress";
            init(collection);
        }

        return view;
    }

    void init(String collection) {
        db = FirebaseFirestore.getInstance();
        taskRef = db.collection(collection);

        Query query = taskRef.whereEqualTo("status", "В работе");

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_in_progress_admin.setHasFixedSize(true);
        recyclerview_in_progress_admin.setLayoutManager(new LinearLayoutManager(fragmentProgressTasks.this.getContext()));
        recyclerview_in_progress_admin.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragmentProgressTasks.this.getContext(), TaskAdminActivity.class);
            intent.putExtra("id_task", id);
            intent.putExtra("location", permission);
            intent.putExtra("collection", collection);
            startActivity(intent);

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}