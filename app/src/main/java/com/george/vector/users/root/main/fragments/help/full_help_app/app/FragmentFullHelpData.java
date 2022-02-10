package com.george.vector.users.root.main.fragments.help.full_help_app.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentFullHelpDataBinding;

public class FragmentFullHelpData extends Fragment {

    FragmentFullHelpDataBinding binding;
    Fragment selectedFragment = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFullHelpDataBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String user = args.getString("user");

        binding.toolbarFullHelpDataApp.setNavigationOnClickListener(v -> {
            Fragment helpApp = new FragmentHelpApp();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, helpApp).commit();
        });

        switch (user) {
            case "user":
                selectedFragment = new FragmentTextHelpUser();
                binding.toolbarFullHelpDataApp.setTitle("Пользователи");
                break;
            case "root":
                selectedFragment = new FragmentTextRoot();
                binding.toolbarFullHelpDataApp.setTitle("Суперпользователи");
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_full_help_data, selectedFragment).commit();


        return view;
    }
}
