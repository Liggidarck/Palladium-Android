package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Logs.TAG_EDIT_TASK_ROOT_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_STATE_TASK;

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
import com.george.vector.users.root.main.MainRootActivity;
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

        String bufferSizePreference = PreferenceManager
                .getDefaultSharedPreferences(EditTaskRootActivity.this)
                .getString("buffer_size", "2");
        Log.d(TAG_EDIT_TASK_ROOT_ACTIVITY, "buffer_size_preference: " + bufferSizePreference);

        int bufferSize = Integer.parseInt(bufferSizePreference);

        addTaskRootBinding.addExecutorRoot.setOnClickListener(v -> showAddExecutorDialog());

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
                Log.d(TAG_EDIT_TASK_ROOT_ACTIVITY, "No image, stop loading");
                addTaskRootBinding.progressBarAddTaskRoot.setVisibility(View.INVISIBLE);
            } else {
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.setImage(image, addTaskRootBinding.progressBarAddTaskRoot, addTaskRootBinding.imageTask, bufferSize);
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

            initializeFields(location);
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

    public void showAddExecutorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recyclerExecutors = dialog.findViewById(R.id.recycler_view_list_executors);
        Chip chipRootDialog = dialog.findViewById(R.id.chip_root_dialog);
        Chip chipExecutorsDialog = dialog.findViewById(R.id.chip_executors_dialog);

        query = collectionReference.whereEqualTo("role", "Root");
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        UserAdapter adapter = new UserAdapter(options);

        recyclerExecutors.setHasFixedSize(true);
        recyclerExecutors.setLayoutManager(new LinearLayoutManager(this));
        recyclerExecutors.setAdapter(adapter);

        chipRootDialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                query = collectionReference.whereEqualTo("role", "Root");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        chipExecutorsDialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
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

                Log.i(TAG_EDIT_TASK_ROOT_ACTIVITY, String.format("name: %s", nameExecutor));
                Log.i(TAG_EDIT_TASK_ROOT_ACTIVITY, String.format("last_name: %s", lastNameExecutor));
                Log.i(TAG_EDIT_TASK_ROOT_ACTIVITY, String.format("patronymic: %s", patronymicExecutor));
                Log.i(TAG_EDIT_TASK_ROOT_ACTIVITY, String.format("email: %s", emailExecutor));

                Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).setText(emailExecutor);
                Objects.requireNonNull(addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText()).setText(full_name_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void updateTask(String collection) {
        String updateImage = image;

        Log.d(TAG_STATE_TASK, "TASK DELETING...");
        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.deleteTask(collection, id);

        String updateAddress = Objects.requireNonNull(addTaskRootBinding.textInputLayoutAddressRoot.getEditText()).getText().toString();
        String updateFloor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutFloorRoot.getEditText()).getText().toString();
        String updateCabinet = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetRoot.getEditText()).getText().toString();
        String updateLetter = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetLiterRoot.getEditText()).getText().toString();
        String updateName = Objects.requireNonNull(addTaskRootBinding.textInputLayoutNameTaskRoot.getEditText()).getText().toString();
        String updateComment = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCommentRoot.getEditText()).getText().toString();
        String updateDateTask = Objects.requireNonNull(addTaskRootBinding.textInputLayoutDateTaskRoot.getEditText()).getText().toString();
        String updateExecutor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).getText().toString();
        String updateNameExecutor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText()).getText().toString();
        String updateStatus = Objects.requireNonNull(addTaskRootBinding.textInputLayoutStatusRoot.getEditText()).getText().toString();
        boolean updateUrgent = addTaskRootBinding.urgentRequestCheckBox.isChecked();

        Log.d(TAG_STATE_TASK, "Save new task");
        task.save(new SaveTask(), location, updateName, updateAddress, dateCreate, updateFloor,
                updateCabinet, updateLetter, updateComment, updateDateTask,
                updateExecutor, updateStatus, timeCreate, emailCreator, updateUrgent, updateImage,
                updateNameExecutor, nameCreator);

        Log.d(TAG_EDIT_TASK_ROOT_ACTIVITY, "Start MainActivity");
        Intent intent = new Intent(this, MainRootActivity.class);
        intent.putExtra(EMAIL, userEmail);
        startActivity(intent);
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    Intent intent = new Intent(this, MainRootActivity.class);
                    intent.putExtra(EMAIL, userEmail);
                    startActivity(intent);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean validateFields() {

        utils.clearError(addTaskRootBinding.textInputLayoutAddressRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutFloorRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutCabinetRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutNameTaskRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutDateTaskRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutExecutorRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutStatusRoot);
        utils.clearError(addTaskRootBinding.textInputLayoutFullNameExecutorRoot);

        address = Objects.requireNonNull(addTaskRootBinding.textInputLayoutAddressRoot.getEditText()).getText().toString();
        floor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutFloorRoot.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(addTaskRootBinding.textInputLayoutCabinetRoot.getEditText()).getText().toString();
        nameTask = Objects.requireNonNull(addTaskRootBinding.textInputLayoutNameTaskRoot.getEditText()).getText().toString();
        String dateTask = Objects.requireNonNull(addTaskRootBinding.textInputLayoutDateTaskRoot.getEditText()).getText().toString();
        emailExecutor = Objects.requireNonNull(addTaskRootBinding.textInputLayoutExecutorRoot.getEditText()).getText().toString();
        status = Objects.requireNonNull(addTaskRootBinding.textInputLayoutStatusRoot.getEditText()).getText().toString();
        fullNameExecutorRoot = addTaskRootBinding.textInputLayoutFullNameExecutorRoot.getEditText().getText().toString();

        boolean checkAddress = utils.validateField(address, addTaskRootBinding.textInputLayoutAddressRoot);
        boolean checkFloor = utils.validateField(floor, addTaskRootBinding.textInputLayoutFloorRoot);
        boolean checkCabinet = utils.validateField(cabinet, addTaskRootBinding.textInputLayoutCabinetRoot);
        boolean checkNameTask = utils.validateField(nameTask, addTaskRootBinding.textInputLayoutNameTaskRoot);
        boolean checkDateTask = utils.validateField(dateTask, addTaskRootBinding.textInputLayoutDateTaskRoot);
        boolean checkExecutor = utils.validateField(emailExecutor, addTaskRootBinding.textInputLayoutExecutorRoot);
        boolean checkStatus = utils.validateField(status, addTaskRootBinding.textInputLayoutStatusRoot);
        boolean checkNameExecutor = utils.validateField(fullNameExecutorRoot, addTaskRootBinding.textInputLayoutFullNameExecutorRoot);

        return checkAddress & checkFloor & checkCabinet & checkNameTask & checkDateTask & checkExecutor & checkStatus & checkNameExecutor;
    }

    public boolean isOnline() {
        ConnectivityManager service = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = service.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void initializeFields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            addTaskRootBinding.addressAutoCompleteRoot.setAdapter(adapter);
        }

        String[] itemsStatus = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsStatus
        );

        addTaskRootBinding.statusAutoCompleteRoot.setAdapter(adapter_status);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
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