package com.george.vector.users.user.main.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentDataHelpUserBinding;

public class FragmentDataHelp extends Fragment {

    FragmentDataHelpUserBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDataHelpUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String task = args.getString("task");

        binding.toolbarQaUser.setNavigationOnClickListener(v -> {
            Fragment fragmentHelp = new FragmentHelp();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, fragmentHelp).commit();
        });

        if (task.equals("HowToCreateTask")) {
            binding.titleHelp.setText(getString(R.string.how_to_create_task_title));
            binding.bodyHelp.setText(getString(R.string.how_to_create_task_user));
        }

        if (task.equals("HowToAddImageTask")) {
            binding.titleHelp.setText(getString(R.string.how_to_add_image_user_title));
            binding.bodyHelp.setText(getString(R.string.how_to_add_image_user));
        }

        if (task.equals("StatusTasks")) {
            binding.titleHelp.setText("Что такое статус заявки? Виды статусов заявки.");
            binding.bodyHelp.setText(getString(R.string.status_tasks_user_text));
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
