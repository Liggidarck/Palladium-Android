package com.george.vector.ui.users.executor.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.databinding.ProfileBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userId, name, lastName, patronymic, email, role;

    ProfileBottomSheetBinding sheetBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sheetBinding = ProfileBottomSheetBinding.inflate(inflater, container, false);
        View view = sheetBinding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        sheetBinding.closeBtn.setOnClickListener(v -> dismiss());

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            name = value.getString("name");
            lastName = value.getString("last_name");
            patronymic = value.getString("patronymic");
            email = value.getString("email");
            role = value.getString("role");

            String fullName = String.format("%s %s %s", lastName, name, patronymic);
            String name = Character.toString(this.name.charAt(0));
            String lastName = Character.toString(this.lastName.charAt(0));
            String ava = String.format("%s%s", name, lastName);

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
