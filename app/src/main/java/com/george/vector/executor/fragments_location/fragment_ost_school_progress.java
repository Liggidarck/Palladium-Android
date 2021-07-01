package com.george.vector.executor.fragments_location;

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
import com.george.vector.common.tasks.Task;
import com.george.vector.common.tasks.TaskAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class fragment_ost_school_progress extends Fragment {

    RecyclerView recycler_view_ost_school_progress_exec;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = db.collection("ost_school_progress");

    TaskAdapter adapter;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ost_executor_school_progress, container, false);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString("email");

        recycler_view_ost_school_progress_exec = view.findViewById(R.id.recycler_view_ost_school_progress_exec);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);

        recycler_view_ost_school_progress_exec.setHasFixedSize(true);
        recycler_view_ost_school_progress_exec.setLayoutManager(new LinearLayoutManager(fragment_ost_school_progress.this.getContext()));
        recycler_view_ost_school_progress_exec.setAdapter(adapter);

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
