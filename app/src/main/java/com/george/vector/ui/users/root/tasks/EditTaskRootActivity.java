package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Logs.TAG_EDIT_TASK_ROOT_ACTIVITY;
import static com.george.vector.common.consts.Logs.TAG_STATE_TASK;

import static java.util.Objects.*;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.network.model.Task;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.network.viewmodel.ViewModelFactory;
import com.george.vector.ui.users.root.main.MainRootActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskRootActivity extends AppCompatActivity {

    Calendar datePickCalendar;

    String id, address, floor, cabinet, letter, nameTask, comment, status, dateCreate, timeCreate,
            dateDone, emailCreator, collection, userEmail, image, fullNameExecutor, nameCreator,
            emailExecutor;

    boolean urgent;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Utils utils = new Utils();

    ActivityAddTaskRootBinding binding;

    TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarAddEditTask.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(LOCATION);
        userEmail = arguments.getString(EMAIL);
        String bufferSizePreference = PreferenceManager
                .getDefaultSharedPreferences(EditTaskRootActivity.this)
                .getString("buffer_size", "2");

        Log.d(TAG_EDIT_TASK_ROOT_ACTIVITY, "buffer_size_preference: " + bufferSizePreference);


        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                collection))
                .get(TaskViewModel.class);

        int bufferSize = Integer.parseInt(bufferSizePreference);

        binding.addExecutorBtn.setOnClickListener(v -> utils.showAddExecutorDialog(EditTaskRootActivity.this,
                binding.taskEmailExecutor, binding.taskNameExecutor));

        getTask(bufferSize);

        binding.doneBtn.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }
            if (!utils.isOnline(EditTaskRootActivity.this)) {
                showDialog();
                return;
            }

            updateTask(collection);
        });
    }

    private void getTask(int bufferSize) {
        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            binding.progressBarAddEditTask.setVisibility(View.VISIBLE);

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
            fullNameExecutor = value.getString("fullNameExecutor");

            dateCreate = value.getString("date_create");
            timeCreate = value.getString("time_create");
            emailCreator = value.getString("email_creator");
            nameCreator = value.getString("nameCreator");

            image = value.getString("image");

            if (image == null) {
                Log.d(TAG_EDIT_TASK_ROOT_ACTIVITY, "No image, stop loading");
                binding.progressBarAddEditTask.setVisibility(View.INVISIBLE);
            } else {
                taskViewModel.setImage(image, binding.progressBarAddEditTask, binding.imageViewTask, bufferSize);
            }

            try {
                urgent = value.getBoolean("urgent");

                requireNonNull(binding.taskAddress.getEditText()).setText(address);
                requireNonNull(binding.taskFloor.getEditText()).setText(floor);
                requireNonNull(binding.taskCabinet.getEditText()).setText(cabinet);
                requireNonNull(binding.taskLetter.getEditText()).setText(letter);
                requireNonNull(binding.taskName.getEditText()).setText(nameTask);
                requireNonNull(binding.taskDateComplete.getEditText()).setText(dateDone);
                requireNonNull(binding.taskEmailExecutor.getEditText()).setText(emailExecutor);
                requireNonNull(binding.taskStatus.getEditText()).setText(status);
                requireNonNull(binding.taskNameExecutor.getEditText()).setText(fullNameExecutor);

                if (comment.equals("Нет коментария к заявке"))
                    requireNonNull(binding.taskComment.getEditText()).setText("");
                else
                    requireNonNull(binding.taskComment.getEditText()).setText(comment);

                binding.urgentCheckBox.setChecked(urgent);

            } catch (Exception e) {
                e.printStackTrace();
            }

            initializeFields(collection);
        });
    }

    void updateTask(String collection) {
        String updateImage = image;
        String updateAddress = requireNonNull(binding.taskAddress.getEditText()).getText().toString();
        String updateFloor = requireNonNull(binding.taskFloor.getEditText()).getText().toString();
        String updateCabinet = requireNonNull(binding.taskCabinet.getEditText()).getText().toString();
        String updateLetter = requireNonNull(binding.taskLetter.getEditText()).getText().toString();
        String updateName = requireNonNull(binding.taskName.getEditText()).getText().toString();
        String updateComment = requireNonNull(binding.taskComment.getEditText()).getText().toString();
        String updateDateComplete = requireNonNull(binding.taskDateComplete.getEditText()).getText().toString();
        String updateExecutor = requireNonNull(binding.taskEmailExecutor.getEditText()).getText().toString();
        String updateNameExecutor = requireNonNull(binding.taskNameExecutor.getEditText()).getText().toString();
        String updateStatus = requireNonNull(binding.taskStatus.getEditText()).getText().toString();
        boolean updateUrgent = binding.urgentCheckBox.isChecked();

        Log.d(TAG_STATE_TASK, "Save new task");
        Task task = new Task(updateName, updateAddress, dateCreate, updateFloor, updateCabinet,
                updateLetter, updateComment, updateDateComplete, updateExecutor, updateStatus, timeCreate,
                emailCreator, updateUrgent, updateImage, updateNameExecutor, nameCreator);

        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(this.getApplication(),
                collection)).get(TaskViewModel.class);

        taskViewModel.updateTask(id, task);

        Intent intent = new Intent(this, MainRootActivity.class);
        intent.putExtra(EMAIL, userEmail);
        startActivity(intent);
    }

    void showDialog() {
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
        utils.clearError(binding.taskAddress);
        utils.clearError(binding.taskFloor);
        utils.clearError(binding.taskCabinet);
        utils.clearError(binding.taskName);
        utils.clearError(binding.taskDateComplete);
        utils.clearError(binding.taskEmailExecutor);
        utils.clearError(binding.taskStatus);
        utils.clearError(binding.taskName);

        address = requireNonNull(binding.taskAddress.getEditText()).getText().toString();
        floor = requireNonNull(binding.taskFloor.getEditText()).getText().toString();
        cabinet = requireNonNull(binding.taskCabinet.getEditText()).getText().toString();
        nameTask = requireNonNull(binding.taskName.getEditText()).getText().toString();
        String dateTask = requireNonNull(binding.taskDateComplete.getEditText()).getText().toString();
        emailExecutor = requireNonNull(binding.taskEmailExecutor.getEditText()).getText().toString();
        status = requireNonNull(binding.taskStatus.getEditText()).getText().toString();
        fullNameExecutor = binding.taskNameExecutor.getEditText().getText().toString();

        boolean checkAddress = utils.validateField(address, binding.taskAddress);
        boolean checkFloor = utils.validateField(floor, binding.taskFloor);
        boolean checkCabinet = utils.validateField(cabinet, binding.taskCabinet);
        boolean checkNameTask = utils.validateField(nameTask, binding.taskName);
        boolean checkDateTask = utils.validateField(dateTask, binding.taskDateComplete);
        boolean checkExecutor = utils.validateField(emailExecutor, binding.taskEmailExecutor);
        boolean checkStatus = utils.validateField(status, binding.taskStatus);
        boolean checkNameExecutor = utils.validateField(fullNameExecutor, binding.taskNameExecutor);

        return checkAddress & checkFloor & checkCabinet & checkNameTask & checkDateTask & checkExecutor & checkStatus & checkNameExecutor;
    }

    void initializeFields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            binding.addressAutoComplete.setAdapter(adapter);
        }

        String[] itemsStatus = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsStatus
        );

        binding.statusAutoComplete.setAdapter(adapter_status);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        binding.letterAutoComplete.setAdapter(adapter_letter);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        binding.taskDateComplete.getEditText().setOnClickListener(v ->
                new DatePickerDialog(EditTaskRootActivity.this, date, datePickCalendar
                        .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar
                        .get(Calendar.DAY_OF_MONTH))
                        .show());

        binding.taskFloor.getEditText().addTextChangedListener(
                new TextValidator(binding.taskFloor.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, binding.taskFloor, binding.doneBtn, 1);
            }
        });

        binding.taskCabinet.getEditText().addTextChangedListener(
                new TextValidator(binding.taskCabinet.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, binding.taskCabinet, binding.doneBtn, 3);
            }
        });

    }

    void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        requireNonNull(binding.taskDateComplete.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }
}