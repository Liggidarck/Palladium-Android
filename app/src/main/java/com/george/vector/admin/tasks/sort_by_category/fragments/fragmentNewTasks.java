package com.george.vector.admin.tasks.sort_by_category.fragments;

import android.os.Bundle;
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
import com.george.vector.common.Task;
import com.george.vector.common.TaskAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class fragmentNewTasks extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef = db.collection("new tasks");

    private TaskAdapter adapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_tasks, container, false);

        Query query = taskRef.whereEqualTo("status", "Новая заявка");

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);

        RecyclerView recyclerview_new_tasks_admin = view.findViewById(R.id.recyclerview_new_tasks_admin);
        recyclerview_new_tasks_admin.setHasFixedSize(true);
        recyclerview_new_tasks_admin.setLayoutManager(new LinearLayoutManager(fragmentNewTasks.this.getContext()));
        recyclerview_new_tasks_admin.setAdapter(adapter);


        return view;
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
