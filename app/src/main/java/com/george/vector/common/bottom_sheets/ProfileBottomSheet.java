package com.george.vector.common.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    ImageView close_btn;
    TextView text_view_full_name, text_view_email, text_view_role, text_view_name_ava;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userID, name, last_name, patronymic, email, role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_bottom_sheet, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        text_view_full_name = view.findViewById(R.id.text_view_full_name);
        text_view_email = view.findViewById(R.id.text_view_email);
        text_view_role = view.findViewById(R.id.text_view_role);
        text_view_name_ava = view.findViewById(R.id.text_view_name_ava);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        close_btn.setOnClickListener(v -> dismiss());

        // TODO: Если вызвать ProfileBottomSheet, а затем выйти из аккаунта приложение крашнеться.
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            name = value.getString("name");
            last_name = value.getString("last_name");
            patronymic = value.getString("patronymic");
            email = value.getString("email");
            role = value.getString("role");

            String full_name = String.format("%s %s %s", last_name, name, patronymic);
            String _name = Character.toString(name.charAt(0));
            String _last_name = Character.toString(last_name.charAt(0));
            String ava = String.format("%s%s", _name, _last_name);

            text_view_full_name.setText(full_name);
            text_view_name_ava.setText(ava);
            text_view_email.setText(email);
            text_view_role.setText(role);
        });

        return view;
    }
}
