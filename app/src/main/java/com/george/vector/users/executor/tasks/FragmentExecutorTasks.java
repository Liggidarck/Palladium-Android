package com.george.vector.users.executor.tasks;

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
import com.george.vector.common.tasks.ui.TaskAdapter;
import com.george.vector.common.tasks.ui.TaskUi;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentExecutorTasks extends Fragment {

    private static final String TAG = "fragmentTasksExecutor";
    TextInputLayout text_input_search_executor;
    Chip chip_all_tasks_executor, chip_urgent_tasks_executor, chip_old_school_tasks_executor, chip_new_school_tasks_executor;
    RecyclerView recycler_view_tasks_executor;

    TaskAdapter adapter;

    String location, folder, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_executor_tasks, container, false);

        text_input_search_executor = view.findViewById(R.id.text_input_search_executor);
        chip_all_tasks_executor = view.findViewById(R.id.chip_all_tasks_executor);
        chip_urgent_tasks_executor = view.findViewById(R.id.chip_urgent_tasks_executor);
        chip_old_school_tasks_executor = view.findViewById(R.id.chip_old_school_tasks_executor);
        chip_new_school_tasks_executor = view.findViewById(R.id.chip_new_school_tasks_executor);
        recycler_view_tasks_executor = view.findViewById(R.id.recycler_view_tasks_executor);

        Bundle args = getArguments();
        assert args != null;
        location = args.getString(getString(R.string.location));
        folder = args.getString(getString(R.string.folder));
        email = args.getString(getString(R.string.email));
        Log.d(TAG, "location: " + location);
        Log.d(TAG, "folder: " + folder);
        Log.d(TAG, "email: " + email);

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.new_tasks)))
            ostSchoolNewTasks();

        if(location.equals(getString(R.string.ost_school)) && folder.equals(getString(R.string.in_progress_tasks)))
            ostSchoolProgressTasks();

        return view;
    }

    void setUpRecyclerView() {
        recycler_view_tasks_executor.setHasFixedSize(true);
        recycler_view_tasks_executor.setLayoutManager(new LinearLayoutManager(FragmentExecutorTasks.this.getContext()));
        recycler_view_tasks_executor.setAdapter(adapter);
        Log.d(TAG, "SetUp Scu");
    }

    void ostSchoolNewTasks() {
        queryOstSchoolNewTasks(email);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentExecutorTasks.this.getContext(), TaskExecutorActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_new));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });
    }

    void queryOstSchoolNewTasks(String email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_new");

        Log.d(TAG, "Query email: " + email);
        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);
    }

    void ostSchoolProgressTasks() {
        queryOstSchoolProgressTasks(email);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentExecutorTasks.this.getContext(), TaskExecutorActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_new));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);

        });
    }

    void queryOstSchoolProgressTasks(String email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_progress");

        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<TaskUi> options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                .setQuery(query, TaskUi.class)
                .build();

        adapter = new TaskAdapter(options);
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
