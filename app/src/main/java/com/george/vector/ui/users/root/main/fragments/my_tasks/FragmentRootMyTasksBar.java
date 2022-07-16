package com.george.vector.ui.users.root.main.fragments.my_tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentBarRootBinding;

public class FragmentRootMyTasksBar extends Fragment {

    FragmentBarRootBinding barRootBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        barRootBinding = FragmentBarRootBinding.inflate(inflater, container, false);

        return barRootBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        barRootBinding = null;
    }
}
