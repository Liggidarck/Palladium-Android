package com.george.vector.users.executor.main.fragments_location.ost_school;

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
import com.george.vector.users.executor.tasks.TaskExecutorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class fragment_ost_school_new extends Fragment {

    RecyclerView recycler_view_new_tasks_executor;
    private static final String TAG = "NewTaskOstSchoolExec";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_new");

    TaskAdapter adapter;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_executor_ost_school_new, container, false);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString("email");

        recycler_view_new_tasks_executor = view.findViewById(R.id.recycler_view_new_tasks_executor);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);

        recycler_view_new_tasks_executor.setHasFixedSize(true);
        recycler_view_new_tasks_executor.setLayoutManager(new LinearLayoutManager(fragment_ost_school_new.this.getContext()));
        recycler_view_new_tasks_executor.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Log.i(TAG, "Position: " + position + " ID: " + id);

            Intent intent = new Intent(fragment_ost_school_new.this.getContext(), TaskExecutorActivity.class);
            intent.putExtra("id_task", id);
            intent.putExtra("location", "ost_school");
            intent.putExtra("collection", "ost_school_new");
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
