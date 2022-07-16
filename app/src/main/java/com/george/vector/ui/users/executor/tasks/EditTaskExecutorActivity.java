package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.network.utilsLegacy.DeleteTask;
import com.george.vector.network.utilsLegacy.SaveTask;
import com.george.vector.network.utilsLegacy.Task;
import com.george.vector.databinding.ActivityEditTaskExecutorBinding;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditTaskExecutorActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskExecutor";

    FirebaseFirestore firebaseFirestore;

    String id, collection, location, address, floor, cabinet, letter, name_task, comment, status, date_create, time_create,
            date_done, email, email_executor, image, full_name_executor, email_creator, name_creator,
            email_mail_activity;
    boolean urgent;

    ActivityEditTaskExecutorBinding executorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorBinding = ActivityEditTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(executorBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);
        email_mail_activity = arguments.getString(EMAIL);

        firebaseFirestore = FirebaseFirestore.getInstance();

        executorBinding.topAppBarNewTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            executorBinding.progressBarAddTaskExecutor.setVisibility(View.VISIBLE);
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

                full_name_executor = value.getString("fullNameExecutor");
                email_creator = value.getString("email_creator");
                name_creator = value.getString("nameCreator");
                urgent = value.getBoolean("urgent");

                image = value.getString("image");
                Objects.requireNonNull(executorBinding.textInputLayoutAddressExecutor.getEditText()).setText(address);
                Objects.requireNonNull(executorBinding.textInputLayoutFloorExecutor.getEditText()).setText(floor);
                Objects.requireNonNull(executorBinding.textInputLayoutCabinetExecutor.getEditText()).setText(cabinet);
                Objects.requireNonNull(executorBinding.textInputLayoutCabinetLiterExecutor.getEditText()).setText(letter);
                Objects.requireNonNull(executorBinding.textInputLayoutNameTaskExecutor.getEditText()).setText(name_task);
                Objects.requireNonNull(executorBinding.textInputLayoutStatusExecutor.getEditText()).setText(status);
                Objects.requireNonNull(executorBinding.textInputLayoutDateTaskExecutor.getEditText()).setText(date_done);
                Objects.requireNonNull(executorBinding.textInputLayoutEmailExecutor.getEditText()).setText(email_executor);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(executorBinding.textInputLayoutCommentExecutor.getEditText()).setText("");
                else
                    Objects.requireNonNull(executorBinding.textInputLayoutCommentExecutor.getEditText()).setText(comment);

            } catch (Exception e) {
                Log.i(TAG, "Error! " + e);
            }

            initialize_fields(location);
        });

        documentReference.get().addOnCompleteListener(v -> executorBinding.progressBarAddTaskExecutor.setVisibility(View.INVISIBLE));

        executorBinding.editTaskExecutor.setOnClickListener(v -> {
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

        String update_address = Objects.requireNonNull(executorBinding.textInputLayoutAddressExecutor.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(executorBinding.textInputLayoutFloorExecutor.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(executorBinding.textInputLayoutCabinetExecutor.getEditText()).getText().toString();
        String update_letter = Objects.requireNonNull(executorBinding.textInputLayoutCabinetLiterExecutor.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(executorBinding.textInputLayoutNameTaskExecutor.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(executorBinding.textInputLayoutCommentExecutor.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(executorBinding.textInputLayoutDateTaskExecutor.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(executorBinding.textInputLayoutEmailExecutor.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(executorBinding.textInputLayoutStatusExecutor.getEditText()).getText().toString();

        task.save(new SaveTask(), location, update_name, update_address, date_create, update_floor,
                update_cabinet, update_letter, update_comment, update_date_task,
                update_executor, update_status, time_create, email, urgent, update_image, full_name_executor, name_creator);

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
            executorBinding.addressAutoCompleteExecutor.setAdapter(adapter);
        }

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        executorBinding.statusAutoCompleteExecutor.setAdapter(adapter_status);

        String[] items_letter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                items_letter
        );

        executorBinding.literAutoCompleteExecutor.setAdapter(adapter_letter);

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}