package com.george.vector.ui.users.user.main.fragments;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_ARCHIVE;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_COMPLETED;
import static com.george.vector.common.consts.Keys.BAR_SCHOOL_PROGRESS;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_ARCHIVE;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_COMPLETED;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_PROGRESS;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_COLLECTION;
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

    String email, permission, collection;
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
        collection = sharedPreferences.getString(USER_PREFERENCES_COLLECTION, "");

        historyUserBinding.taskInProgressUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);

            if(permission.equals(OST_SCHOOL)) {
                collection = OST_SCHOOL_PROGRESS;
            }

            if(permission.equals(BAR_SCHOOL)) {
                collection = BAR_SCHOOL_PROGRESS;
            }

            intent.putExtra(COLLECTION, collection);
            startActivity(intent);
        });

        historyUserBinding.taskCompletedUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, COMPLETED_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);

            if(permission.equals(OST_SCHOOL)) {
                collection = OST_SCHOOL_COMPLETED;
            }

            if(permission.equals(BAR_SCHOOL)) {
                collection = BAR_SCHOOL_COMPLETED;
            }

            intent.putExtra(COLLECTION, collection);
            startActivity(intent);
        });

        historyUserBinding.taskArchiveUser.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentUserHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, ARCHIVE_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);

            if(permission.equals(OST_SCHOOL)) {
                collection = OST_SCHOOL_ARCHIVE;
            }

            if(permission.equals(BAR_SCHOOL)) {
                collection = BAR_SCHOOL_ARCHIVE;
            }

            intent.putExtra(COLLECTION, collection);
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
