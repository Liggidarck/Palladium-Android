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
import com.george.vector.databinding.FragmentDescriptionProjectBinding;

public class FragmentDescriptionProject extends Fragment {

    FragmentDescriptionProjectBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDescriptionProjectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String user = args.getString("user");

        binding.toolbarDescriptionProject.setNavigationOnClickListener(v -> {
            if (user.equals("user")) {
                Fragment about = new com.george.vector.ui.users.user.main.fragments.help.FragmentAboutProject();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_user, about).commit();
            }
            if (user.equals("root")) {
                Fragment about = new FragmentAboutProject();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, about).commit();
            }

        });

        binding.downloadFullText.setOnClickListener(v -> {
            String url = "https://docs.google.com/document/d/1YGy2YM9jFOcPkwCeRWDB2iy_AiDoriyR/edit?usp=sharing&ouid=107837366117826648347&rtpof=true&sd=true";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
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
