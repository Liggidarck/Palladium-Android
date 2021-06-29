package com.george.vector.сaretaker.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.сaretaker.main.FolderTaskCaretakerActivity;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class fragment_ost_caretaker extends Fragment {

    MaterialCardView ost_school_caretaker, ost_kids_one_caretaker, ost_kids_two_caretaker;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ost_caretaker, container, false);

        ost_school_caretaker = view.findViewById(R.id.ost_school_caretaker);
        ost_kids_one_caretaker = view.findViewById(R.id.ost_kids_one_caretaker);
        ost_kids_two_caretaker = view.findViewById(R.id.ost_kids_two_caretaker);

        ost_school_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_ost_caretaker.this.getContext(), FolderTaskCaretakerActivity.class);
            intent.putExtra("location", "ost_school");
            startActivity(intent);
        });

        return view;
    }
}
