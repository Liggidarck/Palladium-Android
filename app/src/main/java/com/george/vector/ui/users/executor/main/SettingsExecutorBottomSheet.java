package com.george.vector.ui.users.executor.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.SettingsExecutorBottomSheetBinding;
import com.george.vector.ui.common.auth.LoginActivity;
import com.george.vector.ui.common.settings.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsExecutorBottomSheet extends BottomSheetDialogFragment {

    private SettingsExecutorBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SettingsExecutorBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        binding.btnClose.setOnClickListener(v -> dismiss());

        binding.btnSettings.setOnClickListener(v ->
                startActivity(new Intent(SettingsExecutorBottomSheet.this.getContext(), SettingsActivity.class))
        );

        binding.btnLogout.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(SettingsExecutorBottomSheet.this.requireActivity())
                    .setTitle(getString(R.string.warning))
                    .setMessage("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("ok", (dialog1, which) -> {
                        userDataViewModel.deleteUserData();
                        startActivity(new Intent(SettingsExecutorBottomSheet.this.requireActivity(),
                                LoginActivity.class));
                    })
                    .setNegativeButton("Отмена", (dialog12, which) -> dialog12.dismiss())
                    .create();
            dialog.show();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
