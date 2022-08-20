package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Logs.TAG_TASK_ROOT_FRAGMENT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentTasksRootBinding;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.network.model.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksRoot extends Fragment {

    String collections, status, executorEmail, email;

    TaskAdapter taskAdapter;
    FragmentTasksRootBinding binding;

    public static final String TAG = FragmentTasksRoot.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksRootBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        collections = args.getString(COLLECTION);
        status = args.getString(FOLDER);
        executorEmail = args.getString(EXECUTOR_EMAIL);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        email = userPrefViewModel.getUser().getEmail();

        if (!collections.equals(OST_SCHOOL)) {
            binding.chipNewSchoolTasksRoot.setVisibility(View.INVISIBLE);
            binding.chipOldSchoolTasksRoot.setVisibility(View.INVISIBLE);
        }

        setUpRecyclerView(collections, status, executorEmail);

        return view;
    }

    void setUpRecyclerView(String collections, String status, String executed) {
        initQuery(collections, status, executed);

        binding.recyclerviewSchoolOstNewTasks.setHasFixedSize(true);
        binding.recyclerviewSchoolOstNewTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksRoot.this.getContext()));
        binding.recyclerviewSchoolOstNewTasks.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();
            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collections);
            startActivity(intent);
        });
    }

    private void initQuery(String location, String status, String executed) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference reference = db.collection(location);
        Query query = null;

        if (executed.equals("root")) {
            query = reference
                    .whereEqualTo("status", status);

            chipRootBehavior(reference, status);
        }

        if (executed.equals("work")) {
            query = reference
                    .whereEqualTo("status", status)
                    .whereEqualTo("executor", email);

            chipWorkBehavior(reference, status);
        }

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        taskAdapter = new TaskAdapter(options);
    }

    void chipRootBehavior(CollectionReference reference, String status) {
        binding.chipAllTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Query queryAll = reference.whereEqualTo("status", status);
                FirestoreRecyclerOptions<Task> allTasks = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryAll, Task.class)
                        .build();

                taskAdapter.updateOptions(allTasks);
            }

        });

        binding.chipUrgentTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Query queryUrgent = reference.whereEqualTo("urgent", true);
                FirestoreRecyclerOptions<Task> urgentOptions = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryUrgent, Task.class)
                        .build();

                taskAdapter.updateOptions(urgentOptions);
            }

        });

        binding.chipOldSchoolTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Query queryAddress = reference.whereEqualTo("address", getText(R.string.old_school_full_text));
                FirestoreRecyclerOptions<Task> addressOptions = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryAddress, Task.class)
                        .build();

                taskAdapter.updateOptions(addressOptions);
            }

        });

        binding.chipNewSchoolTasksRoot.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Query queryAddress = reference.whereEqualTo("address", getText(R.string.new_school_full_text));
                FirestoreRecyclerOptions<Task> addressOptions = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryAddress, Task.class)
                        .build();

                taskAdapter.updateOptions(addressOptions);
            }
        });
    }

    void chipWorkBehavior(CollectionReference reference, String status) {
        binding.chipAllTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Query queryAll = reference
                        .whereEqualTo("executor", email)
                        .whereEqualTo("status", status);
                FirestoreRecyclerOptions<Task> allTasks = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryAll, Task.class)
                        .build();

                taskAdapter.updateOptions(allTasks);
            }

        });

        binding.chipUrgentTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Query queryUrgent = reference.
                        whereEqualTo("executor", email)
                        .whereEqualTo("urgent", true);
                FirestoreRecyclerOptions<Task> urgentOptions = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryUrgent, Task.class)
                        .build();

                taskAdapter.updateOptions(urgentOptions);
            }

        });

        binding.chipOldSchoolTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Query queryAddress = reference
                        .whereEqualTo("executor", email)
                        .whereEqualTo("address", getText(R.string.old_school_full_text));
                FirestoreRecyclerOptions<Task> addressOptions = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryAddress, Task.class)
                        .build();

                taskAdapter.updateOptions(addressOptions);
            }

        });

        binding.chipNewSchoolTasksRoot.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Query queryAddress = reference
                        .whereEqualTo("executor", email)
                        .whereEqualTo("address", getText(R.string.new_school_full_text));
                FirestoreRecyclerOptions<Task> addressOptions = new FirestoreRecyclerOptions.Builder<Task>()
                        .setQuery(queryAddress, Task.class)
                        .build();

                taskAdapter.updateOptions(addressOptions);
            }
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
