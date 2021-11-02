package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.george.vector.databinding.BottomSheetAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddTask extends BottomSheetDialogFragment {

    String email;

    BottomSheetAddTaskBinding taskBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskBinding = BottomSheetAddTaskBinding.inflate(inflater, container, false);
        View view = taskBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);

        taskBinding.ostSchoolNewTask.setOnClickListener(v-> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra(LOCATION, OST_SCHOOL);
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
