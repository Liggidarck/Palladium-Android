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

import com.george.vector.databinding.FragmentAboutProjectBinding;

public class FragmentAboutProject extends Fragment {

    FragmentAboutProjectBinding aboutProject;
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        aboutProject = FragmentAboutProjectBinding.inflate(inflater, container, false);
        View view = aboutProject.getRoot();

        aboutProject.cardPrivacyPolicy.setOnClickListener(v -> {
            String url = "https://drive.google.com/file/d/1g0l1gDULwpHrEWLNo2-d8QQE0LvydXpR/view?usp=sharing";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        aboutProject.cardLicence.setOnClickListener(v -> {
            String url = "https://drive.google.com/file/d/1d1T4Rvr3TxvFO-CxPrmSP0Satquk6vvd/view?usp=sharing";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        aboutProject = null;
    }
}