package com.george.vector.users.admin.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.george.vector.users.admin.main.MainAdminActivity;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.root.tasks.EditTaskRootActivity;
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

public class EditTaskAdminActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_new_task;
    LinearProgressIndicator progress_bar_add_task_admin;

    TextInputLayout text_input_layout_address, text_input_layout_floor, text_input_layout_cabinet,
                    text_input_layout_name_task, text_input_layout_comment, text_input_layout_date_task,
                    text_input_layout_status, text_input_layout_executor_admin, text_input_layout_cabinet_liter_admin;
    TextInputEditText edit_text_date_task;
    MaterialAutoCompleteTextView status_autoComplete, address_autoComplete, liter_autoComplete_admin;

    ExtendedFloatingActionButton update_task;
    Button add_executor_admin;

    Calendar datePickCalendar;

    private static final String TAG = "EditTaskAdmin";
    String id, permission, collection, address, floor,
            cabinet, litera, name_task, comment, status, date_create,
            time_create, date_done, email;
    String name_executor;
    String last_name_executor;
    String patronymic_executor;
    String email_executor;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_admin);

        topAppBar_new_task = findViewById(R.id.topAppBar_new_task);
        text_input_layout_address = findViewById(R.id.text_input_layout_address);
        text_input_layout_floor = findViewById(R.id.text_input_layout_floor);
        text_input_layout_cabinet = findViewById(R.id.text_input_layout_cabinet_admin);
        text_input_layout_cabinet_liter_admin = findViewById(R.id.text_input_layout_cabinet_liter_admin);
        text_input_layout_name_task = findViewById(R.id.text_input_layout_name_task);
        text_input_layout_comment = findViewById(R.id.text_input_layout_comment);
        text_input_layout_date_task = findViewById(R.id.text_input_layout_date_task);
        text_input_layout_status = findViewById(R.id.text_input_layout_status);
        status_autoComplete = findViewById(R.id.status_autoComplete);
        address_autoComplete = findViewById(R.id.address_autoComplete);
        text_input_layout_executor_admin = findViewById(R.id.text_input_layout_executor_admin);
        update_task = findViewById(R.id.crate_task);
        edit_text_date_task = findViewById(R.id.edit_text_date_task);
        progress_bar_add_task_admin = findViewById(R.id.progress_bar_add_task_admin);
        add_executor_admin = findViewById(R.id.add_executor_admin);
        liter_autoComplete_admin = findViewById(R.id.liter_autoComplete_admin);

        topAppBar_new_task.setNavigationOnClickListener(v -> onBackPressed());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(getString(R.string.id)).toString();
        collection = arguments.get(getString(R.string.collection)).toString();
        permission = arguments.get(getString(R.string.permission)).toString();

        add_executor_admin.setOnClickListener(v -> show_add_executor_dialog());

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            address = value.getString("address");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            litera = value.getString("litera");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");

            date_done = value.getString("date_done");
            email_executor = value.getString("executor");

            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            email = value.getString("email_creator");

            try {
                Objects.requireNonNull(text_input_layout_address.getEditText()).setText(address);
                Objects.requireNonNull(text_input_layout_floor.getEditText()).setText(floor);
                Objects.requireNonNull(text_input_layout_cabinet.getEditText()).setText(cabinet);
                Objects.requireNonNull(text_input_layout_cabinet_liter_admin.getEditText()).setText(litera);
                Objects.requireNonNull(text_input_layout_name_task.getEditText()).setText(name_task);
                Objects.requireNonNull(text_input_layout_date_task.getEditText()).setText(date_done);
                Objects.requireNonNull(text_input_layout_executor_admin.getEditText()).setText(email_executor);
                Objects.requireNonNull(text_input_layout_status.getEditText()).setText(status);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(text_input_layout_comment.getEditText()).setText("");
                else
                    Objects.requireNonNull(text_input_layout_comment.getEditText()).setText(comment);
            } catch (Exception e) {
                Log.e(TAG, "Error! " + e);
            }

            initialize_fields(permission);
        });

        documentReference.get().addOnCompleteListener(v -> progress_bar_add_task_admin.setVisibility(View.INVISIBLE));

        update_task.setOnClickListener(v -> {
            if(validateFields()) {
                if(!isOnline())
                    show_dialog();
                 else
                     updateTask(permission);
            }
        });

    }

    void updateTask(String location) {
        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        String update_address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
        String update_litera = Objects.requireNonNull(text_input_layout_cabinet_liter_admin.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(text_input_layout_date_task.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(text_input_layout_executor_admin.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(text_input_layout_status.getEditText()).getText().toString();

        task.save(new SaveTask(), location, update_name, update_address, date_create, update_floor,
                update_cabinet, update_litera, update_comment, update_date_task,
                update_executor, update_status, time_create,
                email);

        go_home();
    }

    void go_home() {
        Intent intent = new Intent(this, MainAdminActivity.class);
        intent.putExtra((String) getText(R.string.permission), permission);
        startActivity(intent);
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> go_home());

        AlertDialog dialog = builder.create();
        dialog.show();
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

                Objects.requireNonNull(text_input_layout_executor_admin.getEditText()).setText(email_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void initialize_fields(String permission) {
        if (permission.contentEquals(getText(R.string.ost_school))) {
            String[] addresses = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> arrayAdapterAddresses = new ArrayAdapter<>(
                    EditTaskAdminActivity.this,
                    R.layout.dropdown_menu_categories,
                    addresses
            );
            address_autoComplete.setAdapter(arrayAdapterAddresses);
        }

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete.setAdapter(adapter_status);

        String[] itemsLitera = getResources().getStringArray(R.array.litera);
        ArrayAdapter<String> adapter_litera = new ArrayAdapter<>(
                EditTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLitera
        );

        liter_autoComplete_admin.setAdapter(adapter_litera);

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
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    boolean validateFields() {
        Utils utils = new Utils();

        address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
        floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
        name_task = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
        String date_task = Objects.requireNonNull(text_input_layout_date_task.getEditText()).getText().toString();
        email_executor = Objects.requireNonNull(text_input_layout_executor_admin.getEditText()).getText().toString();
        status = Objects.requireNonNull(text_input_layout_status.getEditText()).getText().toString();

        utils.clear_error(text_input_layout_address);
        utils.clear_error(text_input_layout_floor);
        utils.clear_error(text_input_layout_cabinet);
        utils.clear_error(text_input_layout_name_task);
        utils.clear_error(text_input_layout_date_task);
        utils.clear_error(text_input_layout_executor_admin);
        utils.clear_error(text_input_layout_status);

        boolean check_address = utils.validate_field(address, text_input_layout_address);
        boolean check_floor = utils.validate_field(floor, text_input_layout_floor);
        boolean check_cabinet = utils.validate_field(cabinet, text_input_layout_cabinet);
        boolean check_name_task = utils.validate_field(name_task, text_input_layout_name_task);
        boolean check_date_task = utils.validate_field(date_task, text_input_layout_date_task);
        boolean check_executor = utils.validate_field(email_executor, text_input_layout_executor_admin);
        boolean check_status = utils.validate_field(status, text_input_layout_status);

        return check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status;
    }
}
