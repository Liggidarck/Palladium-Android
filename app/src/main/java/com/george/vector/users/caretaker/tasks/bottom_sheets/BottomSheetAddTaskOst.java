package com.george.vector.users.caretaker.tasks.bottom_sheets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.george.vector.users.caretaker.tasks.AddTaskCaretakerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomSheetAddTaskOst extends BottomSheetDialogFragment {

    ImageView close_btn;
    Button ost_school_new_task_caretaker, ost_aist_new_task_caretaker, ost_yagodka_new_task_caretaker;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_caretaker_add_task, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        ost_school_new_task_caretaker = view.findViewById(R.id.ost_school_new_task_caretaker);
        ost_aist_new_task_caretaker = view.findViewById(R.id.ost_aist_new_task_caretaker);
        ost_yagodka_new_task_caretaker = view.findViewById(R.id.ost_yagodka_new_task_caretaker);

        close_btn.setOnClickListener(v -> dismiss());

        ost_school_new_task_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTaskOst.this.getContext(), AddTaskCaretakerActivity.class);
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            startActivity(intent);
        });

        return view;
    }
}
