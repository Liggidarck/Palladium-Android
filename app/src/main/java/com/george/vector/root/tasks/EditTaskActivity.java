package com.george.vector.root.tasks;

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
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.utils.ErrorsUtils;
import com.george.vector.common.tasks.Task;
import com.george.vector.root.main.RootMainActivity;
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

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskRoot";

    MaterialToolbar topAppBar_new_task_root;
    LinearProgressIndicator progress_bar_add_task_root;
    ExtendedFloatingActionButton done_task_root;
    Button add_executor_root;

    TextInputLayout text_input_layout_address_root, text_input_layout_floor_root,
            text_input_layout_cabinet_root, text_input_layout_name_task_root,
            text_input_layout_comment_root, text_input_layout_date_task_root,
            text_input_layout_executor_root, text_input_layout_status_root;

    TextInputEditText edit_text_date_task_root;

    MaterialAutoCompleteTextView address_autoComplete_root, status_autoComplete_root;

    String id, collection, address, floor, cabinet, name_task, comment, status, date_create, time_create,
            date_done, email, URI_IMAGE, location;
    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    String name_executor;
    String last_name_executor;
    String patronymic_executor;
    String email_executor;

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
        status_autoComplete_root = findViewById(R.id.status_autoComplete_root);
        add_executor_root = findViewById(R.id.add_executor_root);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        topAppBar_new_task_root.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.get("id_task").toString();
        collection = arguments.get("collection").toString();
        location = arguments.get("zone").toString();

        add_executor_root.setOnClickListener(v -> show_add_executor_dialog());

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
                Objects.requireNonNull(text_input_layout_address_root.getEditText()).setText(address);
                Objects.requireNonNull(text_input_layout_floor_root.getEditText()).setText(floor);
                Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).setText(cabinet);
                Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).setText(name_task);
                Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(date_done);
                Objects.requireNonNull(text_input_layout_executor_root.getEditText()).setText(email_executor);
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
                if(!isOnline())
                    show_dialog();
                else
                    updateTask(collection);
            }

        });

        clearErrors();
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

                Objects.requireNonNull(text_input_layout_executor_root.getEditText()).setText(email_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }


    void updateTask(String collection) {
        progress_bar_add_task_root.setVisibility(View.VISIBLE);
        delete_task(collection, id);

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

        if(location.equals("ost_school")) {
            if (update_status.equals("Новая заявка")) {
                load_data("ost_school_new", update_name, update_address, update_date_task,
                        update_floor, update_cabinet, update_comment, date_create, update_executor,
                        update_status, time_create, email);
            }

            if (update_status.equals("В работе")) {
                load_data("ost_school_progress", update_name, update_address, update_date_task,
                        update_floor, update_cabinet, update_comment, date_create, update_executor,
                        update_status, time_create, email);
            }

            if (update_status.equals("Архив"))
                load_data("ost_school_archive", update_name, update_address, update_date_task,
                        update_floor, update_cabinet, update_comment, date_create, update_executor,
                        update_status, time_create, email);
        }
    }

    void load_data(String collection, String update_name, String  update_address, String update_date_task,
                   String update_floor, String update_cabinet, String update_comment, String date_create,
                   String update_executor, String update_status, String time_create, String email) {

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);
        taskRef.add(new Task(update_name, update_address, update_date_task, update_floor, update_cabinet, update_comment,
                date_create, update_executor, update_status, time_create, email, "62d7f792-2144-4da4-bfe6-b1ea80d348d7"));

        taskRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");
                progress_bar_add_task_root.setVisibility(View.INVISIBLE);
                startActivity(new Intent(this, RootMainActivity.class));
            } else {
                Log.i(TAG, "Error: " + task.getException());
            }

        });
    }

    void delete_task(String collection, String id) {
        DocumentReference documentReferenceTask = firebaseFirestore.collection(collection).document(id);
        documentReferenceTask.delete();
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Внимание!")
                .setMessage("Отсуствует интернет подключение. Вы можете сохранить обновленную заявку у себя в телефоне и когда интренет снова появиться заявка автоматически будет отправлена в фоновом режиме. Или вы можете отправить заявку заявку позже, когда появиться интрнет.")
                .setPositiveButton("Сохранить", (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> startActivity(new Intent(this, RootMainActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

        address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        name_task = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        email_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();

        boolean check_address = errorsUtils.validate_field(address);
        boolean check_floor = errorsUtils.validate_field(floor);
        boolean check_cabinet = errorsUtils.validate_field(cabinet);
        boolean check_name_task = errorsUtils.validate_field(name_task);
        boolean check_date_task = errorsUtils.validate_field(date_task);
        boolean check_executor = errorsUtils.validate_field(email_executor);
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
