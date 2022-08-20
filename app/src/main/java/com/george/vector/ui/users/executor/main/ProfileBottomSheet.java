package com.george.vector.ui.users.executor.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.databinding.ProfileBottomSheetBinding;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    FirebaseAuth firebaseAuth;

    ProfileBottomSheetBinding sheetBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sheetBinding = ProfileBottomSheetBinding.inflate(inflater, container, false);
        View view = sheetBinding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        sheetBinding.closeBtn.setOnClickListener(v -> dismiss());

        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        userViewModel.getUser(userId).observe(this, user -> {
            String name = user.getName();
            String lastName = user.getLast_name();
            String patronymic = user.getPatronymic();
            String email = user.getEmail();
            String role = user.getRole();

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
