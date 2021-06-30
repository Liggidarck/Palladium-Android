package com.george.vector.user.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.george.vector.auth.ActivityLogin;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsUserBottomSheet extends BottomSheetDialogFragment {

    Button btn_logout, edit_data_user;
    ImageView close_btn;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_user_bottom_sheet, container, false);

        btn_logout = view.findViewById(R.id.btn_logout);
        close_btn = view.findViewById(R.id.close_btn);
        edit_data_user = view.findViewById(R.id.edit_data_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        close_btn.setOnClickListener(v -> dismiss());
        btn_logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SettingsUserBottomSheet.this.getContext(), ActivityLogin.class));
        });
        edit_data_user.setOnClickListener(v -> startActivity(new Intent(SettingsUserBottomSheet.this.getContext(), EditDataUserActivity.class)));

        return view;
    }
}
