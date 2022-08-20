package com.george.vector.ui.users.root.main.fragments.home;

import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_CAMERA_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.FragmentBarBinding;
import com.george.vector.ui.users.root.folders.LocationFolderActivity;

public class FragmentRootBar extends Fragment {

    FragmentBarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.barSchool.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootBar.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(COLLECTION, BAR_SCHOOL);
            intent.putExtra(EXECUTOR_EMAIL, "root");
            startActivity(intent);
        });

        binding.barRucheek.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootBar.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(COLLECTION, BAR_RUCHEEK);
            intent.putExtra(EXECUTOR_EMAIL, "root");
            startActivity(intent);
        });

        binding.barZvezdochka.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootBar.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(COLLECTION, BAR_ZVEZDOCHKA);
            intent.putExtra(EXECUTOR_EMAIL, "root");
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
