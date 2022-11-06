package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

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

import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentTasksRootBinding;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class FragmentTasksRoot extends Fragment {

    private String zone, status, executorEmail, email;

    private TaskAdapter taskAdapter = new TaskAdapter();
    private TaskViewModel taskViewModel;
    private FragmentTasksRootBinding binding;

    public static final String TAG = FragmentTasksRoot.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksRootBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        zone = args.getString(ZONE);
        status = args.getString(STATUS);
        executorEmail = args.getString(EXECUTOR_EMAIL);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        String token = userPrefViewModel.getToken();

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getActivity().getApplication(),
                token
        )).get(TaskViewModel.class);

        if (!zone.equals(OST_SCHOOL)) {
            binding.chipNewSchoolTasksRoot.setVisibility(View.INVISIBLE);
            binding.chipOldSchoolTasksRoot.setVisibility(View.INVISIBLE);
        }

        setUpRecyclerView(zone, status, executorEmail);

        return view;
    }

    void setUpRecyclerView(String zone, String status, String executed) {

        taskViewModel
                .getByZoneLikeAndStatusLike(zone, status)
                .observe(FragmentTasksRoot.this.requireActivity(),
                        tasks -> taskAdapter.addTasks(tasks)
                );

        binding.recyclerviewSchoolOstNewTasks.setHasFixedSize(true);
        binding.recyclerviewSchoolOstNewTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksRoot.this.requireActivity()));
        binding.recyclerviewSchoolOstNewTasks.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener((task, position) -> {
            long id = task.getId();
            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
            startActivity(intent);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
