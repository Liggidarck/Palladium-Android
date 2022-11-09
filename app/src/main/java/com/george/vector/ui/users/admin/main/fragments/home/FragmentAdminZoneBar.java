package com.george.vector.ui.users.admin.main.fragments.home;

import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentBarBinding;
import com.george.vector.ui.users.admin.tasks.navigation.StatusActivity;

public class FragmentAdminZoneBar extends Fragment {

    FragmentBarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.barSchool.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentAdminZoneBar.this.getContext(), StatusActivity.class);
            intent.putExtra(ZONE, BAR_SCHOOL);
            intent.putExtra(IS_EXECUTE, "root");
            startActivity(intent);
        });

        binding.barRucheek.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentAdminZoneBar.this.getContext(), StatusActivity.class);
            intent.putExtra(ZONE, BAR_RUCHEEK);
            intent.putExtra(IS_EXECUTE, "root");
            startActivity(intent);
        });

        binding.barZvezdochka.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentAdminZoneBar.this.getContext(), StatusActivity.class);
            intent.putExtra(ZONE, BAR_ZVEZDOCHKA);
            intent.putExtra(IS_EXECUTE, "root");
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
