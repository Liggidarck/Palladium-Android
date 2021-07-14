package com.george.vector.root.main.location_fragments.bar_school;

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
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.root.tasks.TaskRootActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class fragment_school_bar_new_tasks extends Fragment {

    private static final String TAG = "NewTaskBarSchool";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("bar_school_new");

    private TaskAdapter adapter;

    FirebaseFirestore firebaseFirestore;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_bar_new_tasks, container, false);

        RecyclerView recyclerview_school_bar_new_tasks = view.findViewById(R.id.recyclerview_school_bar_new_tasks);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = taskRef.whereEqualTo("status", "Новая заявка");

        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_school_bar_new_tasks.setHasFixedSize(true);
        recyclerview_school_bar_new_tasks.setLayoutManager(new LinearLayoutManager(fragment_school_bar_new_tasks.this.getContext()));
        recyclerview_school_bar_new_tasks.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_school_bar_new_tasks.this.getContext(), TaskRootActivity.class);
            intent.putExtra("id_task_root", id);
            intent.putExtra("collection", "bar_school_new");
            intent.putExtra("zone", "bar_school");
            startActivity(intent);

        });

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
