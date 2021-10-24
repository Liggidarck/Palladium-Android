package com.george.vector.users.user.main.fragments;

import static com.george.vector.common.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.FOLDER;
import static com.george.vector.common.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_ARCHIVE;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_COMPLETED;
import static com.george.vector.common.consts.Keys.OST_SCHOOL_PROGRESS;
import static com.george.vector.common.consts.Keys.PERMISSION;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.users.user.main.FolderUserActivity;
import com.google.android.material.card.MaterialCardView;

public class FragmentHistory extends Fragment {

    MaterialCardView task_in_progress_user, task_completed_user, task_archive_user;
    String email, permission;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_user, container, false);

        task_in_progress_user = view.findViewById(R.id.task_in_progress_user);
        task_completed_user = view.findViewById(R.id.task_completed_user);
        task_archive_user = view.findViewById(R.id.task_archive_user);

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);
        permission = args.getString(PERMISSION);

        task_in_progress_user.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, IN_PROGRESS_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            intent.putExtra(COLLECTION, OST_SCHOOL_PROGRESS);
            startActivity(intent);
        });

        task_completed_user.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, COMPLETED_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            intent.putExtra(COLLECTION, OST_SCHOOL_COMPLETED);
            startActivity(intent);
        });

        task_archive_user.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentHistory.this.getContext(), FolderUserActivity.class);
            intent.putExtra(FOLDER, ARCHIVE_TASKS);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PERMISSION, permission);
            intent.putExtra(COLLECTION, OST_SCHOOL_ARCHIVE);
            startActivity(intent);
        });

        return view;
    }
}
