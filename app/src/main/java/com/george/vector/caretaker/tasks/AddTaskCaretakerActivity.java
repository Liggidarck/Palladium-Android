package com.george.vector.caretaker.tasks;

import androidx.appcompat.app.AppCompatActivity;

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

import com.george.vector.R;
import com.george.vector.caretaker.main.MainCaretakerActivity;
import com.george.vector.common.utils.ErrorsUtils;
import com.george.vector.common.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTaskCaretakerActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskCaretaker";
    MaterialToolbar topAppBar_new_task_caretaker;
    LinearProgressIndicator progress_bar_add_task_caretaker;
    ExtendedFloatingActionButton done_task_caretaker;

    TextInputEditText edit_text_date_task_caretaker;
    TextInputLayout text_input_layout_address_caretaker, text_input_layout_floor_caretaker, text_input_layout_cabinet_caretaker,
            text_input_layout_name_task_caretaker, text_input_layout_comment_caretaker,
            text_input_layout_executor_caretaker, text_input_layout_status_caretaker, text_input_layout_date_task_caretaker;

    MaterialAutoCompleteTextView address_autoComplete_caretaker, executor_autoComplete_caretaker, status_autoComplete_caretaker;

    String location, userID, email, address, floor, cabinet, executor, name_task, date_task, status, comment;

    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_caretaker);

        topAppBar_new_task_caretaker = findViewById(R.id.topAppBar_new_task_caretaker);
        progress_bar_add_task_caretaker = findViewById(R.id.progress_bar_add_task_caretaker);
        done_task_caretaker = findViewById(R.id.done_task_caretaker);
        edit_text_date_task_caretaker = findViewById(R.id.edit_text_date_task_caretaker);
        text_input_layout_address_caretaker = findViewById(R.id.text_input_layout_address_caretaker);
        text_input_layout_name_task_caretaker = findViewById(R.id.text_input_layout_name_task_caretaker);
        text_input_layout_comment_caretaker = findViewById(R.id.text_input_layout_comment_caretaker);
        text_input_layout_floor_caretaker = findViewById(R.id.text_input_layout_floor_caretaker);
        text_input_layout_cabinet_caretaker = findViewById(R.id.text_input_layout_cabinet_caretaker);
        text_input_layout_executor_caretaker = findViewById(R.id.text_input_layout_executor_caretaker);
        text_input_layout_status_caretaker = findViewById(R.id.text_input_layout_status_caretaker);
        address_autoComplete_caretaker = findViewById(R.id.address_autoComplete_caretaker);
        text_input_layout_date_task_caretaker = findViewById(R.id.text_input_layout_date_task_caretaker);
        executor_autoComplete_caretaker = findViewById(R.id.executor_autoComplete_caretaker);
        status_autoComplete_caretaker = findViewById(R.id.status_autoComplete_caretaker);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        String location = arguments.get("location").toString();

        topAppBar_new_task_caretaker.setNavigationOnClickListener(v -> onBackPressed());

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
        });

        done_task_caretaker.setOnClickListener(v -> {

            address = Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor_caretaker.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet_caretaker.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task_caretaker.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).getText().toString();
            date_task = Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).getText().toString();
            executor = Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).getText().toString();
            status = Objects.requireNonNull(text_input_layout_status_caretaker.getEditText()).getText().toString();

            if(validateFields()){
                if(!isOnline())
                    show_alert_dialog();
                else
                    initialize_location(location);
            }

        });

        initialize_fields();
        clearErrors();
    }

    void show_alert_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Внимание!")
                .setMessage("Отсуствует интернет подключение. Вы можете сохранить заявку у себя в телефоне и когда интренет снова появиться заявка автоматически будет отправлена в фоновом режиме. Или вы можете отправить заявку заявку позже, когда появиться интрнет.")

                .setPositiveButton("Сохранить", (dialog, id) -> {

                    if(location.equals("ost_school")) {
                        if (status.equals("Новая заявка"))
                            saveTask("ost_school_new");

                        if (status.equals("В работе"))
                            saveTask("ost_school_progress");

                        if (status.equals("Архив"))
                            saveTask("ost_school_archive");
                    }

                })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, id) -> startActivity(new Intent(this, MainCaretakerActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initialize_location(@NotNull String location) {
        if (location.equals("ost_school")) {
            if (status.equals("Новая заявка"))
                saveTask("ost_school_new");

            if (status.equals("В работе"))
                saveTask("ost_school_progress");

            if (status.equals("Архив"))
                saveTask("ost_school_archive");
        }
    }

    void saveTask(@NotNull String collection) {
        progress_bar_add_task_caretaker.setVisibility(View.VISIBLE);

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        Log.i(TAG, "address: " + address);
        Log.i(TAG, "floor: " + floor);
        Log.i(TAG, "cabinet: " + cabinet);
        Log.i(TAG, "name_task: " + name_task);
        Log.i(TAG, "comment: " + comment);

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        Log.i(TAG, "comment(update): " + comment);

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);

        taskRef.add(new Task(name_task, address, dateText, floor, cabinet, comment,
                date_task, executor, status, timeText, email, "62d7f792-2144-4da4-bfe6-b1ea80d348d7"));

        taskRef.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");
                progress_bar_add_task_caretaker.setVisibility(View.INVISIBLE);
                startActivity(new Intent(this, MainCaretakerActivity.class));
            } else {
                Log.i(TAG, "Error: " + task.getException());
            }

        });

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void initialize_fields() {

        String[] items = getResources().getStringArray(R.array.addresses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AddTaskCaretakerActivity.this,
                R.layout.dropdown_menu_categories,
                items
        );

        address_autoComplete_caretaker.setAdapter(adapter);

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskCaretakerActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_caretaker.setAdapter(adapter_status);

        String[] items_executors = getResources().getStringArray(R.array.executors_ostafyevo);
        ArrayAdapter<String> adapter_executors = new ArrayAdapter<>(
                AddTaskCaretakerActivity.this,
                R.layout.dropdown_menu_categories,
                items_executors
        );

        executor_autoComplete_caretaker.setAdapter(adapter_executors);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_caretaker.setOnClickListener(v -> new DatePickerDialog(AddTaskCaretakerActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

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
                text_input_layout_address_caretaker.setError("Это поле не может быть пустым");

            if(!check_floor)
                text_input_layout_floor_caretaker.setError("Это поле не может быть пустым");

            if(!check_cabinet)
                text_input_layout_cabinet_caretaker.setError("Это поле не может быть пустым");

            if(!check_name_task)
                text_input_layout_name_task_caretaker.setError("Это поле не может быть пустым");

            if(!check_date_task)
                text_input_layout_date_task_caretaker.setError("Это поле не может быть пустым");

            if(!check_executor)
                text_input_layout_executor_caretaker.setError("Это поле не может быть пустым");

            if(!check_status)
                text_input_layout_status_caretaker.setError("Это поле не может быть пустым");

            return false;
        }
    }

    void clearErrors() {
        Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_address_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_floor_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_floor_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_cabinet_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_cabinet_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_name_task_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_task_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_comment_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_date_task_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_executor_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_status_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_status_caretaker.setError(null);
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