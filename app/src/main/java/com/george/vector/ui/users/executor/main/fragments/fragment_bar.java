package com.george.vector.ui.users.executor.main.fragments;

import static com.george.vector.common.consts.Keys.EMAIL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentBarExecutorBinding;

import org.jetbrains.annotations.NotNull;

public class fragment_bar extends Fragment {

    FragmentBarExecutorBinding executorBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        executorBinding = FragmentBarExecutorBinding.inflate(inflater, container, false);
        View view = executorBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(EMAIL);

        return view;
    }
}
