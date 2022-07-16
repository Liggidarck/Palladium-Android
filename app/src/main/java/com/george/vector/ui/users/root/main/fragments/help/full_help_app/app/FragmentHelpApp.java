package com.george.vector.ui.users.root.main.fragments.help.full_help_app.app;

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
import com.george.vector.databinding.FragmentHelpAppBinding;
import com.george.vector.ui.users.root.main.fragments.help.FragmentHelp;

public class FragmentHelpApp extends Fragment {

    FragmentHelpAppBinding appBinding;
    Fragment data = new FragmentFullHelpData();
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        appBinding = FragmentHelpAppBinding.inflate(inflater, container, false);
        View view = appBinding.getRoot();


        appBinding.toolbarHelpApp.setNavigationOnClickListener(v -> {
            Fragment fragmentHelp = new FragmentHelp();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, fragmentHelp).commit();
        });

        appBinding.cardDownloadTextHelp.setOnClickListener(v -> {
            String url = "https://docs.google.com/document/d/1jP6xACZYv3jYBoPE7f2TmiBpjqu8xRNC/edit?usp=sharing&ouid=107837366117826648347&rtpof=true&sd=true";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        appBinding.cardFullHelpUsers.setOnClickListener(v -> {
            bundle.putString("user", "user");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        appBinding.cardFullHelpSuperusers.setOnClickListener(v -> {
            bundle.putString("user", "root");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });

        appBinding.cardFullHelpExecutor.setOnClickListener(v -> {
            bundle.putString("user", "executor");
            data.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, data).commit();
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBinding = null;
    }
}
