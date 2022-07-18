package com.george.vector.ui.users.root.main.fragments.home;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentBarRootBinding;
import com.george.vector.ui.users.root.folders.LocationFolderActivity;

public class FragmentRootBar extends Fragment {

    FragmentBarRootBinding barRootBinding;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        barRootBinding = FragmentBarRootBinding.inflate(inflater, container, false);
        View view = barRootBinding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");

        barRootBinding.barSchoolRoot.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootBar.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(LOCATION, BAR_SCHOOL);
            intent.putExtra(EXECUTOR_EMAIL, "root");
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        barRootBinding = null;
    }
}
