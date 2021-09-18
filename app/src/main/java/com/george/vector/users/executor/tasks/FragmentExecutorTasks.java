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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentExecutorTasks extends Fragment {

    private static final String TAG = "fragmentTasksExecutor";
    Chip chip_all_tasks_executor, chip_urgent_tasks_executor, chip_old_school_tasks_executor, chip_new_school_tasks_executor;
    RecyclerView recycler_view_tasks_executor;

    TaskAdapter adapter;

    String location, folder, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_executor_tasks, container, false);

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

        chip_all_tasks_executor.setOnCheckedChangeListener(((compoundButton, isChecked) -> {

            if(isChecked) {

                Log.d(TAG, "All Tasks");

                Query query_all = taskRef.whereEqualTo("executor", email);
                FirestoreRecyclerOptions<TaskUi> options_all = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_all, TaskUi.class)
                        .build();
                adapter.updateOptions(options_all);
            }

        }));

        chip_urgent_tasks_executor.setOnCheckedChangeListener(((compoundButton, b) -> {

            if(b) {
                Log.i(TAG, "urgent checked");
                Query query_urgent = taskRef
                        .whereEqualTo("urgent", true)
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<TaskUi> urgent_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_urgent, TaskUi.class)
                        .build();

                adapter.updateOptions(urgent_options);
            }

        }));

        chip_old_school_tasks_executor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b){
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.old_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_address, TaskUi.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));

        chip_new_school_tasks_executor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.new_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_address, TaskUi.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));

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
            intent.putExtra(getString(R.string.collection), getString(R.string.ost_school_progress));
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

        chip_all_tasks_executor.setOnCheckedChangeListener(((compoundButton, isChecked) -> {

            if(isChecked) {

                Log.d(TAG, "All Tasks");

                Query query_all = taskRef.whereEqualTo("executor", email);
                FirestoreRecyclerOptions<TaskUi> options_all = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_all, TaskUi.class)
                        .build();
                adapter.updateOptions(options_all);
            }

        }));

        chip_urgent_tasks_executor.setOnCheckedChangeListener(((compoundButton, b) -> {

            if(b) {
                Log.i(TAG, "urgent checked");
                Query query_urgent = taskRef
                        .whereEqualTo("urgent", true)
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<TaskUi> urgent_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_urgent, TaskUi.class)
                        .build();

                adapter.updateOptions(urgent_options);
            }

        }));

        chip_old_school_tasks_executor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b){
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.old_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_address, TaskUi.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));

        chip_new_school_tasks_executor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.new_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<TaskUi> address_options = new FirestoreRecyclerOptions.Builder<TaskUi>()
                        .setQuery(query_address, TaskUi.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));
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
