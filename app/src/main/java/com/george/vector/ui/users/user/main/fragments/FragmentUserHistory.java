package com.george.vector.ui.users.user.main.fragments;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentHistoryUserBinding;
import com.george.vector.ui.users.user.main.FolderUserActivity;

public class FragmentUserHistory extends Fragment {

    private FragmentHistoryUserBinding historyUserBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        historyUserBinding = FragmentHistoryUserBinding.inflate(inflater, container, false);
        View view = historyUserBinding.getRoot();

        historyUserBinding.taskInProgressUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(STATUS, IN_PROGRESS_TASKS);
            startActivity(intent);
        });

        historyUserBinding.taskCompletedUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(STATUS, COMPLETED_TASKS);
            startActivity(intent);
        });

        historyUserBinding.taskArchiveUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(STATUS, ARCHIVE_TASKS);
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
