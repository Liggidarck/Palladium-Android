package com.george.vector.ui.help.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.george.vector.R;
import com.george.vector.databinding.FragmentHelpUserBinding;

public class FragmentUserHelp extends Fragment {

    FragmentHelpUserBinding binding;
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHelpUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        NavController navController =
                Navigation.findNavController(FragmentUserHelp.this.requireActivity(),
                        R.id.navHostFragmentActivityUserMain);

        binding.technicalSupport.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "georgyfilatov@yandex.ru", null));
            intent.putExtra("android.intent.extra.SUBJECT", "Помощь с приложением");
            startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));
        });

        binding.cardHowToCreateTaskUser.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToCreateTaskUser");
            navController.navigate(R.id.action_nav_profile_user_to_fragmentDataHelp2, bundle);
        });

        binding.cardHowToAddImageTask.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToAddImageTask");
            navController.navigate(R.id.action_nav_profile_user_to_fragmentDataHelp2, bundle);
        });

        binding.cardStatusTasks.setOnClickListener(v -> {
            bundle.putString("task", "cardStatusTasks");
            navController.navigate(R.id.action_nav_profile_user_to_fragmentDataHelp2, bundle);
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
