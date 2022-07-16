package com.george.vector.ui.users.user.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentProfileUserBinding;

public class FragmentHelp extends Fragment {

    FragmentProfileUserBinding profileBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileBinding = FragmentProfileUserBinding.inflate(inflater, container, false);
        View view = profileBinding.getRoot();

        return view;
    }
}
