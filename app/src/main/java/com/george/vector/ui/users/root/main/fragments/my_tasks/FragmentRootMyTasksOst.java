package com.george.vector.ui.users.root.main.fragments.my_tasks;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.OST_AIST;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_YAGODKA;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentOstBinding;
import com.george.vector.ui.users.root.tasks.navigation.StatusActivity;

public class FragmentRootMyTasksOst extends Fragment {

    FragmentOstBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOstBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.ostSchool.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootMyTasksOst.this.getContext(), StatusActivity.class);
            intent.putExtra(ZONE, OST_SCHOOL);
            intent.putExtra(IS_EXECUTE, true);
            startActivity(intent);
        });

        binding.ostAist.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootMyTasksOst.this.getContext(), StatusActivity.class);
            intent.putExtra(ZONE, OST_AIST);
            intent.putExtra(IS_EXECUTE, true);
            startActivity(intent);
        });

        binding.ostYagodka.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootMyTasksOst.this.getContext(), StatusActivity.class);
            intent.putExtra(ZONE, OST_YAGODKA);
            intent.putExtra(IS_EXECUTE, true);
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
