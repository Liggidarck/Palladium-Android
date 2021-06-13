package com.george.vector.common;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "ProfileBottomSheet";
    ImageView close_btn;
    TextView text_view_name, text_view_last_name,
            text_view_patronymic, text_view_email, text_view_role;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userID, name, last_name, patronymic, email, role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_bottom_sheet, container, false);

        close_btn = view.findViewById(R.id.close_btn);
        text_view_name = view.findViewById(R.id.text_view_name);
        text_view_last_name = view.findViewById(R.id.text_view_last_name);
        text_view_patronymic = view.findViewById(R.id.text_view_patronymic);
        text_view_email = view.findViewById(R.id.text_view_email);
        text_view_role = view.findViewById(R.id.text_view_role);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        close_btn.setOnClickListener(v -> dismiss());

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            name = "Имя: " + value.getString("name");
            last_name = "Фамилия: " + value.getString("last_name");
            patronymic = "Отчество: " + value.getString("patronymic");
            email = "Email: " + value.getString("email");
            role = "Должность: " + value.getString("role");
            Log.i(TAG, "data: " + name + " " + last_name + " " + patronymic + " " + email + " " + role);

            text_view_name.setText(name);
            text_view_last_name.setText(last_name);
            text_view_patronymic.setText(patronymic);
            text_view_email.setText(email);
            text_view_role.setText(role);
        });

        return view;
    }
}
