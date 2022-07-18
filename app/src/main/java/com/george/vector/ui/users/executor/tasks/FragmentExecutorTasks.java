package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.databinding.FragmentExecutorTasksBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentExecutorTasks extends Fragment {

    String location, status, email;

    TaskAdapter adapter;
    FragmentExecutorTasksBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExecutorTasksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        location = args.getString(LOCATION);
        status = args.getString(FOLDER);
        email = args.getString(EMAIL);

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
        setUpQuery();

        binding.recyclerViewTasksExecutor.setHasFixedSize(true);
        binding.recyclerViewTasksExecutor.setLayoutManager(new LinearLayoutManager(FragmentExecutorTasks.this.getContext()));
        binding.recyclerViewTasksExecutor.setAdapter(adapter);
    }

    private void setUpQuery() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference reference = db.collection(location);
        Query query = reference
                .whereEqualTo("executor", email)
                .whereEqualTo("status", status);


        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
