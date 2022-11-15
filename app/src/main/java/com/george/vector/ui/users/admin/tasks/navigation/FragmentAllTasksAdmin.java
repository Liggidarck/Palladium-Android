package com.george.vector.ui.users.admin.tasks.navigation;

import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.ZONE;

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
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.george.vector.ui.users.admin.tasks.TaskAdminActivity;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class FragmentAllTasksAdmin extends Fragment {

    private FragmentTasksRootBinding binding;

    private final TaskAdapter taskAdapter = new TaskAdapter();
    private TaskViewModel taskViewModel;

    private String zone, status;
    private boolean executed;

    public static final String TAG = FragmentAllTasksAdmin.class.getSimpleName();

    private List<Task> tasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksRootBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        zone = args.getString(ZONE);
        status = args.getString(STATUS);
        executed = args.getBoolean(IS_EXECUTE);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getActivity().getApplication(),
                userPrefViewModel.getToken()
        )).get(TaskViewModel.class);

        if (!zone.equals(OST_SCHOOL)) {
            binding.chipNewSchoolTasksRoot.setVisibility(View.INVISIBLE);
            binding.chipOldSchoolTasksRoot.setVisibility(View.INVISIBLE);
        }

        binding.chipAllTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskAdapter.setTasks(tasks);
            }
        });

        binding.chipUrgentTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked & !tasks.isEmpty()) {
                ArrayList<Task> filterTasks = new ArrayList<>();

                for (Task task : tasks) {
                    if (task.isUrgent()) {
                        filterTasks.add(task);
                    }
                }

                taskAdapter.filterList(filterTasks);
            }
        });

        binding.chipNewSchoolTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked & !tasks.isEmpty()) {
                ArrayList<Task> filterTasks = new ArrayList<>();

                for (Task task : tasks) {
                    if (task.getAddress().equals("Авиаторов 9. Старшая школа")) {
                        filterTasks.add(task);
                    }
                }

                taskAdapter.filterList(filterTasks);
            }
        });

        binding.chipOldSchoolTasksRoot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked & !tasks.isEmpty()) {
                ArrayList<Task> filterTasks = new ArrayList<>();

                for (Task task : tasks) {
                    if (task.getAddress().equals("Авиаторов 9. Начальная школа")) {
                        filterTasks.add(task);
                    }
                }

                taskAdapter.filterList(filterTasks);
            }
        });

        setUpRecyclerView(zone, status, executed);

        return view;
    }

    void setUpRecyclerView(String zone, String status, boolean executed) {
        updateListTasks(zone, status, executed);

        binding.recyclerviewSchoolOstNewTasks.setHasFixedSize(true);
        binding.recyclerviewSchoolOstNewTasks.setLayoutManager(new
                LinearLayoutManager(FragmentAllTasksAdmin.this.requireActivity()));
        binding.recyclerviewSchoolOstNewTasks.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener((task, position) -> {
            long id = task.getId();
            Log.d(TAG, "setUpRecyclerView: " + id);
            Intent intent = new Intent(FragmentAllTasksAdmin.this.getContext(), TaskAdminActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(ZONE, zone);
            intent.putExtra(IS_EXECUTE, executed);
            startActivity(intent);
        });

    }

    private void updateListTasks(String zone, String status, boolean executed) {
        if (!executed) {
            taskViewModel.getByZoneLikeAndStatusLike(zone, status)
                    .observe(FragmentAllTasksAdmin.this.requireActivity(), tasks -> {
                        if (tasks == null) {
                            binding.progressAllTasks.setVisibility(View.INVISIBLE);
                            return;
                        }

                        if (tasks.isEmpty()) {
                            Log.d(TAG, "updateListTasks: empty");
                            try {
                                binding.emptyView.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        taskAdapter.setTasks(tasks);
                        this.tasks = tasks;

                    });
        } else {
            UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

            taskViewModel.getByZoneLikeAndStatusLikeAndExecutorId(zone, status, (int) userDataViewModel.getId())
                    .observe(FragmentAllTasksAdmin.this.requireActivity(), tasks -> {
                        if (tasks == null) {
                            binding.progressAllTasks.setVisibility(View.INVISIBLE);
                            return;
                        }

                        if (tasks.isEmpty()) {
                            Log.d(TAG, "updateListTasks: empty");
                            try {
                                binding.emptyView.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        taskAdapter.setTasks(tasks);
                        this.tasks = tasks;
                    });
        }

        binding.progressAllTasks.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateListTasks(zone, status, executed);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
