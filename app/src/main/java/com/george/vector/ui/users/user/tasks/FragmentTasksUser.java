package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.STATUS;

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
import com.george.vector.databinding.FragmentTasksUserBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

import java.util.ArrayList;

public class FragmentTasksUser extends Fragment {

    private String status;
    private long userId;

    private final TaskAdapter taskAdapter = new TaskAdapter();

    private TaskViewModel taskViewModel;

    private FragmentTasksUserBinding binding;

    ArrayList<Task> allTasks = new ArrayList<>();

    public static final String TAG = FragmentTasksUser.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.requireActivity().getApplication(),
                userDataViewModel.getToken()
        )).get(TaskViewModel.class);

        Bundle args = getArguments();
        assert args != null;
        status = args.getString(STATUS);
        userId = userDataViewModel.getId();

        setUpRecyclerView();

        getAllTasks();

        return view;
    }

    private void getAllTasks() {
        taskViewModel.getTasksByCreator(userId).observe(FragmentTasksUser.this.requireActivity(), tasks -> {

            for (Task task : tasks) {
                if (task.getStatus().equals(status)) {
                    allTasks.add(task);
                }
            }

            taskAdapter.addTasks(allTasks);
        });
    }

    void setUpRecyclerView() {
        binding.recyclerviewUserTasks.setHasFixedSize(true);
        binding.recyclerviewUserTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksUser.this.getContext()));
        binding.recyclerviewUserTasks.setAdapter(taskAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
