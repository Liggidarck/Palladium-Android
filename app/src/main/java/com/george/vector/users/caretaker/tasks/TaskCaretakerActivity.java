package com.george.vector.users.caretaker.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskCaretakerActivity extends AppCompatActivity {

    private static final String TAG = "TaskCaretakerActivity";
    MaterialToolbar topAppBar_tasks_caretaker;
    LinearProgressIndicator progress_bar_task_caretaker;

    TextView text_view_address_task_caretaker, text_view_floor_task_caretaker, text_view_cabinet_task_caretaker,
            text_view_name_task_caretaker, text_view_comment_task_caretaker, text_view_status_task_caretaker,
            text_view_date_create_task_caretaker;
    CircleImageView circle_status_caretaker;
    Button edit_task_btn, delete_task_btn;

    String id, collection, address, floor, cabinet, name_task, comment, status, date_create, time_create, location;
    boolean confirm_delete;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_caretaker);

        topAppBar_tasks_caretaker = findViewById(R.id.topAppBar_tasks_caretaker);
        progress_bar_task_caretaker = findViewById(R.id.progress_bar_task_caretaker);
        text_view_address_task_caretaker = findViewById(R.id.text_view_address_task_caretaker);
        text_view_floor_task_caretaker = findViewById(R.id.text_view_floor_task_caretaker);
        text_view_cabinet_task_caretaker = findViewById(R.id.text_view_cabinet_task_caretaker);
        text_view_name_task_caretaker = findViewById(R.id.text_view_name_task_caretaker);
        text_view_comment_task_caretaker = findViewById(R.id.text_view_comment_task_caretaker);
        text_view_status_task_caretaker = findViewById(R.id.text_view_status_task_caretaker);
        text_view_date_create_task_caretaker = findViewById(R.id.text_view_date_create_task_caretaker);
        circle_status_caretaker = findViewById(R.id.circle_status_caretaker);
        edit_task_btn = findViewById(R.id.edit_task_btn);
        delete_task_btn = findViewById(R.id.delete_task_btn_caretaker);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(getString(R.string.id)).toString();
        collection = arguments.get(getString(R.string.collection)).toString();
        location = arguments.get(getString(R.string.location)).toString();
        confirm_delete = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("confirm_before_deleting", true);

        Log.d(TAG, "Id: " + id);
        Log.d(TAG, "Collection: " + collection);
        Log.d(TAG, "Location: " + location);

        topAppBar_tasks_caretaker.setNavigationOnClickListener(v -> onBackPressed());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        edit_task_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EdtTaskCaretakerActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), collection);
            intent.putExtra(getString(R.string.location), location);
            startActivity(intent);
        });

        delete_task_btn.setOnClickListener(v -> {
            if(confirm_delete)
                show_dialog_delete();
            else
                delete_task();
        });

        load_data(collection, id);
    }

    void load_data(String collection, String id) {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_caretaker.setVisibility(View.VISIBLE);

            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");

            text_view_address_task_caretaker.setText(address);
            text_view_floor_task_caretaker.setText(floor);
            text_view_cabinet_task_caretaker.setText(cabinet);
            text_view_name_task_caretaker.setText(name_task);
            text_view_comment_task_caretaker.setText(comment);
            text_view_status_task_caretaker.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_caretaker.setText(date_create_text);

            try {
                if (status.equals("Новая заявка"))
                    circle_status_caretaker.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_caretaker.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_caretaker.setImageResource(R.color.green);
            } catch (Exception e){
                Log.e(TAG, "Error! " + e);
            }

        });

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_caretaker.setVisibility(View.INVISIBLE));
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
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        onBackPressed();
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
            Snackbar.make(findViewById(R.id.coordinator_task_caretaker), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}