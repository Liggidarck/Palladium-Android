package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.OST_AIST;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_YAGODKA;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_GALLERY_CODE;
import static java.util.Objects.requireNonNull;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.DialogsUtils;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.common.utils.TimeUtils;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.tasks.BottomSheetAddImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskRootActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    private ActivityAddTaskRootBinding binding;
    private TaskViewModel taskViewModel;
    private UserDataViewModel userPrefViewModel;

    String address, floor, cabinet, letter, taskName, dateComplete, taskStatus,
            comment, fullNameExecutor, emailExecutor, collection, emailCreator, fullNameCreator;
    boolean urgent;
    private Uri fileUri;

    private final ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageViewTask.setImageURI(fileUri);
            });

    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageViewTask.setImageURI(fileUri);
                }
            });

    private final BottomSheetAddImage addImage = new BottomSheetAddImage();
    private Calendar datePickCalendar;

    private final TextValidatorUtils textValidatorUtils = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final TimeUtils timeUtils = new TimeUtils();
    private final DialogsUtils dialogsUtils = new DialogsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        collection = arguments.get(COLLECTION).toString();

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(), collection)
        ).get(TaskViewModel.class);
        userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        getUserData();
        initFields(collection);

        binding.toolbarAddEditTask.setNavigationOnClickListener(v -> onBackPressed());

        binding.addExecutorBtn.setOnClickListener(v ->
                dialogsUtils.showAddExecutorDialog(AddTaskRootActivity.this, binding.taskEmailExecutor,
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

            if (!networkUtils.isOnline(AddTaskRootActivity.this)) {
                showDialogNoInternet();
                return;
            }

            saveTask();
        });

    }

    private void saveTask() {
        String image;

        if (fileUri != null)
            image = taskViewModel.uploadImage(fileUri, AddTaskRootActivity.this);
        else
            image = null;

        String dateCreate = timeUtils.getDate();
        String timeCreate = timeUtils.getTime();

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        Task task = new Task(taskName, address, dateCreate, floor, cabinet, letter, comment,
                dateComplete, emailExecutor, taskStatus, timeCreate, emailCreator, urgent,
                image, fullNameExecutor, fullNameCreator);

        taskViewModel.createTask(task);

        onBackPressed();
    }

    private void getUserData() {
        String nameUser = userPrefViewModel.getUser().getName();
        String lastnameUser = userPrefViewModel.getUser().getLast_name();
        String patronymicUser = userPrefViewModel.getUser().getPatronymic();
        emailCreator = userPrefViewModel.getUser().getEmail();
        fullNameCreator = lastnameUser + " " + nameUser + " " + patronymicUser;
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

        if (location.equals(OST_AIST))
            binding.taskAddress.getEditText().setText(getString(R.string.ost_aist_address));

        if (location.equals(OST_YAGODKA))
            binding.taskAddress.getEditText().setText(getString(R.string.ost_yagodka_address));

        if (location.equals(BAR_SCHOOL))
            binding.taskAddress.getEditText().setText(getString(R.string.bar_school_address));

        if (location.equals(BAR_RUCHEEK))
            binding.taskAddress.getEditText().setText(getString(R.string.bar_rucheek_address));

        if (location.equals(BAR_ZVEZDOCHKA))
            binding.taskAddress.getEditText().setText(getString(R.string.bar_zvezdochka_address));

        String[] floors = getResources().getStringArray(R.array.floors);
        ArrayAdapter<String> floorsAdapter = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                floors
        );
        binding.floorAutoComplete.setAdapter(floorsAdapter);

        String[] itemsStatus = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsStatus
        );

        binding.statusAutoComplete.setAdapter(statusAdapter);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> letterAdapter = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        binding.letterAutoComplete.setAdapter(letterAdapter);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDate();
        };

        binding.taskDateComplete.getEditText().setOnClickListener(v ->
                new DatePickerDialog(AddTaskRootActivity.this, date,
                        datePickCalendar.get(Calendar.YEAR),
                        datePickCalendar.get(Calendar.MONTH),
                        datePickCalendar.get(Calendar.DAY_OF_MONTH))
                        .show()
        );

    }

    void setDate() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat format = new SimpleDateFormat(date_text, Locale.US);

        requireNonNull(binding.taskDateComplete.getEditText()).setText(format.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        return textValidatorUtils.isEmptyField(address, binding.taskAddress) &
                textValidatorUtils.isEmptyField(floor, binding.taskFloor) &
                textValidatorUtils.isEmptyField(cabinet, binding.taskCabinet) &
                textValidatorUtils.isEmptyField(taskName, binding.taskName) &
                textValidatorUtils.isEmptyField(dateComplete, binding.taskDateComplete) &
                textValidatorUtils.isEmptyField(emailExecutor, binding.taskEmailExecutor) &
                textValidatorUtils.isEmptyField(taskStatus, binding.taskStatus) &
                textValidatorUtils.isEmptyField(fullNameExecutor, binding.taskNameExecutor) &
                textValidatorUtils.validateNumberField(cabinet, binding.taskCabinet, 3);
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