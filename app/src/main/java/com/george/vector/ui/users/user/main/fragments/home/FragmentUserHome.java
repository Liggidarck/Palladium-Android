package com.george.vector.ui.users.user.main.fragments.home;

import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

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
import com.george.vector.databinding.FragmentUserHomeBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.ui.users.user.tasks.AddTaskUserActivity;
import com.george.vector.ui.users.user.tasks.TaskUserActivity;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

import java.util.ArrayList;

public class FragmentUserHome extends Fragment {

    private FragmentUserHomeBinding homeBinding;

    private int userId;

    private TaskViewModel taskViewModel;
    private final TaskAdapter adapter = new TaskAdapter();

    public static final String TAG = FragmentUserHome.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding = FragmentUserHomeBinding.inflate(inflater, container, false);
        View view = homeBinding.getRoot();

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getActivity().getApplication(),
                userPrefViewModel.getToken()
        )).get(TaskViewModel.class);

        userId = (int) userPrefViewModel.getId();

        homeBinding.homeToolbarUser.setNavigationOnClickListener(v -> {
            BottomSheetProfileUser bottomSheet = new BottomSheetProfileUser();
            bottomSheet.show(getParentFragmentManager(), "ProfileUserBottomSheet");
        });

        getTasks();

        homeBinding.recyclerviewViewUser.setHasFixedSize(true);
        homeBinding.recyclerviewViewUser.setLayoutManager(new LinearLayoutManager(FragmentUserHome.this.getContext()));
        homeBinding.recyclerviewViewUser.setAdapter(adapter);

        adapter.setOnItemClickListener((task, position) -> {
            long id = task.getId();

            Intent intent = new Intent(FragmentUserHome.this.getContext(), TaskUserActivity.class);
            intent.putExtra(ID, id);
            startActivity(intent);
        });

        homeBinding.createTaskUser.setOnClickListener(v ->
                startActivity(new Intent(FragmentUserHome.this.getContext(), AddTaskUserActivity.class))
        );

        return view;
    }

    private void getTasks() {
        taskViewModel
                .getTasksByCreator(userId)
                .observe(FragmentUserHome.this.requireActivity(), tasks -> {
                    ArrayList<Task> sortedTasks = new ArrayList<>();

                    for (Task task : tasks) {
                        if (task.getStatus().equals(NEW_TASKS)) {
                            sortedTasks.add(task);
                        }
                    }

                    adapter.addTasks(sortedTasks);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        getTasks();
    }
}
