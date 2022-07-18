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

import com.george.vector.databinding.FragmentHelpAppBinding;

public class FragmentHelpApp extends Fragment {

    FragmentHelpAppBinding appBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        appBinding = FragmentHelpAppBinding.inflate(inflater, container, false);
        View view = appBinding.getRoot();

        appBinding.cardDownloadTextHelp.setOnClickListener(v -> {
            String url = "https://docs.google.com/document/d/1jP6xACZYv3jYBoPE7f2TmiBpjqu8xRNC/edit?usp=sharing&ouid=107837366117826648347&rtpof=true&sd=true";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBinding = null;
    }
}
