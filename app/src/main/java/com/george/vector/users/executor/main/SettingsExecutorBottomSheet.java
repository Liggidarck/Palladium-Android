package com.george.vector.users.executor.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.george.vector.auth.ActivityLogin;
import com.george.vector.common.bottom_sheets.ConsoleBottomSheet;
import com.george.vector.common.edit_users.EditDataUserActivity;
import com.george.vector.common.settings.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsExecutorBottomSheet extends BottomSheetDialogFragment {

    RelativeLayout layout_settings_executor, layout_edit_person_executor;
    Button btn_logout_executor;
    ImageView close_btn_executor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_executor_bottom_sheet, container, false);

        layout_settings_executor = view.findViewById(R.id.layout_settings_executor);
        layout_edit_person_executor = view.findViewById(R.id.layout_edit_person_executor);
        btn_logout_executor = view.findViewById(R.id.btn_logout_executor);
        close_btn_executor = view.findViewById(R.id.close_btn_executor);

        assert this.getArguments() != null;
        String email = this.getArguments().getString(getString(R.string.email));
        Log.d("ExecutorBottomSheet", String.format("email: %s", email));

        close_btn_executor.setOnClickListener(v -> dismiss());
        layout_edit_person_executor.setOnClickListener(v -> startActivity(new Intent(SettingsExecutorBottomSheet.this.getContext(), EditDataUserActivity.class)));
        btn_logout_executor.setOnClickListener(v -> {

        });

        layout_settings_executor.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsExecutorBottomSheet.this.getContext(), SettingsActivity.class);
            intent.putExtra(getString(R.string.permission), "executor");
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        return view;
    }
}
