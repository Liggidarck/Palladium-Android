package com.george.vector.caretaker.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.caretaker.main.MainCaretakerActivity;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
import com.george.vector.common.tasks.ui.TaskUi;
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
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EdtTaskCaretakerActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskCaretaker";
    MaterialToolbar topAppBar_new_task_caretaker;
    LinearProgressIndicator progress_bar_add_task_caretaker;
    ExtendedFloatingActionButton done_task_caretaker;

    TextInputEditText edit_text_date_task_caretaker;
    TextInputLayout text_input_layout_address_caretaker, text_input_layout_floor_caretaker, text_input_layout_cabinet_caretaker,
            text_input_layout_name_task_caretaker, text_input_layout_comment_caretaker,
            text_input_layout_executor_caretaker, text_input_layout_status_caretaker, text_input_layout_date_task_caretaker;

    MaterialAutoCompleteTextView address_autoComplete_caretaker, status_autoComplete_caretaker;
    Button add_executor_caretaker;

    String id, collection, address, floor, cabinet, name_task, comment, status, date_create, time_create,
            date_done, email, URI_IMAGE, permission, location;
    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String name_executor;
    String last_name_executor;
    String patronymic_executor;
    String email_executor;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

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
        status_autoComplete_caretaker = findViewById(R.id.status_autoComplete_caretaker);
        add_executor_caretaker = findViewById(R.id.add_executor_caretaker);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task").toString();
        collection = arguments.get("collection").toString();
        location = arguments.get("location").toString();

        topAppBar_new_task_caretaker.setNavigationOnClickListener(v -> onBackPressed());

        add_executor_caretaker.setOnClickListener(v -> show_add_executor_dialog());

        String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference user_ref = firebaseFirestore.collection("users").document(userID);
        user_ref.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            permission = value.getString("permission");
        });

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
            email_executor = value.getString("executor");

            date_create = value.getString("priority");
            time_create = value.getString("time_priority");
            email = value.getString("email_creator");
            URI_IMAGE = value.getString("uri_image");

            try {
                Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).setText(address);
                Objects.requireNonNull(text_input_layout_floor_caretaker.getEditText()).setText(floor);
                Objects.requireNonNull(text_input_layout_cabinet_caretaker.getEditText()).setText(cabinet);
                Objects.requireNonNull(text_input_layout_name_task_caretaker.getEditText()).setText(name_task);
                Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).setText(date_done);
                Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).setText(email_executor);
                Objects.requireNonNull(text_input_layout_status_caretaker.getEditText()).setText(status);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).setText("");
                else
                    Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).setText(comment);

            } catch (Exception e) {
                Log.e(TAG, "Error! " + e);
            }

            initialize_fields(location);

        });

        done_task_caretaker.setOnClickListener(v -> {

            if(validateFields()) {

                if(!isOnline())
                    show_dialog();
                else
                    updateTask(location);

            }
        });

        clearErrors();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Внимание!")
                .setMessage("Отсуствует интернет подключение. Вы можете сохранить обновленную заявку у себя в телефоне и когда интренет снова появиться заявка автоматически будет отправлена в фоновом режиме. Или вы можете отправить заявку заявку позже, когда появиться интрнет.")
                .setPositiveButton("Сохранить", (dialog, id) -> updateTask(location))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    Intent intent = new Intent(this, MainCaretakerActivity.class);
                    intent.putExtra("permission", permission);
                    startActivity(intent);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void updateTask(String collection) {
        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        String update_address = Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(text_input_layout_floor_caretaker.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet_caretaker.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(text_input_layout_name_task_caretaker.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(text_input_layout_status_caretaker.getEditText()).getText().toString();

        task.save(new SaveTask(), location, update_name,
                update_address, update_date_task, update_floor,
                update_cabinet, update_comment, date_create,
                update_executor, update_status, time_create,
                email, "62d7f792-2144-4da4-bfe6-b1ea80d348d7");

    }

    public void show_add_executor_dialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recycler_view_list_executors = dialog.findViewById(R.id.recycler_view_list_executors);

        Query query = usersRef.whereEqualTo("role", "Исполнитель");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        UserAdapter adapter = new UserAdapter(options);

        recycler_view_list_executors.setHasFixedSize(true);
        recycler_view_list_executors.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_list_executors.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(id);
            documentReference.addSnapshotListener((value, error) -> {
                assert value != null;
                name_executor = value.getString("name");
                last_name_executor = value.getString("last_name");
                patronymic_executor = value.getString("patronymic");
                email_executor = value.getString("email");

                Log.i(TAG, "name: " + name_executor);
                Log.i(TAG, "last_name: " + last_name_executor);
                Log.i(TAG, "patronymic: " + patronymic_executor);
                Log.i(TAG, "email: " + email_executor);

                Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).setText(email_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void initialize_fields(String location) {
        if(location.equals("ost_school")) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EdtTaskCaretakerActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            address_autoComplete_caretaker.setAdapter(adapter);
        }
        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EdtTaskCaretakerActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_caretaker.setAdapter(adapter_status);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_caretaker.setOnClickListener(v -> new DatePickerDialog(EdtTaskCaretakerActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        Utils utils = new Utils();

        boolean check_address = utils.validate_field(address);
        boolean check_floor = utils.validate_field(floor);
        boolean check_cabinet = utils.validate_field(cabinet);
        boolean check_name_task = utils.validate_field(name_task);
        boolean check_date_task = utils.validate_field(date_done);
        boolean check_executor = utils.validate_field(email_executor);
        boolean check_status = utils.validate_field(status);

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
