package com.george.vector.ui.users.executor.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentBarBinding;

import org.jetbrains.annotations.NotNull;

public class FragmentBarExecutor extends Fragment {

    FragmentBarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        UserDataViewModel userViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        String email = userViewModel.getUser().getEmail();

        return view;
    }
}
