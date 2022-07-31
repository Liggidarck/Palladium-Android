package com.george.vector.ui.users.root.main.fragments.my_tasks;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.EXECUTOR_EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentOstRootBinding;
import com.george.vector.ui.users.root.folders.LocationFolderActivity;

public class FragmentRootMyTasksOst extends Fragment {

    FragmentOstRootBinding ostRootBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ostRootBinding = FragmentOstRootBinding.inflate(inflater, container, false);
        View view = ostRootBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(EMAIL);

        ostRootBinding.ostSchoolRoot.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentRootMyTasksOst.this.getContext(), LocationFolderActivity.class);
            intent.putExtra(LOCATION, OST_SCHOOL);
            intent.putExtra(EMAIL, email);
            intent.putExtra(EXECUTOR_EMAIL, "work");
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ostRootBinding = null;
    }
}