package com.george.vector.users.root.main.fragments;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.USERS;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.develop.DevelopJavaActivity;
import com.george.vector.develop.DevelopKotlinActivity;
import com.george.vector.users.root.edit_users.RegisterUserActivity;
import com.george.vector.users.root.edit_users.ListUsersActivity;
import com.george.vector.common.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {

    TextView text_view_name_ava, text_view_full_name, text_view_email;
    Button btn_settings_profile_root;
    RelativeLayout layout_new_person_profile, layout_edit_person_profile;

    FirebaseAuth firebase_auth;
    FirebaseFirestore firebase_firestore;

    String user_id, name, last_name, patronymic, email, role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_profile, container, false);

        text_view_name_ava = view.findViewById(R.id.text_view_name_ava_fragment);
        text_view_full_name = view.findViewById(R.id.text_view_full_name_fragment);
        text_view_email = view.findViewById(R.id.text_view_email_fragment);
        btn_settings_profile_root =view.findViewById(R.id.btn_settings_profile_root);
        layout_new_person_profile = view.findViewById(R.id.layout_new_person_profile);
        layout_edit_person_profile = view.findViewById(R.id.layout_edit_person_profile);

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
            String ava = String.format("%s%s", _name, _last_name);

            text_view_full_name.setText(full_name);
            text_view_name_ava.setText(ava);
            text_view_email.setText(email);
        });

        layout_new_person_profile.setOnClickListener(v -> startActivity(new Intent(FragmentProfile.this.getActivity(), RegisterUserActivity.class)));
        layout_edit_person_profile.setOnClickListener(v -> startActivity(new Intent(FragmentProfile.this.getContext(), ListUsersActivity.class)));

        btn_settings_profile_root.setOnClickListener(v -> {
            Intent intent = new Intent(FragmentProfile.this.getContext(), SettingsActivity.class);
            intent.putExtra(PERMISSION, "root");
            intent.putExtra(EMAIL, "null");
            startActivity(intent);
        });

        CircleImageView develop_activity = view.findViewById(R.id.develop_activity);
        develop_activity.setOnClickListener(v -> startActivity(new Intent(FragmentProfile.this.getContext(), DevelopJavaActivity.class)));

        return view;
    }
}
