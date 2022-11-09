package com.george.vector.ui.common.help.user;

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

import com.bumptech.glide.Glide;
import com.george.vector.R;
import com.george.vector.databinding.FragmentHelpUserBinding;

public class FragmentUserHelp extends Fragment {

    private FragmentHelpUserBinding binding;
    private final Bundle bundle = new Bundle();

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

        binding.downloadFullText.setOnClickListener(v -> {
            String url = getString(R.string.url_download_text_help);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80")
                .centerCrop()
                .into(binding.imageHowToCreateTask);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1542744173-8e7e53415bb0?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80")
                .centerCrop()
                .into(binding.imageHowToEditTask);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1523289333742-be1143f6b766?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80")
                .centerCrop()
                .into(binding.imageHowToDeleteTask);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
