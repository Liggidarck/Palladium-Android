package com.george.vector.ui.users.user.main.fragments.help;

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
import com.george.vector.databinding.FragmentHelpUserBinding;

public class FragmentHelp extends Fragment {

    FragmentHelpUserBinding binding;
    Bundle bundle = new Bundle();
    Fragment data = new FragmentDataHelp();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHelpUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.technicalSupport.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "georgyfilatov@yandex.ru", null));
            intent.putExtra("android.intent.extra.SUBJECT", "Помощь с приложением");
            startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));
        });

        binding.cardHowToCreateTaskUser.setOnClickListener(v -> {
            bundle.putString("task", "HowToCreateTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, data).commit();
        });

        binding.cardHowToAddImageTask.setOnClickListener(v -> {
            bundle.putString("task", "HowToAddImageTask");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, data).commit();
        });

        binding.cardStatusTasks.setOnClickListener(v -> {
            bundle.putString("task", "StatusTasks");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, data).commit();
        });

        binding.cardAboutProject.setOnClickListener(v -> {
            Fragment aboutProject = new FragmentAboutProject();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, aboutProject).commit();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
