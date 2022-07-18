package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.george.vector.databinding.BottomSheetAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddTask extends BottomSheetDialogFragment {

    String email;
    SharedPreferences sharedPreferences;

    BottomSheetAddTaskBinding taskBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskBinding = BottomSheetAddTaskBinding.inflate(inflater, container, false);
        View view = taskBinding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");

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
