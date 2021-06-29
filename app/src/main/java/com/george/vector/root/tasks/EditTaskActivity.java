package com.george.vector.root.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.ErrorsUtils;
import com.george.vector.root.main.RootMainActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
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

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskRoot";

    MaterialToolbar topAppBar_new_task_root;
    LinearProgressIndicator progress_bar_add_task_root;
    ExtendedFloatingActionButton done_task_root;

    TextInputLayout text_input_layout_address_root, text_input_layout_floor_root,
            text_input_layout_cabinet_root, text_input_layout_name_task_root,
            text_input_layout_comment_root, text_input_layout_date_task_root,
            text_input_layout_executor_root, text_input_layout_status_root;

    TextInputEditText edit_text_date_task_root;

    MaterialAutoCompleteTextView address_autoComplete_root, executor_autoComplete_root,
            status_autoComplete_root;

    String id, collection, address, floor, cabinet, name_task, comment, status, date_create, time_create,
            date_done, executor, email, URI_IMAGE;
    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_root);

        topAppBar_new_task_root = findViewById(R.id.topAppBar_new_task_root);
        progress_bar_add_task_root = findViewById(R.id.progress_bar_add_task_root);
        done_task_root = findViewById(R.id.done_task_root);
        text_input_layout_address_root = findViewById(R.id.text_input_layout_address_root);
        text_input_layout_floor_root = findViewById(R.id.text_input_layout_floor_root);
        text_input_layout_cabinet_root = findViewById(R.id.text_input_layout_cabinet_root);
        text_input_layout_name_task_root = findViewById(R.id.text_input_layout_name_task_root);
        text_input_layout_comment_root = findViewById(R.id.text_input_layout_comment_root);
        text_input_layout_date_task_root = findViewById(R.id.text_input_layout_date_task_root);
        text_input_layout_executor_root = findViewById(R.id.text_input_layout_executor_root);
        text_input_layout_status_root = findViewById(R.id.text_input_layout_status_root);
        edit_text_date_task_root = findViewById(R.id.edit_text_date_task_root);
        address_autoComplete_root = findViewById(R.id.address_autoComplete_root);
        executor_autoComplete_root = findViewById(R.id.executor_autoComplete_root);
        status_autoComplete_root = findViewById(R.id.status_autoComplete_root);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        topAppBar_new_task_root.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task").toString();
        collection = arguments.get("collection").toString();
        Log.i(TAG, "id: " + id);
        Log.i(TAG, "collection: " + collection);

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
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
            URI_IMAGE = value.getString("uri_image");

            try {
                Objects.requireNonNull(text_input_layout_address_root.getEditText()).setText(address);
                Objects.requireNonNull(text_input_layout_floor_root.getEditText()).setText(floor);
                Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).setText(cabinet);
                Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).setText(name_task);
                Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(date_done);
                Objects.requireNonNull(text_input_layout_executor_root.getEditText()).setText(executor);
                Objects.requireNonNull(text_input_layout_status_root.getEditText()).setText(status);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(text_input_layout_comment_root.getEditText()).setText("");
                else
                    Objects.requireNonNull(text_input_layout_comment_root.getEditText()).setText(comment);
            } catch (Exception e) {
                Log.i(TAG, "Error! " + e);
            }
            initialize_fields();

        });

        done_task_root.setOnClickListener(v -> {

            if(validateFields()) {

                if(!isOnline()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Внимание!")
                            .setMessage("Отсуствует интернет подключение. Вы можете сохранить обновленную заявку у себя в телефоне и когда интренет снова появиться заявка автоматически будет отправлена в фоновом режиме. Или вы можете отправить заявку заявку позже, когда появиться интрнет.")
                            .setPositiveButton("Сохранить", (dialog, id) -> {

                                switch (status) {
                                    case "Новая заявка":
                                        updateTask("ost_school_new", collection);
                                    case "В работе":
                                        updateTask("ost_school_progress", collection);
                                    case "Архив":
                                        updateTask("ost_school_archive", collection);
                                }

                            })
                            .setNegativeButton(android.R.string.cancel, (dialog, id) -> startActivity(new Intent(this, RootMainActivity.class)));

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {

                    switch (status){
                        case "Новая заявка":
                            updateTask("ost_school_new", collection);
                        case "В работе":
                            updateTask("ost_school_progress", collection);
                        case "Архив":
                            updateTask("ost_school_archive", collection);

                    }
                }
            }
        });


        clearErrors();
    }

    void delete_task(String collection, String id) {
        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        documentReferenceTask.delete();
    }

    void updateTask(String collection, String current) {
        progress_bar_add_task_root.setVisibility(View.VISIBLE);

        Log.i(TAG, "collection: " + collection);
        Log.i(TAG, "current: " + current);
        Log.i(TAG, "date create: " + date_create);
        Log.i(TAG, "time create: " + time_create);

        String update_address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(text_input_layout_comment_root.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();

        if (update_comment.isEmpty())
            update_comment = "Нет коментария к заявке";

        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        Map<String, Object> new_task = new HashMap<>();

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

        new_task.put("uri_image", URI_IMAGE);

        documentReferenceTask.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                Log.i(TAG, "update completed!");
                progress_bar_add_task_root.setVisibility(View.INVISIBLE);
                startActivity(new Intent(this, RootMainActivity.class));
            } else {
                Log.i(TAG, "Error: " + task.getException());
            }

        });

        documentReferenceTask.set(new_task)
                .addOnSuccessListener(unused -> Log.i(TAG, "onSuccess: task - " + id))
                .addOnFailureListener(e -> Log.i(TAG, "Failure - " + e.toString()));

        delete_task(current, id);
    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

        address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        name_task = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();

        boolean check_address = errorsUtils.validate_field(address);
        boolean check_floor = errorsUtils.validate_field(floor);
        boolean check_cabinet = errorsUtils.validate_field(cabinet);
        boolean check_name_task = errorsUtils.validate_field(name_task);
        boolean check_date_task = errorsUtils.validate_field(date_task);
        boolean check_executor = errorsUtils.validate_field(executor);
        boolean check_status = errorsUtils.validate_field(status);

        if(check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status) {
            return true;
        } else {

            if(!check_address)
                text_input_layout_address_root.setError("Это поле не может быть пустым");

            if(!check_floor)
                text_input_layout_floor_root.setError("Это поле не может быть пустым");

            if(!check_cabinet)
                text_input_layout_cabinet_root.setError("Это поле не может быть пустым");

            if(!check_name_task)
                text_input_layout_name_task_root.setError("Это поле не может быть пустым");

            if(!check_date_task)
                text_input_layout_date_task_root.setError("Это поле не может быть пустым");

            if(!check_executor)
                text_input_layout_executor_root.setError("Это поле не может быть пустым");

            if(!check_status)
                text_input_layout_status_root.setError("Это поле не может быть пустым");

            return false;
        }

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void initialize_fields() {

        String[] items = getResources().getStringArray(R.array.addresses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditTaskActivity.this,
                R.layout.dropdown_menu_categories,
                items
        );

        address_autoComplete_root.setAdapter(adapter);

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_root.setAdapter(adapter_status);

        String[] items_executors = getResources().getStringArray(R.array.executors_ostafyevo);
        ArrayAdapter<String> adapter_executors = new ArrayAdapter<>(
                EditTaskActivity.this,
                R.layout.dropdown_menu_categories,
                items_executors
        );

        executor_autoComplete_root.setAdapter(adapter_executors);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_root.setOnClickListener(v -> new DatePickerDialog(EditTaskActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    void clearErrors() {
        Objects.requireNonNull(text_input_layout_address_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_address_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_floor_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_floor_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_cabinet_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_task_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_comment_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_comment_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_date_task_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_executor_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_executor_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_status_root.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_status_root.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
