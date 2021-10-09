package com.george.vector.users.user.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.george.vector.common.edit_users.EditDataUserActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsUserBottomSheet extends BottomSheetDialogFragment {

    ImageView close_btn;
    RelativeLayout layout_edit_person_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_user_bottom_sheet, container, false);

        close_btn = view.findViewById(R.id.close_btn_user);
        layout_edit_person_user = view.findViewById(R.id.layout_edit_person_user);

        close_btn.setOnClickListener(v -> dismiss());

        layout_edit_person_user.setOnClickListener(v -> startActivity(new Intent(SettingsUserBottomSheet.this.getContext(), EditDataUserActivity.class)));

        return view;
    }
}