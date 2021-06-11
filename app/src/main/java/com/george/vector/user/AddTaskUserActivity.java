package com.george.vector.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import com.george.vector.R;
import com.george.vector.common.ErrorsUtils;
import com.george.vector.common.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTaskUserActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskUserActivity";
    MaterialToolbar topAppBar_new_task_user;
    TextInputLayout text_input_layout_address, text_input_layout_floor, text_input_layout_cabinet,
            text_input_layout_name_task, text_input_layout_comment;
    MaterialAutoCompleteTextView address_autoComplete;
    ExtendedFloatingActionButton crate_task;

    String address, floor, cabinet, name_task, comment, taskID, userID, email;

    String  date_done = null;
    String  executor = null;
    String  status = "Новая заявка";
    String  time_priority = null;


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_user);

        topAppBar_new_task_user = findViewById(R.id.topAppBar_new_task_user);
        text_input_layout_address = findViewById(R.id.text_input_layout_address);
        text_input_layout_floor = findViewById(R.id.text_input_layout_floor);
        text_input_layout_cabinet = findViewById(R.id.text_input_layout_cabinet);
        text_input_layout_name_task = findViewById(R.id.text_input_layout_name_task);
        text_input_layout_comment = findViewById(R.id.text_input_layout_comment);
        address_autoComplete = findViewById(R.id.address_autoComplete);
        crate_task = findViewById(R.id.crate_task);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String[] items = getResources().getStringArray(R.array.addresses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AddTaskUserActivity.this,
                R.layout.dropdown_menu_categories,
                items
        );

        address_autoComplete.setAdapter(adapter);

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
        });

        crate_task.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();

            if(validateFields()) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);

                CollectionReference taskRef = FirebaseFirestore.getInstance().collection("new tasks");
                taskRef.add(new Task(name_task, address, dateText, floor, cabinet, comment, date_done,
                        executor, status, time_priority, email));
                finish();

//                taskID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//                DocumentReference documentReferenceTask = firebaseFirestore.collection("new tasks").document(taskID);
//
//                Map<String,Object> new_task = new HashMap<>();
//                //Ручное добавление
//                new_task.put("description", address);
//                new_task.put("floor", floor);
//                new_task.put("cabinet", cabinet);
//                new_task.put("title", name_task);
//                new_task.put("comment", comment);
//
//                new_task.put("date_task", null);
//                new_task.put("executor", null);
//                new_task.put("status", "Новая заявка");
//
//                //Автоматическое добавление
//                new_task.put("priority", dateText);
//                new_task.put("time_create", timeText);
//                new_task.put("email_creator", email);
//
//                documentReferenceTask.set(new_task)
//                        .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: task - " + taskID))
//                        .addOnFailureListener(e -> Log.d("TAG", "Failure - " + e.toString()));

            }

        });

        clear_errors();

    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

        boolean check_address = errorsUtils.validate_field(address);
        boolean check_floor = errorsUtils.validate_field(floor);
        boolean check_cabinet = errorsUtils.validate_field(cabinet);
        boolean check_name_task = errorsUtils.validate_field(name_task);
        boolean check_comment = errorsUtils.validate_field(comment);


        if(check_address & check_floor & check_cabinet & check_name_task & check_comment) {
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

            if(!check_comment)
                text_input_layout_comment.setError("Это поле не может быть пустым");

            return false;
        }

    }

    void clear_errors() {

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

    }

}