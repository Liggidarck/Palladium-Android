package com.george.vector.users.root.main.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentAboutProjectBinding;
import com.george.vector.users.root.main.fragments.help.develop_app.FragmentDevelopApp;

public class FragmentAboutProject extends Fragment {

    FragmentAboutProjectBinding aboutProject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        aboutProject = FragmentAboutProjectBinding.inflate(inflater, container, false);
        View view = aboutProject.getRoot();

        aboutProject.toolbarAboutProject.setNavigationOnClickListener(v -> {
            Fragment data = new FragmentHelp();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        aboutProject.cardDescriptionProject.setOnClickListener(v -> {
            Fragment descriptionProject = new FragmentDescriptionProject();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, descriptionProject).commit();
        });

        aboutProject.cardDevelopApp.setOnClickListener(v -> {
            Fragment developFragment = new FragmentDevelopApp();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, developFragment).commit();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        aboutProject = null;
    }
}