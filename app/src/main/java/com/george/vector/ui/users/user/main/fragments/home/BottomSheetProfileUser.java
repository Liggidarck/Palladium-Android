package com.george.vector.ui.users.user.main.fragments.home;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.ui.settings.SettingsActivity;
import com.george.vector.databinding.ProfileUserBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetProfileUser extends BottomSheetDialogFragment {

    ProfileUserBottomSheetBinding binding;

    StateListener listener;

    SharedPreferences mDataUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileUserBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mDataUser = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        String name_user = mDataUser.getString(USER_PREFERENCES_NAME, "");
        String last_name_user = mDataUser.getString(USER_PREFERENCES_LAST_NAME, "");
        String email = mDataUser.getString(USER_PREFERENCES_EMAIL, "");

        String _name = Character.toString(name_user.charAt(0));
        String _last_name = Character.toString(last_name_user.charAt(0));

        binding.textNameUser.setText(String.format("%s %s", name_user, last_name_user));
        binding.textEmailUser.setText(email);
        binding.textViewNameAva.setText(String.format("%s%s", _name, _last_name));

        binding.settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(BottomSheetProfileUser.this.getContext(), SettingsActivity.class);
            intent.putExtra(PERMISSION, "user");
            intent.putExtra(EMAIL, "null");
            startActivity(intent);
        });

        binding.logoutBtnUser.setOnClickListener(v -> listener.getButton("logoutBtnUser"));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface StateListener {
        void getButton(String button);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetProfileUser.StateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

}
