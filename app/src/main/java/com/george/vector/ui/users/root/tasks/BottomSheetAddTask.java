package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.BottomSheetAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddTask extends BottomSheetDialogFragment {

    String email;

    BottomSheetAddTaskBinding taskBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskBinding = BottomSheetAddTaskBinding.inflate(inflater, container, false);
        View view = taskBinding.getRoot();

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        email = userPrefViewModel.getUser().getEmail();

        taskBinding.ostSchoolNewTask.setOnClickListener(v-> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra(COLLECTION, OST_SCHOOL);
            startActivity(intent);
        });

        taskBinding.barSchoolNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra(COLLECTION, BAR_SCHOOL);
            startActivity(intent);
        });

        taskBinding.closeBtn.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        taskBinding = null;
    }

}
