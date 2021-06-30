package com.george.vector.admin.tasks.sort_by_category.fragments.ost_school;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.admin.tasks.TaskActivity;
import com.george.vector.common.tasks.Task;
import com.george.vector.common.tasks.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class fragmentNewTasks extends Fragment {

    private static final String TAG = "fragmentNewTasks";
    FirebaseFirestore db;
    CollectionReference taskRef;

    String collection, permission;

    private TaskAdapter adapter;
    RecyclerView recyclerview_new_tasks_admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_tasks, container, false);

        recyclerview_new_tasks_admin = view.findViewById(R.id.recyclerview_new_tasks_admin);

        Bundle args = getArguments();
        assert args != null;
        permission = args.getString("permission");

        if(permission.equals("ost_school")) {
            collection = "ost_school_new";
            init_rec(collection);
        }

        return view;
    }

    void init_rec(String collection) {
        db = FirebaseFirestore.getInstance();
        taskRef = db.collection(collection);

        Query query = taskRef.whereEqualTo("status", "Новая заявка");

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);

        recyclerview_new_tasks_admin.setHasFixedSize(true);
        recyclerview_new_tasks_admin.setLayoutManager(new LinearLayoutManager(fragmentNewTasks.this.getContext()));
        recyclerview_new_tasks_admin.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Task task = documentSnapshot.toObject(Task.class);
            String id = documentSnapshot.getId();
            String path = documentSnapshot.getReference().getPath();

            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragmentNewTasks.this.getContext(), TaskActivity.class);
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
