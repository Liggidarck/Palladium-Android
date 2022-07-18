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

import com.george.vector.databinding.FragmentExecutorTasksBinding;
import com.george.vector.ui.adapter.TaskAdapter;

public class FragmentExecutorTasks extends Fragment {

    String location, folder, email;

    TaskAdapter adapter;
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

        return view;
    }

    void setUpRecyclerView() {
        tasksBinding.recyclerViewTasksExecutor.setHasFixedSize(true);
        tasksBinding.recyclerViewTasksExecutor.setLayoutManager(new LinearLayoutManager(FragmentExecutorTasks.this.getContext()));
        tasksBinding.recyclerViewTasksExecutor.setAdapter(adapter);
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
