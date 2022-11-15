package com.george.vector.ui.users.admin.main.fragments.statistic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentStatisticBinding;

public class FragmentStatistic extends Fragment {

    private FragmentStatisticBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater, container, false);

        updateZone("ost");

        binding.chipRootOst.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                updateZone("ost");
            }
        });

        binding.chipRootBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                updateZone("bar");
            }
        });

        return binding.getRoot();
    }

    private void updateZone(String zone) {
        Fragment currentFragment = null;
        switch (zone) {
            case "ost":
                currentFragment = new FragmentStatisticOst();

                break;
            case "bar":
                currentFragment = new FragmentStatisticBar();

                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.statistic_frame_root, currentFragment).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
