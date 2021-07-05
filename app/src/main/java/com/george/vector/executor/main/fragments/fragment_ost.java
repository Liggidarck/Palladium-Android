package com.george.vector.executor.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.executor.main.FolderExecutorActivity;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class fragment_ost extends Fragment {

    private static final String TAG = "fragment_ost";
    MaterialCardView ost_school_executor;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ost_executor, container, false);

        ost_school_executor = view.findViewById(R.id.ost_school_executor);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString("email");

        ost_school_executor.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_ost.this.getContext(), FolderExecutorActivity.class);
            intent.putExtra("location", "ost_school");
            intent.putExtra("email", email);
            startActivity(intent);
        });

        return view;
    }
}
