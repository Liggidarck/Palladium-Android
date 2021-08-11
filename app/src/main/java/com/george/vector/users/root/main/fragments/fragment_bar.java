package com.george.vector.users.root.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.users.root.main.LocationFolderActivity;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class fragment_bar extends Fragment {

    MaterialCardView bar_school_root, bar_kids_one_root, bar_kids_two_root;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_root, container, false);

        bar_school_root = view.findViewById(R.id.bar_school_root);
        bar_kids_one_root = view.findViewById(R.id.bar_kids_one_root);
        bar_kids_two_root = view.findViewById(R.id.bar_kids_two_root);

        bar_school_root.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_bar.this.getContext(), LocationFolderActivity.class);
            intent.putExtra("location", "bar_school");
            startActivity(intent);
        });

        return view;
    }
}
