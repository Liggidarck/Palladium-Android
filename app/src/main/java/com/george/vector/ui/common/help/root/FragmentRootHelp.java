package com.george.vector.ui.common.help.root;

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
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.george.vector.R;
import com.george.vector.databinding.FragmentRootHelpBinding;

public class FragmentRootHelp extends Fragment {

    private FragmentRootHelpBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRootHelpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle bundle = new Bundle();

        NavController navController =
                NavHostFragment.findNavController(this);

        binding.technicalSupport.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO",
                    Uri.fromParts("mailto", getString(R.string.email_developer), null));
            intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.text_help));
            startActivity(Intent.createChooser(intent,
                    getString(R.string.text_send_email)));
        });

        binding.cardAppHelp.setOnClickListener(v ->
                navController.navigate(R.id.actionNavHelpToFragmentHelpApp));

        binding.cardAboutProject.setOnClickListener(v ->
                navController.navigate(R.id.actionNavHelpToFragmentAboutProject));

        binding.cardHowToCreateTask.setOnClickListener(v -> {
            bundle.putString("task", "HowToCreateTask");
            navController.navigate(R.id.actionNavHelpToFragmentDataHelp, bundle);
        });

        binding.cardHowToEditTask.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToEditTask");
            navController.navigate(R.id.actionNavHelpToFragmentDataHelp, bundle);
        });

        binding.cardHowToDeleteTask.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToDeleteTask");
            navController.navigate(R.id.actionNavHelpToFragmentDataHelp, bundle);
        });

        binding.cardHowToAddUser.setOnClickListener(v -> {
            bundle.putString("task", "cardHowToAddUser");
            navController.navigate(R.id.actionNavHelpToFragmentDataHelp, bundle);
        });

        binding.cardHelpTasks.setOnClickListener(v -> {
            bundle.putString("task", "fullHelpTasks");
            navController.navigate(R.id.actionNavHelpToFragmentDataHelp, bundle);
        });

        binding.cardHelpUsers.setOnClickListener(v -> {
            bundle.putString("task", "fullHelpUsers");
            navController.navigate(R.id.actionNavHelpToFragmentDataHelp, bundle);
        });

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1461749280684-dccba630e2f6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1169&q=80")
                .centerCrop()
                .into(binding.imageHelpApp);

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

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1579389083046-e3df9c2b3325?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80")
                .centerCrop()
                .into(binding.imageNewUser);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1551434678-e076c223a692?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80")
                .centerCrop()
                .into(binding.imageHelpTasks);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1516321318423-f06f85e504b3?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80")
                .centerCrop()
                .into(binding.imageHelpUsers);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
