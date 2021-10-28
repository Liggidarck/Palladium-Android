package com.george.vector.users.user.main.fragments;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USERS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.common.edit_users.EditDataUserActivity;
import com.george.vector.common.settings.SettingsActivity;
import com.george.vector.databinding.FragmentRootProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentProfile extends Fragment {
    FirebaseAuth firebase_auth;
    FirebaseFirestore firebase_firestore;

    String user_id, name, last_name, patronymic, email, role;

    FragmentRootProfileBinding profileBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileBinding = FragmentRootProfileBinding.inflate(inflater, container, false);
        View view = profileBinding.getRoot();

        firebase_auth = FirebaseAuth.getInstance();
        firebase_firestore = FirebaseFirestore.getInstance();

        user_id = Objects.requireNonNull(firebase_auth.getCurrentUser()).getUid();
        DocumentReference documentReference = firebase_firestore.collection(USERS).document(user_id);
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

            profileBinding.textViewFullNameFragment.setText(full_name);
            profileBinding.textViewNameAvaFragment.setText(ava);
            profileBinding.textViewEmailFragment.setText(email);
        });

        profileBinding.layoutEditPersonProfile.setOnClickListener(v -> startActivity(new Intent(FragmentProfile.this.getContext(), EditDataUserActivity.class)));

        profileBinding.settingsProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentProfile.this.getContext(), SettingsActivity.class);
            intent.putExtra(PERMISSION, "user");
            intent.putExtra(EMAIL, "null");
            startActivity(intent);
        });


        return view;
    }
}
