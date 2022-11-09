package com.george.vector.ui.users.admin.tasks.contoll;

import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.OST_AIST;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_YAGODKA;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.george.vector.databinding.BottomSheetAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddTask extends BottomSheetDialogFragment {

    BottomSheetAddTaskBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BottomSheetAddTaskBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.ostSchoolNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskAdminActivity.class);
            intent.putExtra(ZONE, OST_SCHOOL);
            startActivity(intent);
        });

        binding.ostAistNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskAdminActivity.class);
            intent.putExtra(ZONE, OST_AIST);
            startActivity(intent);
        });

        binding.ostYagodkaNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskAdminActivity.class);
            intent.putExtra(ZONE, OST_YAGODKA);
            startActivity(intent);
        });

        binding.barSchoolNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskAdminActivity.class);
            intent.putExtra(ZONE, BAR_SCHOOL);
            startActivity(intent);
        });

        binding.barRucheekNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskAdminActivity.class);
            intent.putExtra(ZONE, BAR_RUCHEEK);
            startActivity(intent);
        });

        binding.barStartNewTask.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskAdminActivity.class);
            intent.putExtra(ZONE, BAR_ZVEZDOCHKA);
            startActivity(intent);
        });

        binding.closeBtn.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
