package com.george.vector.users.root.main.fragments.home;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTED;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "FragmentOst";
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
        String email = args.getString(EMAIL);

        Log.d(TAG, "email: " + email);

        ost_school_root.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentOst.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EXECUTED, "root");
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }
}
