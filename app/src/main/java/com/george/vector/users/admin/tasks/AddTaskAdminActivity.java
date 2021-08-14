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
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTaskAdminActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    LinearProgressIndicator progress_bar_add_task_admin;
    ExtendedFloatingActionButton crate_task_fab;
    Button add_executor_admin;

    MaterialAutoCompleteTextView address_autoComplete, status_autoComplete;
    TextInputLayout text_input_layout_address, text_input_layout_floor, text_input_layout_cabinet,
                    text_input_layout_name_task, text_input_layout_comment, text_input_layout_date_task,
                    text_input_layout_status, text_input_layout_executor_admin;
    TextInputEditText edit_text_date_task;

    private static final String TAG = "AddTaskAdmin";
    String permission, address, floor, cabinet, name_task, comment, date_task, status, userID, email;
    String name_executor;
    String last_name_executor;
    String patronymic_executor;
    String email_executor;

    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

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
        text_input_layout_status = findViewById(R.id.text_input_layout_status);
        toolbar = findViewById(R.id.topAppBar_new_task);
        text_input_layout_executor_admin = findViewById(R.id.text_input_layout_executor_admin);
        edit_text_date_task = findViewById(R.id.edit_text_date_task);
        progress_bar_add_task_admin = findViewById(R.id.progress_bar_add_task_admin);
        add_executor_admin = findViewById(R.id.add_executor_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle arguments = getIntent().getExtras();
        permission = arguments.get("permission").toString();
        Log.i(TAG, "Permission: " + permission);

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        add_executor_admin.setOnClickListener(v -> show_add_executor_dialog());

        crate_task_fab.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();
            date_task = Objects.requireNonNull(text_input_layout_date_task.getEditText()).getText().toString();
            email_executor = Objects.requireNonNull(text_input_layout_executor_admin.getEditText()).getText().toString();
            status = Objects.requireNonNull(text_input_layout_status.getEditText()).getText().toString();

            if(validateFields()) {
                if(!isOnline())
                    show_dialog();
                else
                    save_task(permission);
            }

        });

        initialize_fields(permission);
    }

    void save_task(@NotNull String location) {
        Task task = new Task();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        task.save(new SaveTask(), location, name_task, address, dateText, floor, cabinet, comment,
                date_task, email_executor, status, timeText, email);

        onBackPressed();
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))

                .setPositiveButton(getText(R.string.save), (dialog, id) -> save_task(permission))

                .setNegativeButton(android.R.string.cancel,
                        (dialog, id) -> startActivity(new Intent(this, MainAdminActivity.class)));


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

    void initialize_fields(@NotNull String location) {
        if(location.equals("ost_school")) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskAdminActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            address_autoComplete.setAdapter(adapter);
        }

        if (location.equals("bar_school"))
            Objects.requireNonNull(text_input_layout_address.getEditText()).setText(getText(R.string.bar_school_address));

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete.setAdapter(adapter_status);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task.setOnClickListener(v -> new DatePickerDialog(AddTaskAdminActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        Utils utils = new Utils();

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