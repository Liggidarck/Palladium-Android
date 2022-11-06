package com.george.vector.ui.users.executor.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.databinding.ProfileBottomSheetBinding;
import com.george.vector.network.model.Role;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    private ProfileBottomSheetBinding sheetBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sheetBinding = ProfileBottomSheetBinding.inflate(inflater, container, false);
        View view = sheetBinding.getRoot();

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        sheetBinding.closeBtn.setOnClickListener(v -> dismiss());

        long id = 0;

        userViewModel.getUserById(id).observe(this, user -> {

            List<Role> roleList;

            String name = user.getName();
            String lastName = user.getLastName();
            String patronymic = user.getPatronymic();
            String email = user.getEmail();
            roleList = user.getRoles();
            String role = roleList.get(0).getName();

            String fullName = String.format("%s %s %s", lastName, name, patronymic);
            String charName = Character.toString(name.charAt(0));
            String charLastName = Character.toString(lastName.charAt(0));
            String ava = String.format("%s%s", charName, charLastName);

            sheetBinding.textViewFullName.setText(fullName);
            sheetBinding.textViewNameAva.setText(ava);
            sheetBinding.textViewEmail.setText(email);
            sheetBinding.textViewRole.setText(role);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sheetBinding = null;
    }

}
