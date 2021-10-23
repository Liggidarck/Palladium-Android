package com.george.vector.users.user.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.google.android.material.card.MaterialCardView;

public class FragmentHistory extends Fragment {

    MaterialCardView task_in_progress_user, task_completed_user, task_archive_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_user, container, false);

        task_in_progress_user = view.findViewById(R.id.task_in_progress_user);
        task_completed_user = view.findViewById(R.id.task_completed_user);
        task_archive_user = view.findViewById(R.id.task_archive_user);

        return view;
    }
}
