package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Logs.TAG_TASK_USER_ACTIVITY;

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
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentTasksUserBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.TaskAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentTasksUser extends Fragment {

    TaskAdapter taskAdapter;
    FragmentTasksUserBinding userBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userBinding = FragmentTasksUserBinding.inflate(inflater, container, false);
        View view = userBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        String email = userDataViewModel.getUser().getEmail();
        String zone = userDataViewModel.getUser().getZone();
        String folder = args.getString(FOLDER);

        if(zone.equals(BAR_SCHOOL)) {
            userBinding.chipNewSchoolTasksUser.setVisibility(View.INVISIBLE);
            userBinding.chipOldSchoolTasksUser.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    void setUpRecyclerView() {
        userBinding.recyclerviewUserTasks.setHasFixedSize(true);
        userBinding.recyclerviewUserTasks.setLayoutManager(new LinearLayoutManager(FragmentTasksUser.this.getContext()));
        userBinding.recyclerviewUserTasks.setAdapter(taskAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userBinding = null;
    }

}
