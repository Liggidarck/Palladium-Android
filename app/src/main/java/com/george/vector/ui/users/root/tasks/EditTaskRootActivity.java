package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.LOCATION;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Logs.TAG_STATE_TASK;
import static java.util.Objects.requireNonNull;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.utils.DialogsUtils;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.network.model.Task;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.network.viewmodel.ViewModelFactory;
import com.george.vector.ui.users.root.main.MainRootActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskRootActivity extends AppCompatActivity {

    private Calendar datePickCalendar;

    private String id, comment, dateCreate, timeCreate, emailCreator, collection, userEmail, image, nameCreator;

    private final TextValidatorUtils textValidatorUtils = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final DialogsUtils dialogsUtils = new DialogsUtils();

    private ActivityAddTaskRootBinding binding;

    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
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

        initializeFields(collection);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                collection))
                .get(TaskViewModel.class);

        int bufferSize = Integer.parseInt(bufferSizePreference);

        binding.addExecutorBtn.setOnClickListener(v -> dialogsUtils.showAddExecutorDialog(EditTaskRootActivity.this,
                binding.taskEmailExecutor, binding.taskNameExecutor));

        getTask(bufferSize);

        binding.doneBtn.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }

            if (!networkUtils.isOnline(EditTaskRootActivity.this)) {
                showDialog();
                return;
            }

            updateTask(collection);
        });
    }

    private void getTask(int bufferSize) {
        binding.progressBarAddEditTask.setVisibility(View.VISIBLE);
        taskViewModel.getTask(id).observe(EditTaskRootActivity.this, task -> {

            comment = task.getComment();
            dateCreate = task.getDate_create();
            timeCreate = task.getTime_create();
            image = task.getImage();
            emailCreator = task.getEmail_creator();
            nameCreator = task.getNameCreator();

            requireNonNull(binding.taskAddress.getEditText()).setText(task.getAddress());
            requireNonNull(binding.taskFloor.getEditText()).setText(task.getFloor());
            requireNonNull(binding.taskCabinet.getEditText()).setText(task.getCabinet());
            requireNonNull(binding.taskLetter.getEditText()).setText(task.getLitera());
            requireNonNull(binding.taskName.getEditText()).setText(task.getName_task());
            requireNonNull(binding.taskDateComplete.getEditText()).setText(task.getDate_done());
            requireNonNull(binding.taskEmailExecutor.getEditText()).setText(task.getExecutor());
            requireNonNull(binding.taskStatus.getEditText()).setText(task.getStatus());
            requireNonNull(binding.taskNameExecutor.getEditText()).setText(task.getFullNameExecutor());
            binding.urgentCheckBox.setChecked(task.getUrgent());

            if (comment.equals("Нет коментария к заявке"))
                requireNonNull(binding.taskComment.getEditText()).setText("");
            else
                requireNonNull(binding.taskComment.getEditText()).setText(comment);

            if (image != null)
                taskViewModel.setImage(image, binding.progressBarAddEditTask, binding.imageViewTask, bufferSize);

        });
        binding.progressBarAddEditTask.setVisibility(View.INVISIBLE);
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
        String address = binding.taskAddress.getEditText().getText().toString();
        String floor = binding.taskFloor.getEditText().getText().toString();
        String cabinet = binding.taskCabinet.getEditText().getText().toString();
        String taskName = binding.taskName.getEditText().getText().toString();
        String dateComplete = binding.taskDateComplete.getEditText().getText().toString();
        String emailExecutor = binding.taskEmailExecutor.getEditText().getText().toString();
        String taskStatus = binding.taskStatus.getEditText().getText().toString();
        String fullNameExecutor = binding.taskNameExecutor.getEditText().getText().toString();

        return textValidatorUtils.isEmptyField(address, binding.taskAddress) &
                textValidatorUtils.isEmptyField(floor, binding.taskFloor) &
                textValidatorUtils.isEmptyField(cabinet, binding.taskCabinet) &
                textValidatorUtils.isEmptyField(taskName, binding.taskName) &
                textValidatorUtils.isEmptyField(dateComplete, binding.taskDateComplete) &
                textValidatorUtils.isEmptyField(emailExecutor, binding.taskEmailExecutor) &
                textValidatorUtils.isEmptyField(taskStatus, binding.taskStatus) &
                textValidatorUtils.isEmptyField(fullNameExecutor, binding.taskNameExecutor)&
                textValidatorUtils.validateNumberField(cabinet, binding.taskCabinet, 3);
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

        String[] floors_basic_school = getResources().getStringArray(R.array.floors);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                floors_basic_school
        );
        binding.floorAutoComplete.setAdapter(arrayAdapter);

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

    }

    void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        requireNonNull(binding.taskDateComplete.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }
}