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

import com.george.vector.R;
import com.george.vector.databinding.FragmentDescriptionProjectBinding;

public class FragmentDescriptionProject extends Fragment {

    FragmentDescriptionProjectBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDescriptionProjectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.downloadFullText.setOnClickListener(v -> {
            String url = getString(R.string.url_full_document);
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
