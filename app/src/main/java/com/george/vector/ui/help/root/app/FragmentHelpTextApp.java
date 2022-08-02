package com.george.vector.ui.help.root.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentTextExecutorBinding;
import com.george.vector.databinding.FragmentTextRootBinding;
import com.george.vector.databinding.FragmentTextUserBinding;

public class FragmentHelpTextApp extends Fragment {

    FragmentTextExecutorBinding executorBinding;
    FragmentTextRootBinding rootBinding;
    FragmentTextUserBinding userBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        assert args != null;
        String user = args.getString("user");
        executorBinding = FragmentTextExecutorBinding.inflate(inflater, container, false);
        rootBinding = FragmentTextRootBinding.inflate(inflater, container, false);
        userBinding = FragmentTextUserBinding.inflate(inflater, container, false);

        userBinding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        if(user.equals("user")) {
            return userBinding.getRoot();
        }

        if(user.equals("root")) {
            return rootBinding.getRoot();
        }

        if(user.equals("executor")) {
            return executorBinding.getRoot();
        }

        return null;
    }


}
