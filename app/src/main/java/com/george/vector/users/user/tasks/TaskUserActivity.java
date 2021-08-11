package com.george.vector.users.user.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.george.vector.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TaskUserActivity extends AppCompatActivity {

    private static final String TAG = "TaskUserActivity";

    MaterialToolbar toolbar;
    TextView text_view_address_task_user, text_view_floor_task_user, text_view_cabinet_task_user,
            text_view_name_task_user, text_view_comment_task_user, text_view_status_task_user,
            text_view_date_create_task_user;
    LinearProgressIndicator progress_bar_task_user;

    String id, permission, collection, address, floor, cabinet, name_task, comment, status, date_create, time_create;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_user);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task").toString();
        permission = arguments.getString("permission");

        toolbar = findViewById(R.id.topAppBar_task_user);
        text_view_address_task_user = findViewById(R.id.text_view_address_task_user);
        text_view_floor_task_user = findViewById(R.id.text_view_floor_task_user);
        text_view_cabinet_task_user = findViewById(R.id.text_view_cabinet_task_user);
        text_view_name_task_user = findViewById(R.id.text_view_name_task_user);
        text_view_comment_task_user = findViewById(R.id.text_view_comment_task_user);
        text_view_status_task_user = findViewById(R.id.text_view_status_task_user);
        text_view_date_create_task_user = findViewById(R.id.text_view_date_create_task_user);
        progress_bar_task_user = findViewById(R.id.progress_bar_task_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

         setSupportActionBar(toolbar);
         toolbar.setNavigationOnClickListener(v -> onBackPressed());

         if(permission.equals("ost_school"))
             collection = "ost_school_new";

         if(permission.equals("bar_school"))
             collection = "bar_school_new";

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_user.setVisibility(View.VISIBLE);
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
//                    .into(image_view_task_user, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            progress_bar_task_user.setVisibility(View.INVISIBLE);
//                        }
//
//                        @Override
//                        public void onError() {
//                            Log.e(TAG, "Error!");
//                        }
//                    });

            text_view_address_task_user.setText(address);
            text_view_floor_task_user.setText(floor);
            text_view_cabinet_task_user.setText(cabinet);
            text_view_name_task_user.setText(name_task);
            text_view_comment_task_user.setText(comment);
            text_view_status_task_user.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_user.setText(date_create_text);
        });

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_user.setVisibility(View.INVISIBLE));

    }
}