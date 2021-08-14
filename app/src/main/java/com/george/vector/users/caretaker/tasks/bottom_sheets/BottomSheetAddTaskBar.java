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

public class BottomSheetAddTaskBar extends BottomSheetDialogFragment {

    ImageView close_btn;
    Button bar_school_new_task_caretaker, bar_rucheek_new_task_caretaker, bar_start_new_task_caretaker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_task_bar, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        bar_school_new_task_caretaker = view.findViewById(R.id.bar_school_new_task_caretaker);
        bar_rucheek_new_task_caretaker = view.findViewById(R.id.bar_rucheek_new_task_caretaker);
        bar_start_new_task_caretaker = view.findViewById(R.id.bar_start_new_task_caretaker);

        bar_school_new_task_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTaskBar.this.getContext(), AddTaskCaretakerActivity.class);
            intent.putExtra(getString(R.string.location), getString(R.string.bar_school));
            startActivity(intent);
        });

        close_btn.setOnClickListener(v -> dismiss());

        return view;
    }

}