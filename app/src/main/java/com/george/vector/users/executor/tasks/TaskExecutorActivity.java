package com.george.vector.users.executor.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.george.vector.R;
import com.george.vector.common.tasks.ui.TaskUi;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


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

    String id, collection, location, address, floor, cabinet, name_task, comment, status, date_create, time_create,
            email_executor, email_creator, date_done;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

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

        Bundle arguments = getIntent().getExtras();
        id = arguments.get((String) getString(R.string.id)).toString();
        collection = arguments.get((String) getString(R.string.collection)).toString();
        location = arguments.get((String) getString(R.string.location)).toString();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        edit_btn_executor.setOnClickListener(v -> show_dialog());

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_executor.setVisibility(View.VISIBLE);
            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            email_executor = value.getString("executor");
            email_creator =value.getString("email_creator");
            date_done = value.getString("date_done");

            Log.d(TAG, "Address: " + address);
            Log.d(TAG, "floor: " + floor);
            Log.d(TAG, "cabinet: " + cabinet);
            Log.d(TAG, "name_task: " + name_task);
            Log.d(TAG, "comment: " + comment);
            Log.d(TAG, "status: " + status);
            Log.d(TAG, "comment: " + comment);
            Log.d(TAG, "date_create: " + date_create);
            Log.d(TAG, "time_create: " + time_create);
            Log.d(TAG, "email_executor: " + email_executor);
            Log.d(TAG, "email_creator: " + email_creator);
            Log.d(TAG, "date_done: " + date_done);

            text_view_address_task_executor.setText(address);
            text_view_floor_task_executor.setText(floor);
            text_view_cabinet_task_executor.setText(cabinet);
            text_view_name_task_executor.setText(name_task);
            text_view_comment_task_executor.setText(comment);
            text_view_status_task_executor.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_executor.setText(date_create_text);

            try {
                if (status.equals("Новая заявка"))
                    circle_status_executor.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_executor.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_executor.setImageResource(R.color.green);
            } catch (Exception e){
                Log.i(TAG, "Error! " + e);
            }

        });

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_executor.setVisibility(View.INVISIBLE));

    }

    void show_dialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_executor);

        RadioButton radio_button_new_task = dialog.findViewById(R.id.radio_button_new_task);
        RadioButton radio_button_progress = dialog.findViewById(R.id.radio_button_progress);
        RadioButton radio_button_archive = dialog.findViewById(R.id.radio_button_archive);
        Button done_btn_executor = dialog.findViewById(R.id.done_btn_executor);

        if (status.equals("Новая заявка"))
            radio_button_new_task.setChecked(true);

        if (status.equals("В работе"))
            radio_button_progress.setChecked(true);


        Log.d(TAG, "Address: " + address);
        Log.d(TAG, "floor: " + floor);
        Log.d(TAG, "cabinet: " + cabinet);
        Log.d(TAG, "name_task: " + name_task);
        Log.d(TAG, "comment: " + comment);
        Log.d(TAG, "status: " + status);
        Log.d(TAG, "comment: " + comment);
        Log.d(TAG, "date_create: " + date_create);
        Log.d(TAG, "time_create: " + time_create);
        Log.d(TAG, "email_executor: " + email_executor);
        Log.d(TAG, "email_creator: " + email_creator);
        Log.d(TAG, "date_done: " + date_done);


        done_btn_executor.setOnClickListener(v -> {
            delete_task(collection, id);

            if(location.equals("ost_school")) {
                if (radio_button_new_task.isChecked()) {
                    status = "Новая заявка";
                    load_data(getString(R.string.ost_school_new), name_task, address, date_done,
                            floor, cabinet, comment, date_create, email_executor,
                            status, time_create, email_creator);
                }

                if (radio_button_progress.isChecked()) {
                    status = "В работе";
                    load_data(getString(R.string.ost_school_progress), name_task, address, date_done,
                            floor, cabinet, comment, date_create, email_executor,
                            status, time_create, email_creator);
                }

                if (radio_button_archive.isChecked()) {
                    status = "Архив";
                    load_data(getString(R.string.ost_school_archive), name_task, address, date_done,
                            floor, cabinet, comment, date_create, email_executor,
                            status, time_create, email_creator);
                }
            }


            if(location.equals(getString(R.string.bar_school))) {
                if (radio_button_new_task.isChecked()) {
                    status = "Новая заявка";
                    load_data(getString(R.string.bar_school_new), name_task, address, date_done,
                            floor, cabinet, comment, date_create, email_executor,
                            status, time_create, email_creator);
                }

                if (radio_button_progress.isChecked()) {
                    status = "В работе";
                    load_data(getString(R.string.bar_school_progress), name_task, address, date_done,
                            floor, cabinet, comment, date_create, email_executor,
                            status, time_create, email_creator);
                }

                if (radio_button_archive.isChecked()) {
                    status = "Архив";
                    load_data(getString(R.string.bar_school_progress), name_task, address, date_done,
                            floor, cabinet, comment, date_create, email_executor,
                            status, time_create, email_creator);
                }
            }

            dialog.dismiss();
            onBackPressed();
        });

        dialog.show();
    }

    void load_data(String collection, String update_name, String  update_address, String update_date_task,
                   String update_floor, String update_cabinet, String update_comment, String date_create,
                   String update_executor, String update_status, String time_create, String email) {

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);
        taskRef.add(new TaskUi(update_name, update_address, update_date_task, update_floor, update_cabinet, update_comment,
                date_create, update_executor, update_status, time_create, email));

        taskRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");

            } else {
                Log.i(TAG, "Error: " + task.getException());
            }

        });
    }


    void delete_task(String collection, String id) {
        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        documentReferenceTask.delete();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_task_executor), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}