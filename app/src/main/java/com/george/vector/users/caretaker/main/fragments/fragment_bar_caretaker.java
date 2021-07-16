package com.george.vector.users.caretaker.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.users.caretaker.main.FolderTaskCaretakerActivity;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class fragment_bar_caretaker extends Fragment {

    MaterialCardView bar_school_caretaker;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_caretaker, container, false);

        bar_school_caretaker = view.findViewById(R.id.bar_school_caretaker);

        bar_school_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_bar_caretaker.this.getContext(), FolderTaskCaretakerActivity.class);
            intent.putExtra("location", "bar_school");
            startActivity(intent);
        });

        return view;
    }
}
