package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.STATUS;

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

import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.FragmentExecutorTasksBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

import java.util.ArrayList;

public class FragmentExecutorTasks extends Fragment {

    private final TaskAdapter adapter = new TaskAdapter();

    private FragmentExecutorTasksBinding binding;

    public static final String TAG = FragmentExecutorTasks.class.getSimpleName();

    private String zone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExecutorTasksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        zone = args.getString(ZONE);
        String status = args.getString(STATUS);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.requireActivity().getApplication(),
                userPrefViewModel.getToken()
        )).get(TaskViewModel.class);

        long executorId = userPrefViewModel.getId();

        setUpRecyclerView();

        taskViewModel.getTasksByExecutor(executorId).observe(this.requireActivity(), tasks -> {
            ArrayList<Task> filterList = new ArrayList<>();

            for(Task task : tasks) {
                if(task.getZone().equals(zone) & task.getStatus().equals(status)) {
                    filterList.add(task);
                }
            }

            adapter.setTasks(filterList);
        });

        return view;
    }

    private void setUpRecyclerView() {
        binding.recyclerViewTasksExecutor.setHasFixedSize(true);
        binding.recyclerViewTasksExecutor.setLayoutManager(new LinearLayoutManager(FragmentExecutorTasks.this.getContext()));
        binding.recyclerViewTasksExecutor.setAdapter(adapter);

        adapter.setOnItemClickListener((task, position) -> {
            Intent intent = new Intent(this.requireActivity(), TaskExecutorActivity.class);
            intent.putExtra(ID, task.getId());
            intent.putExtra(ZONE, zone);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
