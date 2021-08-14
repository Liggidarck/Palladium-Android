package com.george.vector.users.root.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.root.main.RootMainActivity;
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
            date_done, email, location;
    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection(getString(R.string.users));

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
        id = arguments.get(getString(R.string.id)).toString();
        collection = arguments.get(getString(R.string.collection)).toString();
        location = arguments.get(getString(R.string.location)).toString();

        add_executor_root.setOnClickListener(v -> show_add_executor_dialog());

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("address");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");

            date_done = value.getString("date_done");
            email_executor = value.getString("executor");

            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            email = value.getString("email_creator");

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

            initialize_fields(location);
        });

        done_task_root.setOnClickListener(v -> {

            if(validateFields()) {
                if(!isOnline())
                    show_dialog();
                else
                    updateTask(collection);
            }

        });
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
        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        String update_address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(text_input_layout_comment_root.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();

        task.save(new SaveTask(), location, update_name, update_address, date_create, update_floor,
                update_cabinet, update_comment, update_date_task,
                update_executor, update_status, time_create,
                email);

        startActivity(new Intent(this, RootMainActivity.class));
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> startActivity(new Intent(this, RootMainActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(text_input_layout_address_root);
        utils.clear_error(text_input_layout_floor_root);
        utils.clear_error(text_input_layout_cabinet_root);
        utils.clear_error(text_input_layout_name_task_root);
        utils.clear_error(text_input_layout_date_task_root);
        utils.clear_error(text_input_layout_executor_root);
        utils.clear_error(text_input_layout_status_root);

        address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        name_task = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        email_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();

        boolean check_address = utils.validate_field(address, text_input_layout_address_root);
        boolean check_floor = utils.validate_field(floor, text_input_layout_floor_root);
        boolean check_cabinet = utils.validate_field(cabinet, text_input_layout_cabinet_root);
        boolean check_name_task = utils.validate_field(name_task, text_input_layout_name_task_root);
        boolean check_date_task = utils.validate_field(date_task, text_input_layout_date_task_root);
        boolean check_executor = utils.validate_field(email_executor, text_input_layout_executor_root);
        boolean check_status = utils.validate_field(status, text_input_layout_status_root);

        return check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void initialize_fields(String location) {
        if (location.equals(getString(R.string.ost_school))) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            address_autoComplete_root.setAdapter(adapter);
        }

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
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }
}
