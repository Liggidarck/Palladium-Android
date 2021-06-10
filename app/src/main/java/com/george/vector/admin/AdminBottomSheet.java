package com.george.vector.admin;

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
import com.george.vector.auth.ActivityRegisterUser;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminBottomSheet extends BottomSheetDialogFragment {

    ImageView close_btn;
    RelativeLayout layout_new_person;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_bottom_sheet, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        layout_new_person = view.findViewById(R.id.layout_new_person);

        close_btn.setOnClickListener(v -> dismiss());

        layout_new_person.setOnClickListener(v -> startActivity(new Intent(AdminBottomSheet.this.getActivity(), ActivityRegisterUser.class)));

        return view;
    }
}