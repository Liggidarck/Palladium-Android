package com.george.vector.ui.users.user.main.fragments;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.FOLDER;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.PERMISSION;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.FragmentHistoryUserBinding;
import com.george.vector.ui.users.user.main.FolderUserActivity;

public class FragmentUserHistory extends Fragment {

    String email, permission;
    FragmentHistoryUserBinding historyUserBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        historyUserBinding = FragmentHistoryUserBinding.inflate(inflater, container, false);
        View view = historyUserBinding.getRoot();

        UserPreferencesViewModel userPrefViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);
        email = userPrefViewModel.getUser().getEmail();
        permission = userPrefViewModel.getUser().getPermission();

        historyUserBinding.taskInProgressUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);

            startActivity(intent);
        });

        historyUserBinding.taskCompletedUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, COMPLETED_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);
        });

        historyUserBinding.taskArchiveUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, ARCHIVE_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        historyUserBinding = null;
    }
}
