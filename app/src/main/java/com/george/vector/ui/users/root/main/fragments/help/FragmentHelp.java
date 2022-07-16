package com.george.vector.ui.users.root.main.fragments.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentRootHelpBinding;
import com.george.vector.ui.users.root.main.fragments.help.full_help_app.app.FragmentHelpApp;

public class FragmentHelp extends Fragment {

    FragmentRootHelpBinding helpBinding;
    Fragment data = new FragmentDataHelp();
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        helpBinding = FragmentRootHelpBinding.inflate(inflater, container, false);
        View view = helpBinding.getRoot();

        helpBinding.technicalSupport.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "liggidarck@gmail.com", null));
            intent.putExtra("android.intent.extra.SUBJECT", "Помощь с приложением");
            startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));
        });

        helpBinding.cardHowToCreateTask.setOnClickListener(v -> {
            bundle.putString("task", "HowToCreateTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHowToEditTask.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToEditTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHowToDeleteTask.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToDeleteTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHowToAddUser.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToAddUser");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHelpTasks.setOnClickListener(v -> {
            bundle.putString("task", "full_help_tasks");

        });

        helpBinding.cardAppHelp.setOnClickListener(v -> {
            Fragment fragmentHelpApp = new FragmentHelpApp();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, fragmentHelpApp).commit();
        });

        helpBinding.cardAboutProject.setOnClickListener(v -> {
            Fragment aboutProject = new FragmentAboutProject();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, aboutProject).commit();
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helpBinding = null;
    }
}
