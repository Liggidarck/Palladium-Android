package com.george.vector.common.bottom_sheets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.george.vector.auth.ActivityLogin;
import com.george.vector.auth.ActivityRegisterUser;
import com.george.vector.common.edit_users.ListUsersActivity;
import com.george.vector.common.settings.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsoleBottomSheet extends BottomSheetDialogFragment {

    ImageView close_btn;
    RelativeLayout layout_new_person, layout_edit_person;
    Button btn_logout, btn_settings;

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
        btn_settings = view.findViewById(R.id.btn_settings);

        assert this.getArguments() != null;
        String permission = this.getArguments().getString(getString(R.string.permission));
        Log.d("ConsoleBottomSheet", String.format("permission: %s", permission));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        close_btn.setOnClickListener(v -> dismiss());

        layout_new_person.setOnClickListener(v -> startActivity(new Intent(ConsoleBottomSheet.this.getActivity(), ActivityRegisterUser.class)));
        layout_edit_person.setOnClickListener(v -> startActivity(new Intent(ConsoleBottomSheet.this.getContext(), ListUsersActivity.class)));

        btn_settings.setOnClickListener(v -> {
            Intent intent = new Intent(ConsoleBottomSheet.this.getContext(), SettingsActivity.class);

            if (permission.equals("root")) {
                intent.putExtra(getString(R.string.permission), "root");
                intent.putExtra(getString(R.string.email), "null");
                startActivity(intent);
            }

            if (permission.equals("admin")) {
                intent.putExtra(getString(R.string.permission), "admin");
                intent.putExtra(getString(R.string.email), "null");
                startActivity(intent);
            }

            if (permission.equals("caretaker")) {
                intent.putExtra(getString(R.string.permission), "caretaker");
                intent.putExtra(getString(R.string.email), "null");
                startActivity(intent);
            }

        });

        btn_logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ConsoleBottomSheet.this.getContext(), ActivityLogin.class));
        });
        return view;
    }
}