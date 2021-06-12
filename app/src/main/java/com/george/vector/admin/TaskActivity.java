package com.george.vector.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.george.vector.R;
import com.george.vector.admin.tasks.EditTaskAdminActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_tasks_admin;
    TextView text_view_address_task_admin, text_view_floor_task_admin, text_view_cabinet_task_admin,
            text_view_name_task_admin, text_view_comment_task_admin, text_view_status_task_admin,
            text_view_date_create_task_admin;
    Button edit_task_btn;
    CircleImageView circle_status;

    private static final String TAG = "TaskActivity";

    String id, address, floor, cabinet, name_task, comment, status, date_create, time_create;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        topAppBar_tasks_admin = findViewById(R.id.topAppBar_tasks_admin);
        text_view_address_task_admin = findViewById(R.id.text_view_address_task_admin);
        text_view_floor_task_admin = findViewById(R.id.text_view_floor_task_admin);
        text_view_cabinet_task_admin = findViewById(R.id.text_view_cabinet_task_admin);
        text_view_name_task_admin = findViewById(R.id.text_view_name_task_admin);
        text_view_comment_task_admin = findViewById(R.id.text_view_comment_task_admin);
        text_view_status_task_admin = findViewById(R.id.text_view_status_task_admin);
        text_view_date_create_task_admin = findViewById(R.id.text_view_date_create_task_admin);
        edit_task_btn = findViewById(R.id.edit_task_btn);
        circle_status = findViewById(R.id.circle_status);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task").toString();
        Log.i(TAG, "id: " + id);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        topAppBar_tasks_admin.setNavigationOnClickListener(v -> onBackPressed());

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

            text_view_address_task_admin.setText(address);
            text_view_floor_task_admin.setText("Этаж - " +  floor);
            text_view_cabinet_task_admin.setText("Кабинет - " + cabinet);
            text_view_name_task_admin.setText(name_task);
            text_view_comment_task_admin.setText(comment);
            text_view_status_task_admin.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_admin.setText(date_create_text);

            if(status.equals("Новая заявка"))
                circle_status.setImageResource(R.color.red);

            if(status.equals("В работе"))
                circle_status.setImageResource(R.color.orange);

            if(status.equals("Архив"))
                circle_status.setImageResource(R.color.green);

        });

        edit_task_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskAdminActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

    }
}