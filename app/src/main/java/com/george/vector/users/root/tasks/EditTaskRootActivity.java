package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.USERS;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.common.tasks.utils.GetDataTask;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.users.root.main.RootMainActivity;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EditTaskRootActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskRoot";

    Calendar datePickCalendar;

    String id, collection, address, floor, cabinet, letter, nameTask, comment, status, dateCreate, timeCreate,
            dateDone, emailCreator, location, userEmail, image, fullNameExecutorRoot, nameCreator, nameExecutor,
            lastNameExecutor, patronymicExecutor, emailExecutor;

    boolean urgent;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = firebaseFirestore.collection("users");

    Query query;

    Utils utils = new Utils();

    ActivityAddTaskRootBinding addTaskRootBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTaskRootBinding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(addTaskRootBinding.getRoot());

        addTaskRootBinding.topAppBarNewTaskRoot.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);
        userEmail = arguments.getString(EMAIL);

        String buffer_size_preference = PreferenceManager
                .getDefaultSharedPreferences(EditTaskRootActivity.this)
                .getString("buffer_size", "2");
        Log.d(TAG, "buffer_size_preference: " + buffer_size_preference);

        int buffer_size = Integer.parseInt(buffer_size_preference);

        addTaskRootBinding.addExecutorRoot.setOnClickListener(v -> show_add_executor_dialog());

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            addTaskRootBinding.progressBarAddTaskRoot.setVisibility(View.VISIBLE);

            assert value != null;
            address = value.getString("address");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            letter = value.getString("litera");
            nameTask = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");

            dateDone = value.getString("date_done");
            emailExecutor = value.getString("executor");
            fullNameExecutorRoot = value.getString("fullNameExecutor");

            dateCreate = value.getString("date_create");
            timeCreate = value.getString("time_create");
            emailCreator = value.getString("email_creator");
            nameCreator = value.getString("nameCreator");

            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                addTaskRootBinding.progressBarAddTaskRoot.setVisibility(View.INVISIBLE);
            } else {
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.setImage(image, addTaskRootBinding.progressBarAddTaskRoot, addTaskRootBinding.imageTask, buffer_size);
            }

            try {
                urgent = value.getBoolean("urgent");

                Objects.requireNonNull(addTaskRootBinding.textInputLayoutAddressRoot.getEditText()).setText(address);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutFloorRoot.getEditText()).setText(floor);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetRoot.getEditText()).setText(cabinet);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetLiterRoot.getEditText()).setText(letter);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutNameTaskRoot.getEditText()).setText(nameTask);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutDateTaskRoot.getEditText()).setText(dateDone);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).setText(emailExecutor);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutStatusRoot.getEditText()).setText(status);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText()).setText(fullNameExecutorRoot);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(addTaskRootBinding.textInputLayoutCommentRoot.getEditText()).setText("");
                else
                    Objects.requireNonNull(addTaskRootBinding.textInputLayoutCommentRoot.getEditText()).setText(comment);

                addTaskRootBinding.urgentRequestCheckBox.setChecked(urgent);

            } catch (Exception e) {
                e.printStackTrace();
            }

            initialize_fields(location);
        });

        addTaskRootBinding.doneTaskRoot.setOnClickListener(v -> {

            if (validateFields()) {
                if (!isOnline())
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
        Chip chip_root_dialog = dialog.findViewById(R.id.chip_root_dialog);
        Chip chip_executors_dialog = dialog.findViewById(R.id.chip_executors_dialog);

        query = collectionReference.whereEqualTo("role", "Root");
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        UserAdapter adapter = new UserAdapter(options);

        recycler_view_list_executors.setHasFixedSize(true);
        recycler_view_list_executors.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_list_executors.setAdapter(adapter);

        chip_root_dialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "root checked");

                query = collectionReference.whereEqualTo("role", "Root");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        chip_executors_dialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "Executor checked");

                query = collectionReference.whereEqualTo("role", "Исполнитель");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            DocumentReference documentReference = firebaseFirestore.collection(USERS).document(id);
            documentReference.addSnapshotListener((value, error) -> {
                assert value != null;
                nameExecutor = value.getString("name");
                lastNameExecutor = value.getString("last_name");
                patronymicExecutor = value.getString("patronymic");
                emailExecutor = value.getString("email");
                String full_name_executor = lastNameExecutor + " " + nameExecutor + " " + patronymicExecutor;

                Log.i(TAG, String.format("name: %s", nameExecutor));
                Log.i(TAG, String.format("last_name: %s", lastNameExecutor));
                Log.i(TAG, String.format("patronymic: %s", patronymicExecutor));
                Log.i(TAG, String.format("email: %s", emailExecutor));

                Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).setText(emailExecutor);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText()).setText(full_name_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void updateTask(String collection) {

        String update_image = image;

        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        String update_address = Objects.requireNonNull(addTaskRootBinding.textInputLayoutAddressRoot.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutFloorRoot.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetRoot.getEditText()).getText().toString();
        String update_letter = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetLiterRoot.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(addTaskRootBinding.textInputLayoutNameTaskRoot.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCommentRoot.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(addTaskRootBinding.textInputLayoutDateTaskRoot.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).getText().toString();
        String update_name_executor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(addTaskRootBinding.textInputLayoutStatusRoot.getEditText()).getText().toString();
        boolean update_urgent = addTaskRootBinding.urgentRequestCheckBox.isChecked();

        task.save(new SaveTask(), location, update_name, update_address, dateCreate, update_floor,
                update_cabinet, update_letter, update_comment, update_date_task,
                update_executor, update_status, timeCreate, emailCreator, update_urgent, update_image, update_name_executor, nameCreator);

        Intent intent = new Intent(this, RootMainActivity.class);
        intent.putExtra(EMAIL, userEmail);
        startActivity(intent);
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    Intent intent = new Intent(this, RootMainActivity.class);
                    intent.putExtra(EMAIL, userEmail);
                    startActivity(intent);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean validateFields() {

        utils.clear_error(addTaskRootBinding.textInputLayoutAddressRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutFloorRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutCabinetRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutNameTaskRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutDateTaskRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutExecutorRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutStatusRoot);
        utils.clear_error(addTaskRootBinding.textInputLayoutFullNameExecutorRoot);

        address = Objects.requireNonNull(addTaskRootBinding.textInputLayoutAddressRoot.getEditText()).getText().toString();
        floor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutFloorRoot.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetRoot.getEditText()).getText().toString();
        nameTask = Objects.requireNonNull(addTaskRootBinding.textInputLayoutNameTaskRoot.getEditText()).getText().toString();
        String date_task = Objects.requireNonNull(addTaskRootBinding.textInputLayoutDateTaskRoot.getEditText()).getText().toString();
        emailExecutor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).getText().toString();
        status = Objects.requireNonNull(addTaskRootBinding.textInputLayoutStatusRoot.getEditText()).getText().toString();
        fullNameExecutorRoot = addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText().getText().toString();

        boolean check_address = utils.validate_field(address, addTaskRootBinding.textInputLayoutAddressRoot);
        boolean check_floor = utils.validate_field(floor, addTaskRootBinding.textInputLayoutFloorRoot);
        boolean check_cabinet = utils.validate_field(cabinet, addTaskRootBinding.textInputLayoutCabinetRoot);
        boolean check_name_task = utils.validate_field(nameTask, addTaskRootBinding.textInputLayoutNameTaskRoot);
        boolean check_date_task = utils.validate_field(date_task, addTaskRootBinding.textInputLayoutDateTaskRoot);
        boolean check_executor = utils.validate_field(emailExecutor, addTaskRootBinding.textInputLayoutExecutorRoot);
        boolean check_status = utils.validate_field(status, addTaskRootBinding.textInputLayoutStatusRoot);
        boolean check_name_executor = utils.validate_field(fullNameExecutorRoot, addTaskRootBinding.textInputLayoutFullNameExecutorRoot);

        return check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status & check_name_executor;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void initialize_fields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            addTaskRootBinding.addressAutoCompleteRoot.setAdapter(adapter);
        }

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        addTaskRootBinding.statusAutoCompleteRoot.setAdapter(adapter_status);

        String[] items_letter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_letter
        );

        addTaskRootBinding.literAutoCompleteRoot.setAdapter(adapter_letter);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        addTaskRootBinding.editTextDateTaskRoot.setOnClickListener(v ->
                new DatePickerDialog(EditTaskRootActivity.this, date, datePickCalendar
                        .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar
                        .get(Calendar.DAY_OF_MONTH))
                        .show());

        addTaskRootBinding.textInputLayoutFloorRoot.getEditText().addTextChangedListener(new TextValidator(addTaskRootBinding.textInputLayoutFloorRoot.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, addTaskRootBinding.textInputLayoutFloorRoot, addTaskRootBinding.doneTaskRoot, 1);
            }
        });

        addTaskRootBinding.textInputLayoutCabinetRoot.getEditText().addTextChangedListener(new TextValidator(addTaskRootBinding.textInputLayoutCabinetRoot.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, addTaskRootBinding.textInputLayoutCabinetRoot, addTaskRootBinding.doneTaskRoot, 3);
            }
        });

    }

    void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(addTaskRootBinding.textInputLayoutDateTaskRoot.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }
}