package com.george.vector.ui.users.user.main.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ProfileUserBottomSheetBinding;
import com.george.vector.ui.settings.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetProfileUser extends BottomSheetDialogFragment {

    ProfileUserBottomSheetBinding binding;

    stateBtnListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileUserBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        String nameUser = userPrefViewModel.getUser().getName();
        String lastNameUser = userPrefViewModel.getUser().getLast_name();
        String email = userPrefViewModel.getUser().getEmail();

        String charName = Character.toString(nameUser.charAt(0));
        String charLastname = Character.toString(lastNameUser.charAt(0));

        binding.textNameUser.setText(String.format("%s %s", nameUser, lastNameUser));
        binding.textEmailUser.setText(email);
        binding.textViewNameAva.setText(String.format("%s%s", charName, charLastname));

        binding.settingsBtn.setOnClickListener(v ->
                startActivity(new Intent(BottomSheetProfileUser.this.getContext(), SettingsActivity.class))
        );

        binding.logoutBtnUser.setOnClickListener(v -> listener.getButton("logoutBtnUser"));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface stateBtnListener {
        void getButton(String button);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (stateBtnListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement BottomSheetListener");
        }
    }

}
