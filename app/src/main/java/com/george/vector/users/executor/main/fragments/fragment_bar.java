package com.george.vector.users.executor.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.users.executor.main.FolderExecutorActivity;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class fragment_bar extends Fragment {

    MaterialCardView bar_school_executor;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_executor, container, false);

        bar_school_executor = view.findViewById(R.id.bar_school_executor);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(getString(R.string.email));

        bar_school_executor.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_bar.this.getContext(), FolderExecutorActivity.class);
            intent.putExtra(getString(R.string.location), getString(R.string.bar_school));
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        return view;
    }
}
