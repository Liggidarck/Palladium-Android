package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.tasks.fragmentImageTask;
import com.george.vector.common.tasks.fragmentUrgentRequest;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
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
            text_view_date_create_task_root, text_view_email_creator_task_root,
            text_view_email_executor_task_root, text_view_date_done_task_root, text_view_full_name_executor,
            text_view_full_name_creator;
    CircleImageView circle_status_root;
    FloatingActionButton edit_task_root_btn;

    private static final String TAG = "TaskActivityRoot";

    String id, collection, address, floor, cabinet, letter, name_task, comment, status,
            date_create, time_create, location, email, image, email_creator, email_executor,
            date_done, full_name_executor, full_name_creator;
    boolean confirm_delete, urgent;

    FirebaseFirestore firebaseFirestore;

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
        edit_task_root_btn = findViewById(R.id.edit_task_root_btn);

        text_view_email_creator_task_root = findViewById(R.id.text_view_email_creator_task_root);
        text_view_email_executor_task_root = findViewById(R.id.text_view_email_executor_task_root);
        text_view_date_done_task_root = findViewById(R.id.text_view_date_done_task_root);
        text_view_full_name_executor = findViewById(R.id.text_view_full_name_executor);
        text_view_full_name_creator = findViewById(R.id.text_view_full_name_creator);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(ID).toString();
        collection = arguments.get(COLLECTION).toString();
        location = arguments.get(LOCATION).toString();
        email = arguments.get(EMAIL).toString();
        confirm_delete = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("confirm_before_deleting_root", true);

        firebaseFirestore = FirebaseFirestore.getInstance();

        setSupportActionBar(topAppBar_tasks_root);
        topAppBar_tasks_root.setNavigationOnClickListener(v -> onBackPressed());

        edit_task_root_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(LOCATION, location);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        load_data(collection, id);
    }

    void load_data(String collection, String id) {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("address");
            floor = String.format("Этаж: %s", value.getString("floor"));
            cabinet = String.format("Кабинет: %s", value.getString("cabinet"));
            letter = value.getString("litera");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");
            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            image = value.getString("image");

            email_creator = value.getString("email_creator");
            email_executor = value.getString("executor");
            date_done = value.getString("date_done");
            full_name_executor = value.getString("fullNameExecutor");
            full_name_creator = value.getString("nameCreator");

            try {
                urgent = value.getBoolean("urgent");
                if (status.equals("Новая заявка"))
                    circle_status_root.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_root.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_root.setImageResource(R.color.green);

                if (!letter.equals("-") && !letter.isEmpty())
                    cabinet = String.format("%s%s", cabinet, letter);

                if (image == null) {
                    Log.d(TAG, "No image, stop loading");
                } else {
                    Fragment image_fragment = new fragmentImageTask();

                    Bundle bundle = new Bundle();
                    bundle.putString("image_id", image);
                    bundle.putString(ID, id);
                    bundle.putString(COLLECTION, collection);
                    bundle.putString(LOCATION, location);
                    bundle.putString(EMAIL, email);

                    image_fragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_image_task, image_fragment)
                            .commit();
                }

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

            text_view_email_creator_task_root.setText(email_creator);

            if (full_name_creator == null)
                text_view_full_name_creator.setText("Нет данных об этом пользователе (404)");
            else
                text_view_full_name_creator.setText(full_name_creator);

            if (full_name_executor == null)
                text_view_full_name_executor.setText("Нет назначенного исполнителя");
            else
                text_view_full_name_executor.setText(full_name_executor);

            if (email_executor == null)
                text_view_email_executor_task_root.setText("Нет данных");
            else
                text_view_email_executor_task_root.setText(email_executor);

            if (date_done == null)
                text_view_date_done_task_root.setText("Дата выполнения не назначена");
            else {
                String date_done_text = "Дата выполнения: " + date_done;
                text_view_date_done_task_root.setText(date_done_text);
            }

        });

        documentReference.get().addOnCompleteListener(task -> progress_bar_task_root.setVisibility(View.INVISIBLE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.root_delete_task) {

            if (confirm_delete)
                show_dialog_delete();
            else
                delete_task();

        }
        return true;
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

        if (image != null) {
            String storageUrl = "images/" + image;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(storageUrl);
            storageReference.delete();
        }

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