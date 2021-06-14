package com.george.vector.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.george.vector.admin.edit_users.ListUsersActivity;
import com.george.vector.auth.ActivityLogin;
import com.george.vector.auth.ActivityRegisterUser;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminBottomSheet extends BottomSheetDialogFragment {

    ImageView close_btn;
    RelativeLayout layout_new_person, layout_edit_person;
    Button btn_logout;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_bottom_sheet, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        layout_new_person = view.findViewById(R.id.layout_new_person);
        btn_logout = view.findViewById(R.id.btn_logout);
        layout_edit_person = view.findViewById(R.id.layout_edit_person);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        close_btn.setOnClickListener(v -> dismiss());
        layout_new_person.setOnClickListener(v -> startActivity(new Intent(AdminBottomSheet.this.getActivity(), ActivityRegisterUser.class)));
        btn_logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminBottomSheet.this.getContext(), ActivityLogin.class));
        });
        layout_edit_person.setOnClickListener(v -> startActivity(new Intent(AdminBottomSheet.this.getContext(), ListUsersActivity.class)));

        return view;
    }
}