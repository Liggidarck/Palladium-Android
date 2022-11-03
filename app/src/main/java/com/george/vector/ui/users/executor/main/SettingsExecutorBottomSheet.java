package com.george.vector.ui.users.executor.main;

import static com.george.vector.common.utils.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.SettingsExecutorBottomSheetBinding;
import com.george.vector.network.model.User;
import com.george.vector.ui.auth.LoginActivity;
import com.george.vector.ui.edit_users.EditDataUserActivity;
import com.george.vector.ui.settings.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsExecutorBottomSheet extends BottomSheetDialogFragment {

    private SettingsExecutorBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SettingsExecutorBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        String email = userDataViewModel.getUser().getEmail();
        Log.d("ExecutorBottomSheet", String.format("email: %s", email));

        binding.btnClose.setOnClickListener(v -> dismiss());
        binding.btnEditUser.setOnClickListener(v ->
                startActivity(new Intent(SettingsExecutorBottomSheet.this.getContext(), EditDataUserActivity.class))
        );

        binding.btnSettings.setOnClickListener(v ->
                startActivity(new Intent(SettingsExecutorBottomSheet.this.getContext(), SettingsActivity.class))
        );

        binding.btnLogout.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(SettingsExecutorBottomSheet.this.requireActivity())
                    .setTitle(getString(R.string.warning))
                    .setMessage("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("ok", (dialog1, which) -> {

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
