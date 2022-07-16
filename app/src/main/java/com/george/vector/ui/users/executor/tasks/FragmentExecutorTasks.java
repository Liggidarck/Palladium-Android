package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_PROGRESS;

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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.ui.adapters.TaskAdapter;
import com.george.vector.network.model.Task;
import com.george.vector.databinding.FragmentExecutorTasksBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentExecutorTasks extends Fragment {

    private static final String TAG = "fragmentTasksExecutor";
    TaskAdapter adapter;

    String location, folder, email;

    FragmentExecutorTasksBinding tasksBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tasksBinding = FragmentExecutorTasksBinding.inflate(inflater, container, false);
        View view = tasksBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        location = args.getString(LOCATION);
        folder = args.getString(FOLDER);
        email = args.getString(EMAIL);
        Log.d(TAG, "location: " + location);
        Log.d(TAG, "folder: " + folder);
        Log.d(TAG, "email: " + email);

        if(location.equals(OST_SCHOOL) && folder.equals(NEW_TASKS))
            ostSchoolNewTasks();

        if(location.equals(OST_SCHOOL) && folder.equals(IN_PROGRESS_TASKS))
            ostSchoolProgressTasks();

        if(location.equals(OST_SCHOOL) && folder.equals(COMPLETED_TASKS))
            ostSchoolCompleteTasks();

        return view;
    }

    void setUpRecyclerView() {
        tasksBinding.recyclerViewTasksExecutor.setHasFixedSize(true);
        tasksBinding.recyclerViewTasksExecutor.setLayoutManager(new LinearLayoutManager(FragmentExecutorTasks.this.getContext()));
        tasksBinding.recyclerViewTasksExecutor.setAdapter(adapter);
    }

    void ostSchoolNewTasks() {
        queryOstSchoolNewTasks(email);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentExecutorTasks.this.getContext(), TaskExecutorActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(COLLECTION, OST_SCHOOL_NEW);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void queryOstSchoolNewTasks(String email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_new");

        Log.d(TAG, "Query email: " + email);
        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        adapter = new TaskAdapter(options);

        tasksBinding.chipAllTasksExecutor.setOnCheckedChangeListener(((compoundButton, isChecked) -> {

            if(isChecked) {

                Log.d(TAG, "All Tasks");

                Query query_all = taskRef.whereEqualTo("executor", email);
                FirestoreRecyclerOptions<Task> options_all = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_all, Task.class)
                        .build();
                adapter.updateOptions(options_all);
            }

        }));

        tasksBinding.chipUrgentTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {

            if(b) {
                Log.i(TAG, "urgent checked");
                Query query_urgent = taskRef
                        .whereEqualTo("urgent", true)
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> urgent_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_urgent, Task.class)
                        .build();

                adapter.updateOptions(urgent_options);
            }

        }));

        tasksBinding.chipOldSchoolTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b){
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.old_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_address, Task.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));

        tasksBinding.chipNewSchoolTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.new_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_address, Task.class)
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
            intent.putExtra(ID, id);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(COLLECTION, OST_SCHOOL_PROGRESS);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void queryOstSchoolProgressTasks(String email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection("ost_school_progress");

        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);

        tasksBinding.chipAllTasksExecutor.setOnCheckedChangeListener(((compoundButton, isChecked) -> {

            if(isChecked) {

                Log.d(TAG, "All Tasks");

                Query query_all = taskRef.whereEqualTo("executor", email);
                FirestoreRecyclerOptions<Task> options_all = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_all, Task.class)
                        .build();
                adapter.updateOptions(options_all);
            }

        }));

        tasksBinding.chipUrgentTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {

            if(b) {
                Log.i(TAG, "urgent checked");
                Query query_urgent = taskRef
                        .whereEqualTo("urgent", true)
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> urgent_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_urgent, Task.class)
                        .build();

                adapter.updateOptions(urgent_options);
            }

        }));

        tasksBinding.chipOldSchoolTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b){
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.old_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_address, Task.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));

        tasksBinding.chipNewSchoolTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.new_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_address, Task.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));
    }


    void ostSchoolCompleteTasks() {
        queryOstSchoolCompleteTasks(email);

        setUpRecyclerView();

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentExecutorTasks.this.getContext(), TaskExecutorActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(COLLECTION, COMPLETED_TASKS);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void queryOstSchoolCompleteTasks(String email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection(COMPLETED_TASKS);

        Log.d(TAG, "Query email: " + email);
        Query query = taskRef.whereEqualTo("executor", email);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        adapter = new TaskAdapter(options);

        tasksBinding.chipAllTasksExecutor.setOnCheckedChangeListener(((compoundButton, isChecked) -> {

            if(isChecked) {

                Log.d(TAG, "All Tasks");

                Query query_all = taskRef.whereEqualTo("executor", email);
                FirestoreRecyclerOptions<Task> options_all = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_all, Task.class)
                        .build();
                adapter.updateOptions(options_all);
            }

        }));

        tasksBinding.chipUrgentTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {

            if(b) {
                Log.i(TAG, "urgent checked");
                Query query_urgent = taskRef
                        .whereEqualTo("urgent", true)
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> urgent_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_urgent, Task.class)
                        .build();

                adapter.updateOptions(urgent_options);
            }

        }));

        tasksBinding.chipOldSchoolTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b){
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.old_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_address, Task.class)
                        .build();

                adapter.updateOptions(address_options);
            }
        }));

        tasksBinding.chipNewSchoolTasksExecutor.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                Query query_address = taskRef
                        .whereEqualTo("address", getText(R.string.new_school_full_text))
                        .whereEqualTo("executor", email);

                FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(query_address, Task.class)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tasksBinding = null;
    }
}
