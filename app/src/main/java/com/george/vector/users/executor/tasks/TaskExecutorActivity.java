package com.george.vector.users.executor.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.common.tasks.FragmentImageTask;
import com.george.vector.common.tasks.FragmentUrgentRequest;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskExecutorActivity extends AppCompatActivity {

    private static final String TAG = "TaskExecutor";
    MaterialToolbar top_app_bar_task_executor;
    LinearProgressIndicator progress_bar_task_executor;
    TextView text_view_address_task_executor, text_view_floor_task_executor, text_view_cabinet_task_executor,
            text_view_name_task_executor, text_view_comment_task_executor, text_view_status_task_executor,
            text_view_date_create_task_executor, text_view_full_name_creator_executor, text_view_email_creator_task_executor,
            text_view_full_name_executor_EX, text_view_email_executor_task_executor, text_view_date_done_task_executor;
    CircleImageView circle_status_executor;
    FloatingActionButton edit_task_executor_btn;

    String id, collection, location, address, floor, cabinet, letter, name_task, comment, status, date_create, time_create,
            email_executor, email_creator, date_done, image, full_name_creator, full_name_executor;

    FirebaseFirestore firebaseFirestore;

    String email_main_activity;

    boolean urgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_executor);

        top_app_bar_task_executor = findViewById(R.id.topAppBar_task_executor);
        progress_bar_task_executor = findViewById(R.id.progress_bar_task_executor);
        text_view_address_task_executor = findViewById(R.id.text_view_address_task_executor);
        text_view_floor_task_executor = findViewById(R.id.text_view_floor_task_executor);
        text_view_cabinet_task_executor = findViewById(R.id.text_view_cabinet_task_executor);
        text_view_name_task_executor = findViewById(R.id.text_view_name_task_executor);
        text_view_comment_task_executor = findViewById(R.id.text_view_comment_task_executor);
        text_view_status_task_executor = findViewById(R.id.text_view_status_task_executor);
        text_view_date_create_task_executor = findViewById(R.id.text_view_date_create_task_executor);
        circle_status_executor = findViewById(R.id.circle_status_executor);
        edit_task_executor_btn = findViewById(R.id.edit_task_executor_btn);
        text_view_full_name_creator_executor = findViewById(R.id.text_view_full_name_creator_executor);
        text_view_email_creator_task_executor = findViewById(R.id.text_view_email_creator_task_executor);
        text_view_full_name_executor_EX = findViewById(R.id.text_view_full_name_executor_EX);
        text_view_email_executor_task_executor = findViewById(R.id.text_view_email_executor_task_executor);
        text_view_date_done_task_executor = findViewById(R.id.text_view_date_done_task_executor);

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);
        email_main_activity = arguments.getString(EMAIL);

        firebaseFirestore = FirebaseFirestore.getInstance();
        top_app_bar_task_executor.setNavigationOnClickListener(v -> onBackPressed());

        edit_task_executor_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskExecutorActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(LOCATION, location);
            intent.putExtra(EMAIL, email_main_activity);
            startActivity(intent);
        });

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_task_executor.setVisibility(View.VISIBLE);
            assert value != null;
            try {
                address = value.getString("address");
                floor = value.getString("floor");
                cabinet = value.getString("cabinet");
                letter = value.getString("litera");
                name_task = value.getString("name_task");
                comment = value.getString("comment");
                status = value.getString("status");
                date_create = value.getString("date_create");
                time_create = value.getString("time_create");
                email_executor = value.getString("executor");
                date_done = value.getString("date_done");
                image = value.getString("image");

                full_name_creator = value.getString("nameCreator");
                email_creator = value.getString("email_creator");

                full_name_executor = value.getString("fullNameExecutor");
                urgent = value.getBoolean("urgent");

                Log.d(TAG, "address: " + address);
                Log.d(TAG, "floor: " + floor);
                Log.d(TAG, "litera: " + letter);
                Log.d(TAG, "comment: " + comment);
                Log.d(TAG, "status: " + status);
                Log.d(TAG, "date_create: " + date_create);
                Log.d(TAG, "time_create: " + time_create);
                Log.d(TAG, "email_executor: " + email_executor);
                Log.d(TAG, "email_creator: " + email_creator);
                Log.d(TAG, "date_done: " + date_done);
                Log.d(TAG, "image: " + image);

                if (status.equals("Новая заявка"))
                    circle_status_executor.setImageResource(R.color.red);

                if (status.equals("В работе"))
                    circle_status_executor.setImageResource(R.color.orange);

                if (status.equals("Архив"))
                    circle_status_executor.setImageResource(R.color.green);

                if (!letter.equals("-") && !letter.isEmpty())
                    cabinet = String.format("%s%s", cabinet, letter);

                if (urgent) {
                    Log.d(TAG, "Срочная заявка");

                    Fragment urgent_fragment = new FragmentUrgentRequest();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_urgent_task_executor, urgent_fragment)
                            .commit();

                }

                if (image != null) {
                    Fragment image_fragment = new FragmentImageTask();

                    Bundle bundle = new Bundle();
                    bundle.putString("image_id", image);
                    bundle.putString(ID, id);
                    bundle.putString(COLLECTION, collection);
                    bundle.putString(LOCATION, location);
                    bundle.putString(EMAIL, "email");

                    image_fragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_image_task_executor, image_fragment)
                            .commit();
                }

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

            text_view_full_name_creator_executor.setText(full_name_creator);
            text_view_email_creator_task_executor.setText(email_creator);

            text_view_full_name_executor_EX.setText(full_name_executor);
            text_view_email_executor_task_executor.setText(email_executor);

            if (date_done == null)
                text_view_date_done_task_executor.setText("Дата выполнения не назначена");
            else {
                String date_done_text = "Дата выполнения: " + date_done;
                text_view_date_done_task_executor.setText(date_done_text);
            }

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