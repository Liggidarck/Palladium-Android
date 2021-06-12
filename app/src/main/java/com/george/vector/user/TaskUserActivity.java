package com.george.vector.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class TaskUserActivity extends AppCompatActivity {

    private static final String TAG = "TaskUserActivity";

    MaterialToolbar toolbar;
    TextView text_view_address_task_user, text_view_floor_task_user, text_view_cabinet_task_user,
            text_view_name_task_user, text_view_comment_task_user, text_view_status_task_user,
            text_view_date_create_task_user;

    String id, address, floor, cabinet, name_task, comment, status, date_create, time_create;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_user);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task").toString();

        toolbar = findViewById(R.id.topAppBar_task_user);
        text_view_address_task_user = findViewById(R.id.text_view_address_task_user);
        text_view_floor_task_user = findViewById(R.id.text_view_floor_task_user);
        text_view_cabinet_task_user = findViewById(R.id.text_view_cabinet_task_user);
        text_view_name_task_user = findViewById(R.id.text_view_name_task_user);
        text_view_comment_task_user = findViewById(R.id.text_view_comment_task_user);
        text_view_status_task_user = findViewById(R.id.text_view_status_task_user);
        text_view_date_create_task_user = findViewById(R.id.text_view_date_create_task_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

         setSupportActionBar(toolbar);
         toolbar.setNavigationOnClickListener(v -> onBackPressed());

         Log.i(TAG, "id: " + id);

        DocumentReference documentReference = firebaseFirestore.collection("new tasks").document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("description");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            name_task = value.getString("title");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("priority");
            time_create = value.getString("time_priority");

            text_view_address_task_user.setText(address);
            text_view_floor_task_user.setText("Этаж - " +  floor);
            text_view_cabinet_task_user.setText("Кабинет - " + cabinet);
            text_view_name_task_user.setText(name_task);
            text_view_comment_task_user.setText(comment);
            text_view_status_task_user.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_user.setText(date_create_text);
        });

    }
}