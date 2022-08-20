package com.george.vector.ui.help.root.app;

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
import com.george.vector.databinding.FragmentHelpAppBinding;

public class FragmentHelpApp extends Fragment {

    private FragmentHelpAppBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHelpAppBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle bundle = new Bundle();

        NavController navController =
                NavHostFragment.findNavController(this);

        binding.toolbarHelpApp.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        binding.cardDownloadTextHelp.setOnClickListener(v -> {
            String url = getString(R.string.url_download_text_help);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        binding.cardFullHelpUsers.setOnClickListener(v -> {
            bundle.putString("user", "user");
            navController.navigate(R.id.actionFragmentHelpAppToFragmentHelpTextApp, bundle);
        });

        binding.cardFullHelpRoot.setOnClickListener(v -> {
            bundle.putString("user", "root");
            navController.navigate(R.id.actionFragmentHelpAppToFragmentHelpTextApp, bundle);
        });

        binding.cardFullHelpExecutor.setOnClickListener(v -> {
            bundle.putString("user", "executor");
            navController.navigate(R.id.actionFragmentHelpAppToFragmentHelpTextApp, bundle);
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
