package com.george.vector.users.caretaker.tasks.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomSheetAddTaskBar extends BottomSheetDialogFragment {

    ImageView close_btn;
    Button bar_school_new_task_caretaker, bar_rucheek_new_task_caretaker, bar_start_new_task_caretaker;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_task_bar, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        bar_school_new_task_caretaker = view.findViewById(R.id.bar_school_new_task_caretaker);
        bar_rucheek_new_task_caretaker = view.findViewById(R.id.bar_rucheek_new_task_caretaker);
        bar_start_new_task_caretaker = view.findViewById(R.id.bar_start_new_task_caretaker);

        close_btn.setOnClickListener(v -> dismiss());

        return view;
    }

}