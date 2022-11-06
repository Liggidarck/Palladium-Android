package com.george.vector.ui.common.help.root;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentDataHelpBinding;

public class FragmentDataHelp extends Fragment {

    FragmentDataHelpBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentDataHelpBinding.inflate(inflater, container,false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String task = args.getString("task");

        binding.toolbarQa.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        if (task.equals("HowToCreateTask")) {
            binding.titleHelp.setText(getString(R.string.how_to_create_task_title));
            binding.bodyHelp.setText(getString(R.string.how_to_create_task_body));
        }

        if (task.equals("cardHowToEditTask")) {
            binding.titleHelp.setText(getString(R.string.how_to_edit_task_title));
            binding.bodyHelp.setText(getString(R.string.how_to_edit_task_body));
        }

        if (task.equals("cardHowToDeleteTask")) {
            binding.titleHelp.setText(getString(R.string.how_to_delete_task_title));
            binding.bodyHelp.setText(getString(R.string.how_to_delete_task_body));
        }

        if (task.equals("cardHowToAddUser")) {
            binding.titleHelp.setText(getString(R.string.how_to_add_user_title));
            binding.bodyHelp.setText(getString(R.string.how_to_add_user_body));
        }

        if (task.equals("fullHelpTasks")) {
            binding.titleHelp.setText(getString(R.string.tasks));
            binding.bodyHelp.setText(getString(R.string.task_help));
        }

        if (task.equals("fullHelpUsers")) {
            binding.titleHelp.setText(getString(R.string.users_text));
            binding.bodyHelp.setText(getString(R.string.user_help));
        }

        if(task.equals("cardHowToCreateTaskUser")) {
            binding.titleHelp.setText(getString(R.string.tasks));
            binding.bodyHelp.setText(getString(R.string.task_hel_user));
        }

        if(task.equals("cardHowToAddImageTask")) {
            binding.titleHelp.setText("Как добавить изображение к заявке?");
            binding.bodyHelp.setText("Для того чтобы прикрепить изображение к заявке необходимо на экране создания заявки нажать на поле изображение. Далее откроется диалоговое окно, которое предложит сделать изображение или выбрать изображение с памяти устройства.");
        }

        if(task.equals("cardStatusTasks")) {
            binding.titleHelp.setText("Что такое статус заявки? Виды статусов заявки.");
            binding.bodyHelp.setText(getText(R.string.text_help_status_user));

        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
