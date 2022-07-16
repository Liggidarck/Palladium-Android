package com.george.vector.ui.users.executor.main;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.ui.edit_users.EditDataUserActivity;
import com.george.vector.ui.settings.SettingsActivity;
import com.george.vector.databinding.SettingsExecutorBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsExecutorBottomSheet extends BottomSheetDialogFragment {

    SettingsExecutorBottomSheetBinding sheetBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sheetBinding = SettingsExecutorBottomSheetBinding.inflate(inflater, container, false);
        View view = sheetBinding.getRoot();

        assert this.getArguments() != null;
        String email = this.getArguments().getString(EMAIL);
        Log.d("ExecutorBottomSheet", String.format("email: %s", email));

        sheetBinding.closeBtnExecutor.setOnClickListener(v -> dismiss());
        sheetBinding.layoutEditPersonExecutor.setOnClickListener(v -> startActivity(new Intent(SettingsExecutorBottomSheet.this.getContext(), EditDataUserActivity.class)));

        sheetBinding.layoutSettingsExecutor.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsExecutorBottomSheet.this.getContext(), SettingsActivity.class);
            intent.putExtra(PERMISSION, "executor");
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sheetBinding = null;
    }
}
