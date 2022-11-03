package com.george.vector.ui.users.executor.main.fragments;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentOstBinding;
import com.george.vector.ui.users.executor.main.FolderExecutorActivity;

public class FragmentOstExecutor extends Fragment {

    FragmentOstBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOstBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.ostSchool.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentOstExecutor.this.getContext(), FolderExecutorActivity.class);
            intent.putExtra(ZONE, OST_SCHOOL);
            startActivity(intent);
        });

        return view;
    }
}
