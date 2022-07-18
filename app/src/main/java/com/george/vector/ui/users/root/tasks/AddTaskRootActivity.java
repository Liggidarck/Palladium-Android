package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static java.util.Objects.requireNonNull;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.network.model.Task;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.network.viewmodel.ViewModelFactory;
import com.george.vector.ui.tasks.BottomSheetAddImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskRootActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    ActivityAddTaskRootBinding binding;

    SharedPreferences preferences;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    String address, floor, cabinet, letter, taskName, dateComplete, taskStatus,
            comment, fullNameExecutor, emailExecutor, location, emailCreator, fullNameCreator;
    boolean urgent;

    private Uri fileUri;

    Calendar datePickCalendar;

    ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageViewTask.setImageURI(fileUri);
            });

    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageViewTask.setImageURI(fileUri);
                }
            });

    BottomSheetAddImage addImage = new BottomSheetAddImage();
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        preferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);

        Bundle arguments = getIntent().getExtras();
        location = arguments.get(LOCATION).toString();

        getUserData();
        initFields(location);

        binding.toolbarAddEditTask.setNavigationOnClickListener(v -> onBackPressed());

        binding.addExecutorBtn.setOnClickListener(v ->
                utils.showAddExecutorDialog(AddTaskRootActivity.this, binding.taskEmailExecutor,
                        binding.taskNameExecutor));

        binding.cardImage.setOnClickListener(v -> addImage.show(getSupportFragmentManager(), "BottomSheetAddImage"));

        binding.doneBtn.setOnClickListener(v -> {
            address = requireNonNull(binding.taskAddress.getEditText()).getText().toString();
            floor = requireNonNull(binding.taskFloor.getEditText()).getText().toString();
            cabinet = requireNonNull(binding.taskCabinet.getEditText()).getText().toString();
            letter = requireNonNull(binding.taskLetter.getEditText()).getText().toString();
            taskName = requireNonNull(binding.taskName.getEditText()).getText().toString();
            comment = requireNonNull(binding.taskComment.getEditText()).getText().toString();
            dateComplete = requireNonNull(binding.taskDateComplete.getEditText()).getText().toString();
            emailExecutor = requireNonNull(binding.taskEmailExecutor.getEditText()).getText().toString();
            taskStatus = requireNonNull(binding.taskStatus.getEditText()).getText().toString();
            urgent = binding.urgentCheckBox.isChecked();
            fullNameExecutor = requireNonNull(binding.taskNameExecutor.getEditText()).getText().toString();

            if (!validateFields()) {
                return;
            }

            if (!utils.isOnline(AddTaskRootActivity.this)) {
                showDialogNoInternet();
                return;
            }

            saveTask();
        });

    }

    private void getUserData() {
        String nameUser = preferences.getString(USER_PREFERENCES_NAME, "");
        String lastnameUser = preferences.getString(USER_PREFERENCES_LAST_NAME, "");
        String patronymicUser = preferences.getString(USER_PREFERENCES_PATRONYMIC, "");
        emailCreator = preferences.getString(USER_PREFERENCES_EMAIL, "");
        fullNameCreator = nameUser + " " + lastnameUser + " " + patronymicUser;
    }

    private void saveTask() {
        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(this
                .getApplication(), location)).get(TaskViewModel.class);

        String image;

        if (fileUri != null)
            image = taskViewModel.uploadImage(fileUri, AddTaskRootActivity.this);
        else
            image = null;

        String dateCreate = utils.getDate();
        String timeCreate = utils.getTime();

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        Task task = new Task(taskName, address, dateCreate, floor, cabinet, letter, comment,
                dateComplete, emailExecutor, taskStatus, timeCreate, emailCreator, urgent,
                image, fullNameExecutor, fullNameCreator);

        taskViewModel.createTask(task);

        onBackPressed();
    }

    void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> saveTask())
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> onBackPressed());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initFields(String location) {

        if (location.equals(OST_SCHOOL)) {

            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            binding.addressAutoComplete.setAdapter(adapter);

        }

        if (location.equals(BAR_SCHOOL))
            requireNonNull(binding.taskAddress.getEditText()).setText(getText(R.string.bar_school_address));

        String[] floors_basic_school = getResources().getStringArray(R.array.floors);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                floors_basic_school
        );
        binding.floorAutoComplete.setAdapter(arrayAdapter);

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        binding.statusAutoComplete.setAdapter(adapter_status);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapterLetter = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        binding.letterAutoComplete.setAdapter(adapterLetter);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDate();
        };

        binding.taskDateComplete.getEditText().setOnClickListener(v -> new DatePickerDialog(AddTaskRootActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

        binding.taskCabinet.getEditText().addTextChangedListener(
                new TextValidator(binding.taskCabinet.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, binding.taskCabinet, binding.doneBtn, 3);
            }
        });

    }

    void setDate() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        requireNonNull(binding.taskDateComplete.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clearError(binding.taskAddress);
        utils.clearError(binding.taskFloor);
        utils.clearError(binding.taskCabinet);
        utils.clearError(binding.taskName);
        utils.clearError(binding.taskDateComplete);
        utils.clearError(binding.taskEmailExecutor);
        utils.clearError(binding.taskStatus);
        utils.clearError(binding.taskNameExecutor);

        boolean checkAddress = utils.validateField(address, binding.taskAddress);
        boolean checkNameTask = utils.validateField(taskName, binding.taskName);
        boolean checkFloor = utils.validateField(floor, binding.taskFloor);
        boolean checkCabinet = utils.validateField(cabinet, binding.taskCabinet);
        boolean checkDateTask = utils.validateField(dateComplete, binding.taskDateComplete);
        boolean checkExecutor = utils.validateField(emailExecutor, binding.taskEmailExecutor);
        boolean checkStatus = utils.validateField(taskStatus, binding.taskStatus);
        boolean checkNameExecutor = utils.validateField(fullNameExecutor, binding.taskNameExecutor);

        return checkAddress & checkFloor & checkCabinet & checkNameTask & checkDateTask & checkExecutor & checkStatus & checkNameExecutor;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_GALLERY_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPictureLauncher.launch("image/*");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle(getText(R.string.warning))
                            .setMessage(getString(R.string.permission_gallery))
                            .setPositiveButton("Настройки", (dialog, id) ->
                                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null))))
                            .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel());

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                break;

            case PERMISSION_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File file = new File(getFilesDir(), "picFromCamera");
                    fileUri = FileProvider.getUriForFile(
                            this,
                            getApplicationContext().getPackageName() + ".provider",
                            file);
                    cameraLauncher.launch(fileUri);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle(getText(R.string.warning))
                            .setMessage(getString(R.string.permission_camera))
                            .setPositiveButton("Настройки", (dialog, id) ->
                                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null))))
                            .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel());

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                break;
        }

    }

    @Override
    public void getPhotoFromDevice(String button) {
        if (button.equals("new photo")) {
            ActivityCompat.requestPermissions(
                    AddTaskRootActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);
            addImage.dismiss();
        }

        if (button.equals("existing photo")) {
            ActivityCompat.requestPermissions(
                    AddTaskRootActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);
            addImage.dismiss();
        }
    }
}