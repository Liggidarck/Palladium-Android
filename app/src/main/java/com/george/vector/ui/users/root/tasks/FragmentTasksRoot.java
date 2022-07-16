package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_ARCHIVE;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_COMPLETED;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_PROGRESS;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTED;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.NEW_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_ARCHIVE;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_COMPLETED;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_NEW;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_PROGRESS;
import static com.george.vector.common.consts.Logs.TAG_TASK_ROOT_FRAGMENT;

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
import com.george.vector.databinding.FragmentTasksRootBinding;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.network.model.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksRoot extends Fragment {

    String location, folder, executed, email;

    TaskAdapter taskAdapter;
    FragmentTasksRootBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksRootBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        location = args.getString(LOCATION);
        folder = args.getString(FOLDER);
        executed = args.getString(EXECUTED);
        email = args.getString(EMAIL);

        if (location.equals(OST_SCHOOL) && folder.equals(NEW_TASKS))
            ostSchoolNewTasks();

        if (location.equals(OST_SCHOOL) && folder.equals(IN_PROGRESS_TASKS))
            ostSchoolProgressTasks();

        if (location.equals(OST_SCHOOL) && folder.equals(COMPLETED_TASKS))
            ostSchoolCompletedTasks();

        if (location.equals(OST_SCHOOL) && folder.equals(ARCHIVE_TASKS))
            ostSchoolArchiveTasks();

        if (location.equals(BAR_SCHOOL) && folder.equals(NEW_TASKS))
            barSchoolNewTasks();

        if (location.equals(BAR_SCHOOL) && folder.equals(IN_PROGRESS_TASKS))
            barSchoolProgressTasks();

        if (location.equals(BAR_SCHOOL) && folder.equals(COMPLETED_TASKS))
            barSchoolCompletedTasks();

        if (location.equals(BAR_SCHOOL) && folder.equals(ARCHIVE_TASKS))
            barSchoolArchiveTasks();

        if(location.equals(BAR_SCHOOL)) {
            binding.chipNewSchoolTasksRoot.setVisibility(View.INVISIBLE);
            binding.chipOldSchoolTasksRoot.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    void setUpRecyclerView() {
        binding.recyclerviewSchoolOstNewTasks.setHasFixedSize(true);
        binding.recyclerviewSchoolOstNewTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksRoot.this.getContext()));
        binding.recyclerviewSchoolOstNewTasks.setAdapter(taskAdapter);
    }

    void queryTasks(String executed, String collectionReference, String status) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference taskRef = db.collection(collectionReference);

        Query query = taskRef.whereEqualTo("status", status);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        taskAdapter = new TaskAdapter(options);

        if (executed.equals("root")) {
            Log.d(TAG_TASK_ROOT_FRAGMENT, "All Tasks");

            binding.chipAllTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Log.d(TAG_TASK_ROOT_FRAGMENT, "Default Query");

                    Query query_all = taskRef.whereEqualTo("status", status);
                    FirestoreRecyclerOptions<Task> all_tasks = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_all, Task.class)
                            .build();

                    taskAdapter.updateOptions(all_tasks);
                }

            });

            binding.chipUrgentTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Log.i(TAG_TASK_ROOT_FRAGMENT, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true);

                    FirestoreRecyclerOptions<Task> urgent_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_urgent, Task.class)
                            .build();

                    taskAdapter.updateOptions(urgent_options);
                }

            });

            binding.chipOldSchoolTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text));

                    FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_address, Task.class)
                            .build();

                    taskAdapter.updateOptions(address_options);
                }

            });

            binding.chipNewSchoolTasksRoot.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text));

                    FirestoreRecyclerOptions<Task> address_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_address, Task.class)
                            .build();

                    taskAdapter.updateOptions(address_options);
                }
            });

        }

        if (executed.equals("work")) {
            Log.d(TAG_TASK_ROOT_FRAGMENT, "All Executed Tasks");

            Query query_all_default = taskRef.whereEqualTo("executor", email);
            FirestoreRecyclerOptions<Task> executor_options_default = new FirestoreRecyclerOptions.Builder<Task>()
                    .setQuery(query_all_default, Task.class)
                    .build();

            taskAdapter.updateOptions(executor_options_default);

            binding.chipAllTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_all = taskRef.whereEqualTo("executor", email);
                    FirestoreRecyclerOptions<Task> executor_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_all, Task.class)
                            .build();

                    taskAdapter.updateOptions(executor_options);
                }

            });

            binding.chipUrgentTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Log.i(TAG_TASK_ROOT_FRAGMENT, "urgent checked");
                    Query query_urgent = taskRef.whereEqualTo("urgent", true).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<Task> search_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_urgent, Task.class)
                            .build();

                    taskAdapter.updateOptions(search_options);
                }

            });

            binding.chipOldSchoolTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.old_school_full_text)).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<Task> search_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_address, Task.class)
                            .build();

                    taskAdapter.updateOptions(search_options);
                }

            });

            binding.chipNewSchoolTasksRoot.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    Query query_address = taskRef.whereEqualTo("address", getText(R.string.new_school_full_text)).whereEqualTo("executor", email);

                    FirestoreRecyclerOptions<Task> search_options = new FirestoreRecyclerOptions.Builder<Task>()
                            .setQuery(query_address, Task.class)
                            .build();

                    taskAdapter.updateOptions(search_options);
                }
            });

        }
    }

    void ostSchoolNewTasks() {
        queryTasks(executed, OST_SCHOOL_NEW, "Новая заявка");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_NEW);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void ostSchoolProgressTasks() {
        queryTasks(executed, OST_SCHOOL_PROGRESS, "В работе");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_PROGRESS);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void ostSchoolCompletedTasks() {
        queryTasks(executed, OST_SCHOOL_COMPLETED, "Завершенная заявка");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_COMPLETED);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void ostSchoolArchiveTasks() {
        queryTasks(executed, OST_SCHOOL_ARCHIVE, "Архив");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, OST_SCHOOL_ARCHIVE);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void barSchoolNewTasks() {
        queryTasks(executed, BAR_SCHOOL_NEW, "Новая заявка");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, BAR_SCHOOL_NEW);
            intent.putExtra(LOCATION, BAR_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });
    }

    void barSchoolProgressTasks() {
        queryTasks(executed, BAR_SCHOOL_PROGRESS, "В работе");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, BAR_SCHOOL_PROGRESS);
            intent.putExtra(LOCATION, BAR_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void barSchoolCompletedTasks() {
        queryTasks(executed, BAR_SCHOOL_COMPLETED, "Завершенная заявка");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, BAR_SCHOOL_COMPLETED);
            intent.putExtra(LOCATION, BAR_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    void barSchoolArchiveTasks() {
        queryTasks(executed, BAR_SCHOOL_ARCHIVE, "Архив");

        setUpRecyclerView();

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            Log.d(TAG_TASK_ROOT_FRAGMENT, String.format("position: %d id: %s", position, id));

            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, BAR_SCHOOL_ARCHIVE);
            intent.putExtra(LOCATION, BAR_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
