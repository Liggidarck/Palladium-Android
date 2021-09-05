package com.george.vector.users.root.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.george.vector.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetAddTask extends BottomSheetDialogFragment {

    Button ost_school_new_task, ost_aist_new_task, ost_yagodka_new_task,
            bar_school_new_task, bar_rucheek_new_task, bar_start_new_task;
    ImageView close_btn;

    String email;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_task, container, false);

        ost_school_new_task = view.findViewById(R.id.ost_school_new_task);
        ost_aist_new_task = view.findViewById(R.id.ost_aist_new_task);
        ost_yagodka_new_task = view.findViewById(R.id.ost_yagodka_new_task);
        bar_school_new_task = view.findViewById(R.id.bar_school_new_task);
        bar_rucheek_new_task = view.findViewById(R.id.bar_rucheek_new_task);
        bar_start_new_task = view.findViewById(R.id.bar_start_new_task);
        close_btn = view.findViewById(R.id.close_btn);

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(getString(R.string.email));

        ost_school_new_task.setOnClickListener(v-> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra("location", "ost_school");
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        bar_school_new_task.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetAddTask.this.getContext(), AddTaskRootActivity.class);
            intent.putExtra("location", "bar_school");
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        close_btn.setOnClickListener(v -> dismiss());

        return view;
    }
}
