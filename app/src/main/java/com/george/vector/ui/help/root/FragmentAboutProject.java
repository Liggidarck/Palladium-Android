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

import com.bumptech.glide.Glide;
import com.george.vector.R;
import com.george.vector.databinding.FragmentAboutProjectBinding;

public class FragmentAboutProject extends Fragment {

    FragmentAboutProjectBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutProjectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.toolbarAboutProject.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        NavController navController =
                NavHostFragment.findNavController(this);

        binding.cardPrivacyPolicy.setOnClickListener(v -> {
            String url = "https://drive.google.com/file/d/1g0l1gDULwpHrEWLNo2-d8QQE0LvydXpR/view?usp=sharing";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        binding.cardLicence.setOnClickListener(v -> {
            String url = "https://drive.google.com/file/d/1d1T4Rvr3TxvFO-CxPrmSP0Satquk6vvd/view?usp=sharing";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        binding.cardDescriptionProject.setOnClickListener(v -> navController.navigate(R.id.actionFragmentAboutProjectToFragmentDescriptionProject));

        binding.cardDevelopApp.setOnClickListener(v -> navController.navigate(R.id.actionFragmentAboutProjectToFragmentDevelopApp));

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1562408590-e32931084e23?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80")
                .centerCrop()
                .into(binding.imageDevelopApp);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1526628953301-3e589a6a8b74?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1106&q=80")
                .centerCrop()
                .into(binding.mainImage);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}