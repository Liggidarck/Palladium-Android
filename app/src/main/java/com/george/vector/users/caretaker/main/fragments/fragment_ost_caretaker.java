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

public class fragment_ost_caretaker extends Fragment {

    MaterialCardView ost_school_caretaker, ost_kids_one_caretaker, ost_kids_two_caretaker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ost_caretaker, container, false);

        ost_school_caretaker = view.findViewById(R.id.ost_school_caretaker);
        ost_kids_one_caretaker = view.findViewById(R.id.ost_kids_one_caretaker);
        ost_kids_two_caretaker = view.findViewById(R.id.ost_kids_two_caretaker);

        ost_school_caretaker.setOnClickListener(v -> {
            Intent intent = new Intent(fragment_ost_caretaker.this.getContext(), FolderTaskCaretakerActivity.class);
            intent.putExtra((String) getText(R.string.location), getText(R.string.ost_school));
            startActivity(intent);
        });

//        ost_kids_one_caretaker.setOnClickListener(v -> {
//            Intent intent = new Intent(fragment_ost_caretaker.this.getContext(), FolderTaskCaretakerActivity.class);
//            intent.putExtra("location", "ost_aist");
//            startActivity(intent);
//        });
//
//        ost_kids_two_caretaker.setOnClickListener(v -> {
//            Intent intent = new Intent(fragment_ost_caretaker.this.getContext(), FolderTaskCaretakerActivity.class);
//            intent.putExtra("location", "ost_yagodka");
//            startActivity(intent);
//        });

        return view;
    }
}
