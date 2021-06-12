package com.george.vector.admin.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.george.vector.R;
import com.george.vector.common.ErrorsUtils;
import com.george.vector.common.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTaskAdminActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    ExtendedFloatingActionButton crate_task_fab;
    MaterialAutoCompleteTextView address_autoComplete, status_autoComplete, executor_autoComplete;
    TextInputLayout text_input_layout_address, text_input_layout_floor, text_input_layout_cabinet,
            text_input_layout_name_task, text_input_layout_comment, text_input_layout_date_task,
            text_input_layout_executor, text_input_layout_status;
    TextInputEditText edit_text_date_task;

    String address, floor, cabinet, name_task, comment, date_task, executor, status, userID, email;

    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "AddTaskAdmin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_admin);

        crate_task_fab = findViewById(R.id.crate_task);
        address_autoComplete = findViewById(R.id.address_autoComplete);
        status_autoComplete = findViewById(R.id.status_autoComplete);
        text_input_layout_address = findViewById(R.id.text_input_layout_address);
        text_input_layout_floor = findViewById(R.id.text_input_layout_floor);
        text_input_layout_cabinet = findViewById(R.id.text_input_layout_cabinet);
        text_input_layout_name_task = findViewById(R.id.text_input_layout_name_task);
        text_input_layout_comment = findViewById(R.id.text_input_layout_comment);
        text_input_layout_date_task = findViewById(R.id.text_input_layout_date_task);
        text_input_layout_executor = findViewById(R.id.text_input_layout_executor);
        text_input_layout_status = findViewById(R.id.text_input_layout_status);
        toolbar = findViewById(R.id.topAppBar_new_task);
        edit_text_date_task = findViewById(R.id.edit_text_date_task);
        executor_autoComplete = findViewById(R.id.executor_autoComplete);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String[] items = getResources().getStringArray(R.array.addresses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AddTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                items
        );

        address_autoComplete.setAdapter(adapter);

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete.setAdapter(adapter_status);

        String[] items_executors = getResources().getStringArray(R.array.executors_ostafyevo);
        ArrayAdapter<String> adapter_executors = new ArrayAdapter<>(
                AddTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                items_executors
        );

        executor_autoComplete.setAdapter(adapter_executors);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task.setOnClickListener(v -> new DatePickerDialog(AddTaskAdminActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

        crate_task_fab.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();
            date_task = Objects.requireNonNull(text_input_layout_date_task.getEditText()).getText().toString();
            executor = Objects.requireNonNull(text_input_layout_executor.getEditText()).getText().toString();
            status = Objects.requireNonNull(text_input_layout_status.getEditText()).getText().toString();

            if(validateFields()){
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

                if(comment.isEmpty())
                    comment = "Нет коментария к заявке";

                Log.i(TAG, "comment(update): " + comment);

                CollectionReference taskRef = FirebaseFirestore.getInstance().collection("new tasks");
                taskRef.add(new Task(name_task, address, dateText, floor, cabinet, comment, date_task, executor, status, timeText, email));
                finish();
            }

        });

        clearErrors();
    }
    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
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
                text_input_layout_address.setError("Это поле не может быть пустым");

            if(!check_floor)
                text_input_layout_floor.setError("Это поле не может быть пустым");

            if(!check_cabinet)
                text_input_layout_cabinet.setError("Это поле не может быть пустым");

            if(!check_name_task)
                text_input_layout_name_task.setError("Это поле не может быть пустым");

            if(!check_date_task)
                text_input_layout_date_task.setError("Это поле не может быть пустым");

            if(!check_executor)
                text_input_layout_executor.setError("Это поле не может быть пустым");

            if(!check_status)
                text_input_layout_status.setError("Это поле не может быть пустым");

            return false;
        }

    }

    void clearErrors() {
        Objects.requireNonNull(text_input_layout_address.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_address.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_floor.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_floor.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_cabinet.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_cabinet.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_name_task.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_task.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_comment.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_comment.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_date_task.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_date_task.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_executor.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_executor.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_status.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_status.setError(null);
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