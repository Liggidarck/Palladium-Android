package com.george.vector.ui.users.executor.main.fragments;

import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.LOCATION;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentOstExecutorBinding;
import com.george.vector.ui.users.executor.main.FolderExecutorActivity;

public class FragmentOstExecutor extends Fragment {

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
            Intent intent = new Intent(FragmentOstExecutor.this.getContext(), FolderExecutorActivity.class);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }
}
