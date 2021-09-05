package com.george.vector.users.root.main.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.users.root.folders.LocationFolderActivity;
import com.google.android.material.card.MaterialCardView;

public class FragmentOst extends Fragment {

    MaterialCardView ost_school_root, ost_kids_one_root, ost_kids_two_root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ost_root, container,false);

        ost_school_root = view.findViewById(R.id.ost_school_root);
        ost_kids_one_root = view.findViewById(R.id.ost_kids_one_root);
        ost_kids_two_root = view.findViewById(R.id.ost_kids_two_root);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(getString(R.string.email));

        ost_school_root.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentOst.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(getString(R.string.location), getString(R.string.ost_school));
            intent.putExtra("executed", "root");
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        return view;
    }
}
