package com.george.vector.users.executor.main.fragments;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentOstExecutorBinding;
import com.george.vector.users.executor.main.FolderExecutorActivity;

public class fragment_ost extends Fragment {

    FragmentOstExecutorBinding executorBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        executorBinding = FragmentOstExecutorBinding.inflate(inflater, container, false);
        View view = executorBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(EMAIL);

        executorBinding.ostSchoolExecutor.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_ost.this.getContext(), FolderExecutorActivity.class);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }
}
