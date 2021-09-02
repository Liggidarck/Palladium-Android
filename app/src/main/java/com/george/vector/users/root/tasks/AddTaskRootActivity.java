package com.george.vector.users.root.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTaskRootActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_new_task_root;
    LinearProgressIndicator progress_bar_add_task_root;
    ExtendedFloatingActionButton done_task_root;
    Button add_executor_root;
    CheckBox urgent_request_check_box;
    TextInputLayout text_input_layout_address_root, text_input_layout_floor_root,
                    text_input_layout_cabinet_root, text_input_layout_name_task_root,
                    text_input_layout_comment_root, text_input_layout_date_task_root,
                    text_input_layout_executor_root, text_input_layout_status_root,
                    text_input_layout_cabinet_liter_root;
    TextInputEditText edit_text_date_task_root;
    MaterialAutoCompleteTextView address_autoComplete_root, status_autoComplete_root, liter_autoComplete_root;

    String location, userID, email, address, floor, cabinet, litera, name_task, date_complete, status, comment;
    boolean urgent;
    private static final String TAG = "AddTaskRoot";

    Calendar datePickCalendar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

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
        text_input_layout_cabinet_liter_root = findViewById(R.id.text_input_layout_cabinet_liter_root);
        liter_autoComplete_root = findViewById(R.id.liter_autoComplete_root);
        urgent_request_check_box = findViewById(R.id.urgent_request_check_box);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        topAppBar_new_task_root.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        location = arguments.get(getString(R.string.location)).toString();
        Log.d(TAG, location);

        //При выходе из аккаунта крашится тут
        try {
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
            documentReferenceUser.addSnapshotListener(this, (value, error) -> {
                assert value != null;
                email = value.getString("email");
            });
        } catch (Exception e) {
            Log.e(TAG, String.format("Error! %s", e));
        }

        add_executor_root.setOnClickListener(v -> show_add_executor_dialog());

        done_task_root.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
            litera = Objects.requireNonNull(text_input_layout_cabinet_liter_root.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment_root.getEditText()).getText().toString();
            date_complete = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
            email_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
            status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();
            urgent = urgent_request_check_box.isChecked();

            if(validateFields()){
                if(!isOnline())
                    show_dialog();
                 else
                    save_task(location);
            }
        });

        initialize_fields(location);
    }

    void save_task(String location) {
        Task task = new Task();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_create = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time_create = timeFormat.format(currentDate);

        task.save(new SaveTask(), location, name_task, address, date_create, floor, cabinet, litera, comment,
                date_complete, email_executor, status, time_create, email, urgent);

        onBackPressed();
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

                Log.i(TAG, String.format("name: %s", name_executor));
                Log.i(TAG, String.format("last_name: %s", last_name_executor));
                Log.i(TAG, String.format("patronymic: %s", patronymic_executor));
                Log.i(TAG, String.format("email: %s", email_executor));

                Objects.requireNonNull(text_input_layout_executor_root.getEditText()).setText(email_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) ->
                    save_task(location))
                .setNegativeButton(android.R.string.cancel,
                        (dialog, id) -> startActivity(new Intent(this, RootMainActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initialize_fields(String location) {
        if(location.equals(getString(R.string.ost_school))) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            address_autoComplete_root.setAdapter(adapter);
        }

        if (location.equals(getString(R.string.bar_school)))
            Objects.requireNonNull(text_input_layout_address_root.getEditText()).setText(getText(R.string.bar_school_address));

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_root.setAdapter(adapter_status);

        String[] itemsLitera = getResources().getStringArray(R.array.litera);
        ArrayAdapter<String> adapter_litera = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLitera
        );

        liter_autoComplete_root.setAdapter(adapter_litera);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_root.setOnClickListener(v -> new DatePickerDialog(AddTaskRootActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
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

        boolean check_address = utils.validate_field(address, text_input_layout_address_root);
        boolean check_floor = utils.validate_field(floor, text_input_layout_floor_root);
        boolean check_cabinet = utils.validate_field(cabinet, text_input_layout_cabinet_root);
        boolean check_name_task = utils.validate_field(name_task, text_input_layout_name_task_root);
        boolean check_date_task = utils.validate_field(date_complete, text_input_layout_date_task_root);
        boolean check_executor = utils.validate_field(email_executor, text_input_layout_executor_root);
        boolean check_status = utils.validate_field(status, text_input_layout_status_root);

        return  check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}