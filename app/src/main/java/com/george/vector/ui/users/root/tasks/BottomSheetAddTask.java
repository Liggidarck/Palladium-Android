package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.LOCATION;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.BottomSheetAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddTask extends BottomSheetDialogFragment {

    String email;

    BottomSheetAddTaskBinding taskBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskBinding = BottomSheetAddTaskBinding.inflate(inflater, container, false);
        View view = taskBinding.getRoot();

        UserPreferencesViewModel userPrefViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);
        email = userPrefViewModel.getUser().getEmail();

        taskBinding.ostSchoolNewTask.setOnClickListener(v-> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        taskBinding.barSchoolNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra(LOCATION, BAR_SCHOOL);
            intent.putExtra(EMAIL, email);
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
