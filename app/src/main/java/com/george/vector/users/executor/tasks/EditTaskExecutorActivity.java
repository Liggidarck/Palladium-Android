package com.george.vector.users.executor.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditTaskExecutorActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskExecutor";
    MaterialToolbar topAppBar_new_task_executor;
    LinearProgressIndicator progress_bar_add_task_executor;
    ExtendedFloatingActionButton edit_task_executor;

    TextInputLayout text_input_layout_address_executor, text_input_layout_floor_executor,
            text_input_layout_cabinet_executor, text_input_layout_cabinet_liter_executor,
            text_input_layout_name_task_executor, text_input_layout_comment_executor,
            text_input_layout_status_executor, text_input_layout_date_task_executor,
            text_input_layout_executor_executor;

    MaterialAutoCompleteTextView address_autoComplete_executor, liter_autoComplete_executor,
            status_autoComplete_executor;

    FirebaseFirestore firebaseFirestore;

    String id, collection, location;

    String address, floor, cabinet, letter, name_task, comment, status, date_create, time_create,
            date_done, email, email_executor, image;

    String email_mail_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_executor);

        topAppBar_new_task_executor = findViewById(R.id.topAppBar_new_task_executor);
        progress_bar_add_task_executor = findViewById(R.id.progress_bar_add_task_executor);
        edit_task_executor = findViewById(R.id.edit_task_executor);
        text_input_layout_address_executor = findViewById(R.id.text_input_layout_address_executor);
        text_input_layout_floor_executor = findViewById(R.id.text_input_layout_floor_executor);
        text_input_layout_cabinet_executor = findViewById(R.id.text_input_layout_cabinet_executor);
        text_input_layout_cabinet_liter_executor = findViewById(R.id.text_input_layout_cabinet_liter_executor);
        text_input_layout_name_task_executor = findViewById(R.id.text_input_layout_name_task_executor);
        text_input_layout_comment_executor = findViewById(R.id.text_input_layout_comment_executor);
        text_input_layout_status_executor = findViewById(R.id.text_input_layout_status_executor);
        address_autoComplete_executor = findViewById(R.id.address_autoComplete_executor);
        liter_autoComplete_executor = findViewById(R.id.liter_autoComplete_executor);
        status_autoComplete_executor = findViewById(R.id.status_autoComplete_executor);
        text_input_layout_date_task_executor = findViewById(R.id.text_input_layout_date_task_executor);
        text_input_layout_executor_executor = findViewById(R.id.text_input_layout_executor_executor);

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);
        email_mail_activity = arguments.getString(EMAIL);

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_add_task_executor.setVisibility(View.VISIBLE);
            try {
                assert value != null;
                address = value.getString("address");
                floor = value.getString("floor");
                cabinet = value.getString("cabinet");
                letter = value.getString("litera");
                name_task = value.getString("name_task");
                comment = value.getString("comment");
                status = value.getString("status");

                date_done = value.getString("date_done");
                email_executor = value.getString("executor");

                date_create = value.getString("date_create");
                time_create = value.getString("time_create");
                email = value.getString("email_creator");

                image = value.getString("image");
                Objects.requireNonNull(text_input_layout_address_executor.getEditText()).setText(address);
                Objects.requireNonNull(text_input_layout_floor_executor.getEditText()).setText(floor);
                Objects.requireNonNull(text_input_layout_cabinet_executor.getEditText()).setText(cabinet);
                Objects.requireNonNull(text_input_layout_cabinet_liter_executor.getEditText()).setText(letter);
                Objects.requireNonNull(text_input_layout_name_task_executor.getEditText()).setText(name_task);
                Objects.requireNonNull(text_input_layout_status_executor.getEditText()).setText(status);
                Objects.requireNonNull(text_input_layout_date_task_executor.getEditText()).setText(date_done);
                Objects.requireNonNull(text_input_layout_executor_executor.getEditText()).setText(email_executor);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(text_input_layout_comment_executor.getEditText()).setText("");
                else
                    Objects.requireNonNull(text_input_layout_comment_executor.getEditText()).setText(comment);

            } catch (Exception e) {
                Log.i(TAG, "Error! " + e);
            }

            initialize_fields(location);
        });

        documentReference.get().addOnCompleteListener(v -> progress_bar_add_task_executor.setVisibility(View.INVISIBLE));

        edit_task_executor.setOnClickListener(v -> {
            if (!isOnline())
                show_dialog();
            else
                updateTask(collection);
        });
    }

    void updateTask(String collection) {

        String update_image = image;

        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        String update_address = Objects.requireNonNull(text_input_layout_address_executor.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(text_input_layout_floor_executor.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet_executor.getEditText()).getText().toString();
        String update_letter = Objects.requireNonNull(text_input_layout_cabinet_liter_executor.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(text_input_layout_name_task_executor.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(text_input_layout_comment_executor.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(text_input_layout_date_task_executor.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(text_input_layout_executor_executor.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(text_input_layout_status_executor.getEditText()).getText().toString();

        task.save(new SaveTask(), location, update_name, update_address, date_create, update_floor,
                update_cabinet, update_letter, update_comment, update_date_task,
                update_executor, update_status, time_create, email, false, update_image);

        Intent intent = new Intent(this, MainExecutorActivity.class);
        intent.putExtra(EMAIL, email_mail_activity);
        startActivity(intent);
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> startActivity(new Intent(this, MainExecutorActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initialize_fields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskExecutorActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            address_autoComplete_executor.setAdapter(adapter);
        }

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_executor.setAdapter(adapter_status);

        String[] items_letter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                items_letter
        );

        liter_autoComplete_executor.setAdapter(adapter_letter);

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}