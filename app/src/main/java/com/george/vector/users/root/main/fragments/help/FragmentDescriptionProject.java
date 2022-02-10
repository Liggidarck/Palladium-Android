package com.george.vector.users.root.main.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentDescriptionProjectBinding;

public class FragmentDescriptionProject extends Fragment {

    FragmentDescriptionProjectBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDescriptionProjectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.toolbarDescriptionProject.setNavigationOnClickListener(v -> {
            Fragment about = new FragmentAboutProject();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, about).commit();
        });

        binding.descriptionTitle.setText(getString(R.string.description_project));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
