package com.george.vector.users.executor.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskUi;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

public class TaskExecutorActivity extends AppCompatActivity {

    private static final String TAG = "TaskExecutor";
    MaterialToolbar topAppBar_task_executor;
    LinearProgressIndicator progress_bar_task_executor;
    TextView text_view_address_task_executor, text_view_floor_task_executor, text_view_cabinet_task_executor,
            text_view_name_task_executor, text_view_comment_task_executor, text_view_status_task_executor,
            text_view_date_create_task_executor;
    Button edit_btn_executor;
    CircleImageView circle_status_executor;
    ImageView image_executor_task;

    String id, collection, location, address, floor, cabinet, litera, name_task, comment, status, date_create, time_create,
            email_executor, email_creator, date_done, image;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String email_mail_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_executor);

        topAppBar_task_executor = findViewById(R.id.topAppBar_task_executor);
        progress_bar_task_executor = findViewById(R.id.progress_bar_task_executor);
        text_view_address_task_executor = findViewById(R.id.text_view_address_task_executor);
        text_view_floor_task_executor = findViewById(R.id.text_view_floor_task_executor);
        text_view_cabinet_task_executor = findViewById(R.id.text_view_cabinet_task_executor);
        text_view_name_task_executor = findViewById(R.id.text_view_name_task_executor);
        text_view_comment_task_executor = findViewById(R.id.text_view_comment_task_executor);
        text_view_status_task_executor = findViewById(R.id.text_view_status_task_executor);
        text_view_date_create_task_executor = findViewById(R.id.text_view_date_create_task_executor);
        edit_btn_executor = findViewById(R.id.edit_btn_executor);
        circle_status_executor = findViewById(R.id.circle_status_executor);
        image_executor_task = findViewById(R.id.image_executor_task);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(getString(R.string.id)).toString();
        collection = arguments.get(getString(R.string.collection)).toString();
        location = arguments.get(getString(R.string.location)).toString();
        email_mail_activity = arguments.getString(getString(R.string.email));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        topAppBar_task_executor.setNavigationOnClickListener(v -> onBackPressed());

        edit_btn_executor.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskExecutorActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), collection);
            intent.putExtra(getString(R.string.location), location);
            intent.putExtra(getString(R.string.email), email_mail_activity);
            startActivity(intent);
        });

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_executor.setVisibility(View.VISIBLE);
            assert value != null;
            address = value.getString("address");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            litera = value.getString("litera");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            email_executor = value.getString("executor");
            email_creator = value.getString("email_creator");
            date_done = value.getString("date_done");

            image = value.getString("image");

            String IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image);
            Picasso.with(this)
                    .load(IMAGE_URL)
                    .into(image_executor_task, new Callback() {
                        @Override
                        public void onSuccess() {
                            image_executor_task.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "Error on download");
                        }
                    });

            Log.d(TAG, "address: " + address);
            Log.d(TAG, "floor: " + floor);
            Log.d(TAG, "litera: " + litera);
            Log.d(TAG, "comment: " + comment);
            Log.d(TAG, "status: " + status);
            Log.d(TAG, "date_create: " + date_create);
            Log.d(TAG, "time_create: " + time_create);
            Log.d(TAG, "email_executor: " + email_executor);
            Log.d(TAG, "email_creator: " + email_creator);
            Log.d(TAG, "date_done: " + date_done);

            try {
                if (status.equals("Новая заявка"))
                    circle_status_executor.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_executor.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_executor.setImageResource(R.color.green);

                if (!litera.equals("-") && !litera.isEmpty())
                    cabinet = String.format("%s%s", cabinet, litera);
            } catch (Exception e) {
                Log.e(TAG, "Error! " + e);
            }

            text_view_address_task_executor.setText(address);
            text_view_floor_task_executor.setText(String.format("%s %s", getText(R.string.floor), floor));
            text_view_cabinet_task_executor.setText(String.format("%s %s", getText(R.string.cabinet), cabinet));
            text_view_name_task_executor.setText(name_task);
            text_view_comment_task_executor.setText(comment);
            text_view_status_task_executor.setText(status);

            String date_create_text = String.format("Созданно: %s %s", date_create, time_create);
            text_view_date_create_task_executor.setText(date_create_text);
        });

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_executor.setVisibility(View.INVISIBLE));

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_task_executor), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}