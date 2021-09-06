package com.george.vector.users.root.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.george.vector.R;
import com.george.vector.common.announcements.fragmentUrgentRequest;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskRootActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_tasks_root;
    LinearProgressIndicator progress_bar_task_root;
    TextView text_view_address_task_root, text_view_floor_task_root, text_view_cabinet_task_root,
            text_view_name_task_root, text_view_comment_task_root, text_view_status_task_root,
            text_view_date_create_task_root;
    CircleImageView circle_status_root;
    Button edit_task_btn, delete_task_btn;
    ImageView image_root_task;

    private static final String TAG = "TaskActivityRoot";

    String id, collection, address, floor, cabinet, litera, name_task, comment, status, date_create, time_create, location, email, image;
    boolean confirm_delete, urgent;

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
        image_root_task = findViewById(R.id.image_root_task);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(getString(R.string.id)).toString();
        collection = arguments.get(getString(R.string.collection)).toString();
        location = arguments.get(getString(R.string.location)).toString();
        email = arguments.get(getString(R.string.email)).toString();
        confirm_delete = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("confirm_before_deleting_root", true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        topAppBar_tasks_root.setNavigationOnClickListener(v -> onBackPressed());

        edit_task_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskRootActivity.class);
            intent.putExtra(getString(R.string.id), id);
            intent.putExtra(getString(R.string.collection), collection);
            intent.putExtra(getString(R.string.location), location);
            intent.putExtra(getString(R.string.email), email);
            startActivity(intent);
        });

        delete_task_btn.setOnClickListener(v -> {
            if (confirm_delete)
                show_dialog_delete();
            else
                delete_task();
        });

        load_data(collection, id);
    }

    void load_data(String collection, String id) {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_root.setVisibility(View.VISIBLE);
            assert value != null;
            try {
                address = value.getString("address");
                floor = String.format("Этаж: %s", value.getString("floor"));
                cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
                litera = value.getString("litera");
                name_task = value.getString("name_task");
                comment = value.getString("comment");
                status = value.getString("status");
                date_create = value.getString("date_create");
                time_create = value.getString("time_create");
                urgent = value.getBoolean("urgent");
                image = value.getString("image");

                if (image == null) {
                    Log.d(TAG, "No image, stop loading");
                    progress_bar_task_root.setVisibility(View.INVISIBLE);
                    image_root_task.setImageResource(R.drawable.no_image);
                } else {

                    String IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image);
                    Picasso.with(this)
                            .load(IMAGE_URL)
                            .into(image_root_task, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progress_bar_task_root.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {
                                    Log.e(TAG, "Error on download");
                                    progress_bar_task_root.setVisibility(View.INVISIBLE);
                                }
                            });
                }


                if (status.equals("Новая заявка"))
                    circle_status_root.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_root.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_root.setImageResource(R.color.green);

                if (!litera.equals("-") && !litera.isEmpty())
                    cabinet = String.format("%s%s", cabinet, litera);

                if (urgent) {
                    Log.d(TAG, "Срочная заявка");

                    Fragment urgent_fragment = new fragmentUrgentRequest();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_urgent_task, urgent_fragment)
                            .commit();

                }

            } catch (Exception e) {
                Log.e(TAG, String.format("Error! %s", e));
            }

            text_view_address_task_root.setText(address);
            text_view_floor_task_root.setText(floor);
            text_view_cabinet_task_root.setText(cabinet);
            text_view_name_task_root.setText(name_task);
            text_view_comment_task_root.setText(comment);
            text_view_status_task_root.setText(status);

            String date_create_text = "Созданно: " + date_create + " " + time_create;
            text_view_date_create_task_root.setText(date_create_text);
        });
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

        String storageUrl = "images/" + image;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(storageUrl);
        storageReference.delete();

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

        if (!isOnline())
            Snackbar.make(findViewById(R.id.task_root_coordinator), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

}