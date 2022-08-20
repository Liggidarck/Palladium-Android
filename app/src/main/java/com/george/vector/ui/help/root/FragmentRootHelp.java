package com.george.vector.ui.help.root;

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
import androidx.navigation.fragment.NavHostFragment;

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
