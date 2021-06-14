package com.george.vector.admin.tasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.admin.MainAdminActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditTaskAdminActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskAdmin";
    MaterialToolbar topAppBar_new_task;

    TextInputLayout text_input_layout_address, text_input_layout_floor, text_input_layout_cabinet,
            text_input_layout_name_task, text_input_layout_comment, text_input_layout_date_task,
            text_input_layout_executor, text_input_layout_status;

    TextInputEditText edit_text_date_task;

    MaterialAutoCompleteTextView status_autoComplete, address_autoComplete, executor_autoComplete;
    ExtendedFloatingActionButton crate_task;

    String id, address, floor, cabinet, name_task, comment, status, date_create, time_create,
            date_done, executor, email;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    Calendar datePickCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_admin);

        topAppBar_new_task = findViewById(R.id.topAppBar_new_task);
        text_input_layout_address = findViewById(R.id.text_input_layout_address);
        text_input_layout_floor = findViewById(R.id.text_input_layout_floor);
        text_input_layout_cabinet = findViewById(R.id.text_input_layout_cabinet);
        text_input_layout_name_task = findViewById(R.id.text_input_layout_name_task);
        text_input_layout_comment = findViewById(R.id.text_input_layout_comment);
        text_input_layout_date_task = findViewById(R.id.text_input_layout_date_task);
        text_input_layout_executor = findViewById(R.id.text_input_layout_executor);
        text_input_layout_status = findViewById(R.id.text_input_layout_status);
        status_autoComplete = findViewById(R.id.status_autoComplete);
        address_autoComplete = findViewById(R.id.address_autoComplete);
        crate_task = findViewById(R.id.crate_task);
        edit_text_date_task = findViewById(R.id.edit_text_date_task);
        executor_autoComplete = findViewById(R.id.executor_autoComplete);

        topAppBar_new_task.setNavigationOnClickListener(v -> onBackPressed());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id").toString();
        Log.i(TAG, "id: " + id);

        DocumentReference documentReference = firebaseFirestore.collection("new tasks").document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("description");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            name_task = value.getString("title");
            comment = value.getString("comment");
            status = value.getString("status");

            date_done = value.getString("date_done");
            executor = value.getString("executor");

            date_create = value.getString("priority");
            time_create = value.getString("time_priority");
            email = value.getString("email_creator");

            Objects.requireNonNull(text_input_layout_address.getEditText()).setText(address);
            Objects.requireNonNull(text_input_layout_floor.getEditText()).setText(floor);
            Objects.requireNonNull(text_input_layout_cabinet.getEditText()).setText(cabinet);
            Objects.requireNonNull(text_input_layout_name_task.getEditText()).setText(name_task);
            Objects.requireNonNull(text_input_layout_date_task.getEditText()).setText(date_done);
            Objects.requireNonNull(text_input_layout_executor.getEditText()).setText(executor);
            Objects.requireNonNull(text_input_layout_status.getEditText()).setText(status);

            if(comment.equals("Нет коментария к заявке"))
                Objects.requireNonNull(text_input_layout_comment.getEditText()).setText("");
            else
                Objects.requireNonNull(text_input_layout_comment.getEditText()).setText(comment);

            String[] addresses = getResources().getStringArray(R.array.addresses);
            ArrayAdapter<String> arrayAdapterAddresses = new ArrayAdapter<>(
                    EditTaskAdminActivity.this,
                    R.layout.dropdown_menu_categories,
                    addresses
            );
            address_autoComplete.setAdapter(arrayAdapterAddresses);

            String[] items_status = getResources().getStringArray(R.array.status);
            ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                    EditTaskAdminActivity.this,
                    R.layout.dropdown_menu_categories,
                    items_status
            );

            status_autoComplete.setAdapter(adapter_status);

            String[] items_executors = getResources().getStringArray(R.array.executors_ostafyevo);
            ArrayAdapter<String> adapter_executors = new ArrayAdapter<>(
                    EditTaskAdminActivity.this,
                    R.layout.dropdown_menu_categories,
                    items_executors
            );

            executor_autoComplete.setAdapter(adapter_executors);

        });

        crate_task.setOnClickListener(v -> {
            Log.i(TAG, "date create: " + date_create);
            Log.i(TAG, "time create: " + time_create);

            String update_address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
            String update_floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
            String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
            String update_name = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
            String update_comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();
            String update_date_task = Objects.requireNonNull(text_input_layout_date_task.getEditText()).getText().toString();
            String update_executor = Objects.requireNonNull(text_input_layout_executor.getEditText()).getText().toString();
            String update_status = Objects.requireNonNull(text_input_layout_status.getEditText()).getText().toString();

            if(update_comment.isEmpty())
                update_comment = "Нет коментария к заявке";

            DocumentReference documentReferenceTask = firebaseFirestore.collection("new tasks").document(id);
            Map<String,Object> new_task = new HashMap<>();
            //Ручное добавление
            new_task.put("description", update_address);
            new_task.put("floor", update_floor);
            new_task.put("cabinet", update_cabinet);
            new_task.put("title", update_name);
            new_task.put("comment", update_comment);

            new_task.put("date_done", update_date_task);
            new_task.put("executor", update_executor);
            new_task.put("status", update_status);

            //Автоматическое добавление
            new_task.put("priority", date_create);
            new_task.put("time_priority", time_create);
            new_task.put("email_creator", email);

            documentReferenceTask.set(new_task)
                    .addOnSuccessListener(unused -> Log.i(TAG, "onSuccess: task - " + id))
                    .addOnFailureListener(e -> Log.i(TAG, "Failure - " + e.toString()));

            Intent intent = new Intent(this, MainAdminActivity.class);
            startActivity(intent);
        });

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task.setOnClickListener(v -> new DatePickerDialog(EditTaskAdminActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }


}
