package com.george.vector.ui.users.user.main.fragments;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PERMISSION;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    String email, permission;
    FragmentHistoryUserBinding historyUserBinding;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        historyUserBinding = FragmentHistoryUserBinding.inflate(inflater, container, false);
        View view = historyUserBinding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");
        permission = sharedPreferences.getString(USER_PREFERENCES_PERMISSION, "");

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
