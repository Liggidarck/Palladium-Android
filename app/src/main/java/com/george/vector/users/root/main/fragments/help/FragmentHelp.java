package com.george.vector.users.root.main.fragments.help;

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

public class FragmentHelp extends Fragment {

    FragmentRootHelpBinding helpBinding;
    Fragment data = new FragmentDataHelp();

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
            Bundle bundle = new Bundle();
            bundle.putString("task", "HowToCreateTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHowToEditTask.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("task", "cardHowToEditTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHowToDeleteTask.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("task", "cardHowToDeleteTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardHowToAddUser.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("task", "cardHowToAddUser");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        helpBinding.cardAppHelp.setOnClickListener(v -> {

        });

        helpBinding.cardAboutProject.setOnClickListener(v -> {

        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helpBinding = null;
    }
}
