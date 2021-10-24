package com.george.vector.users.user.main.fragments;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USERS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.common.edit_users.EditDataUserActivity;
import com.george.vector.common.settings.SettingsActivity;
import com.george.vector.users.user.main.SettingsUserBottomSheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentProfile extends Fragment {

    TextView text_view_name_ava, text_view_full_name, text_view_email;
    Button edit_data_user_btn, settings_user_btn;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userID, name, last_name, patronymic, email, role;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        text_view_name_ava = view.findViewById(R.id.text_view_name_ava_fragment_user);
        text_view_full_name = view.findViewById(R.id.text_view_full_name_fragment_user);
        text_view_email = view.findViewById(R.id.text_view_email_fragment_user);
        edit_data_user_btn = view.findViewById(R.id.edit_data_user_btn);
        settings_user_btn = view.findViewById(R.id.settings_user_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebaseFirestore.collection(USERS).document(userID);
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
            String ava = String.format("%s%s", _last_name, _name);

            text_view_full_name.setText(full_name);
            text_view_name_ava.setText(ava);
            text_view_email.setText(email);
        });

        edit_data_user_btn.setOnClickListener(v -> startActivity(new Intent(FragmentProfile.this.getContext(), EditDataUserActivity.class)));

        settings_user_btn.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentProfile.this.getContext(), SettingsActivity.class);
            intent.putExtra(PERMISSION, "user");
            intent.putExtra(EMAIL, "null");
            startActivity(intent);
        });


        return view;
    }
}
