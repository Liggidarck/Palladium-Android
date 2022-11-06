package com.george.vector.ui.users.root.tasks.navigation;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

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

import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.FragmentTasksRootBinding;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.ui.users.root.tasks.TaskRootActivity;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class FragmentTasksRoot extends Fragment {

    private String zone, status, executed, email;

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
        boolean executed = args.getBoolean(IS_EXECUTE);

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

        setUpRecyclerView(zone, status, executed);

        return view;
    }

    void setUpRecyclerView(String zone, String status, boolean executed) {

        if (!executed) {
            taskViewModel
                    .getByZoneLikeAndStatusLike(zone, status)
                    .observe(FragmentTasksRoot.this.requireActivity(),
                            tasks -> taskAdapter.addTasks(tasks)
                    );

        } else {
            UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
            taskViewModel
                    .getTasksByExecutor(userDataViewModel.getId())
                    .observe(FragmentTasksRoot.this.requireActivity(),
                            tasks -> taskAdapter.addTasks(tasks)
                    );
        }

        binding.recyclerviewSchoolOstNewTasks.setHasFixedSize(true);
        binding.recyclerviewSchoolOstNewTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksRoot.this.requireActivity()));
        binding.recyclerviewSchoolOstNewTasks.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener((task, position) -> {
            long id = task.getId();
            Log.d(TAG, "setUpRecyclerView: " + id);
            Intent intent = new Intent(FragmentTasksRoot.this.getContext(), TaskRootActivity.class);
            intent.putExtra(ID, id);
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
