package com.george.vector.users.root.main.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.google.android.material.card.MaterialCardView;

public class FragmentBar extends Fragment {

    private static final String TAG = "FragmentBar";
    MaterialCardView bar_school_root, bar_kids_one_root, bar_kids_two_root;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_root, container, false);

        bar_school_root = view.findViewById(R.id.bar_school_root);
        bar_kids_one_root = view.findViewById(R.id.bar_kids_one_root);
        bar_kids_two_root = view.findViewById(R.id.bar_kids_two_root);

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(getString(R.string.email));
        Log.d(TAG, "email: " + email);

        return view;
    }
}
