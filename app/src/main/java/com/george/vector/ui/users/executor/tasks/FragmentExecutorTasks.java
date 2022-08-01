package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentExecutorTasksBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentExecutorTasks extends Fragment {

    String collection, status, email;

    TaskAdapter adapter;
    FragmentExecutorTasksBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExecutorTasksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        collection = args.getString(COLLECTION);
        status = args.getString(FOLDER);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        email = userPrefViewModel.getUser().getEmail();

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
        final CollectionReference reference = db.collection(collection);
        Query query = reference
                .whereEqualTo("executor", email)
                .whereEqualTo("status", status);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        adapter = new TaskAdapter(options);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Intent intent = new Intent(FragmentExecutorTasks.this.requireActivity(), TaskExecutorActivity.class);
            intent.putExtra(ID, documentSnapshot.getId());
            intent.putExtra(COLLECTION, collection);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
