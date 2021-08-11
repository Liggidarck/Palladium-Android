package com.george.vector.users.root.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskRootActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_tasks_root;
    LinearProgressIndicator progress_bar_task_root;
    TextView text_view_address_task_root, text_view_floor_task_root, text_view_cabinet_task_root,
            text_view_name_task_root, text_view_comment_task_root, text_view_status_task_root,
            text_view_date_create_task_root;
    CircleImageView circle_status_root;
    Button edit_task_btn, delete_task_btn;

    private static final String TAG = "TaskActivityRoot";

    String id, collection, address, floor, cabinet, name_task, comment, status, date_create, time_create, location;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_root);

        topAppBar_tasks_root = findViewById(R.id.topAppBar_tasks_root);
        progress_bar_task_root = findViewById(R.id.progress_bar_task_root);
        text_view_address_task_root = findViewById(R.id.text_view_address_task_root);
        text_view_floor_task_root = findViewById(R.id.text_view_floor_task_root);
        text_view_cabinet_task_root = findViewById(R.id.text_view_cabinet_task_root);
        text_view_name_task_root = findViewById(R.id.text_view_name_task_root);
        text_view_comment_task_root = findViewById(R.id.text_view_comment_task_root);
        text_view_status_task_root = findViewById(R.id.text_view_status_task_root);
        text_view_date_create_task_root = findViewById(R.id.text_view_date_create_task_root);
        circle_status_root = findViewById(R.id.circle_status_root);
        edit_task_btn = findViewById(R.id.edit_task_btn);
        delete_task_btn = findViewById(R.id.delete_task_btn_root);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task_root").toString();
        collection = arguments.get("collection").toString();
        location = arguments.get("zone").toString();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        topAppBar_tasks_root.setNavigationOnClickListener(v -> onBackPressed());

        edit_task_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskActivity.class);
            intent.putExtra("id_task", id);
            intent.putExtra("collection", collection);
            intent.putExtra("zone", location);
            startActivity(intent);
        });

        load_data(collection, id);
    }

    void load_data(String collection, String id) {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_root.setVisibility(View.VISIBLE);

            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");

//            String IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image_key);
//            Picasso.with(this)
//                    .load(IMAGE_URL)
//                    .into(image_view_task_root, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            progress_bar_task_root.setVisibility(View.INVISIBLE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            Log.i(TAG, "Error on download");
//                        }
//                    });

            text_view_address_task_root.setText(address);
            text_view_floor_task_root.setText(floor);
            text_view_cabinet_task_root.setText(cabinet);
            text_view_name_task_root.setText(name_task);
            text_view_comment_task_root.setText(comment);
            text_view_status_task_root.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_root.setText(date_create_text);

            try {
                if (status.equals("Новая заявка"))
                    circle_status_root.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_root.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_root.setImageResource(R.color.green);
            } catch (Exception e){
                Log.e(TAG, "Error! " + e);
            }

        });

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_root.setVisibility(View.INVISIBLE));

        delete_task_btn.setOnClickListener(v -> show_dialog_delete());
    }

    void show_dialog_delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_delete_task))
                .setPositiveButton(getText(R.string.delete), (dialog, id) -> delete_task())
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void delete_task() {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.delete();

        onBackPressed();
    }

}