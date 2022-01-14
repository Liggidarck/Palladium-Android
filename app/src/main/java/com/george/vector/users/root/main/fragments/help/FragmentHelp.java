package com.george.vector.users.root.main.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentRootHelpBinding;

public class FragmentHelp extends Fragment {

    FragmentRootHelpBinding helpBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        helpBinding = FragmentRootHelpBinding.inflate(inflater, container, false);
        View view = helpBinding.getRoot();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helpBinding = null;
    }
}
