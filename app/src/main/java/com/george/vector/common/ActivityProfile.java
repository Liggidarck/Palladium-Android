package com.george.vector.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.george.vector.R;
import com.george.vector.auth.ActivityLogin;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ActivityProfile extends AppCompatActivity {

    Button logout_btn;
    MaterialToolbar toolbar_profile;

    TextView role_text_view, name_text_view;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout_btn = findViewById(R.id.logout_btn);
        toolbar_profile = findViewById(R.id.toolbar_profile);
        role_text_view = findViewById(R.id.role_text_view);
        name_text_view = findViewById(R.id.name_text_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        toolbar_profile.setNavigationOnClickListener(v -> onBackPressed());

        logout_btn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, ActivityLogin.class));
            finish();
        });

    }
}