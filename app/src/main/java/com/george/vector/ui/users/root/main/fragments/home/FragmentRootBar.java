package com.george.vector.ui.users.root.main.fragments.home;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.utils.consts.Keys.LOCATION;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_EMAIL;

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
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.FragmentBarRootBinding;
import com.george.vector.ui.users.root.folders.LocationFolderActivity;

public class FragmentRootBar extends Fragment {

    FragmentBarRootBinding barRootBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        barRootBinding = FragmentBarRootBinding.inflate(inflater, container, false);
        View view = barRootBinding.getRoot();

        UserPreferencesViewModel userPrefViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);
        String email = userPrefViewModel.getUser().getEmail();

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
