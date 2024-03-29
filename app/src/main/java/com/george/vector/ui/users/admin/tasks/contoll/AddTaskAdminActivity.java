package com.george.vector.ui.users.admin.tasks.contoll;

import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.OST_AIST;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_YAGODKA;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static java.util.Objects.requireNonNull;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.common.utils.TimeUtils;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.UserAdapter;
import com.george.vector.ui.common.tasks.BottomSheetAddImage;
import com.george.vector.ui.users.admin.tasks.navigation.AllTasksAdminActivity;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddTaskAdminActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    private ActivityAddTaskRootBinding binding;

    private TaskViewModel taskViewModel;
    private UserViewModel userViewModel;

    private String address, floor, cabinet, letter, taskName, dateComplete, taskStatus,
            comment, zone, executorName;

    private int executorId;
    private int userId;

    boolean urgent;

    public static final String TAG = AddTaskAdminActivity.class.getSimpleName();

    private Uri fileUri;

    private final ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageViewTask.setImageURI(fileUri);
                Toast.makeText(this, "Ваше изображение отображено в коне формы заявки", Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageViewTask.setImageURI(fileUri);
                    Toast.makeText(this, "Ваше изображение отображено в коне формы заявки", Toast.LENGTH_SHORT).show();
                }
            });

    private final BottomSheetAddImage addImage = new BottomSheetAddImage();
    private Calendar datePickCalendar;

    private final TextValidatorUtils textValidatorUtils = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final TimeUtils timeUtils = new TimeUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        zone = arguments.getString(ZONE);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userPrefViewModel.getToken())
        ).get(TaskViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userPrefViewModel.getToken()
        )).get(UserViewModel.class);

        userId = (int) userPrefViewModel.getId();

        initFields(zone);

        binding.toolbarAddEditTask.setNavigationOnClickListener(v -> onBackPressed());

        binding.addExecutorBtn.setOnClickListener(v -> showAddExecutorDialog());

        binding.addImageBtn.setOnClickListener(v ->
                addImage.show(getSupportFragmentManager(), "BottomSheetAddImage"));

        binding.doneBtn.setOnClickListener(v -> {
            address = requireNonNull(binding.taskAddress.getEditText()).getText().toString();
            floor = requireNonNull(binding.taskFloor.getEditText()).getText().toString();
            cabinet = requireNonNull(binding.taskCabinet.getEditText()).getText().toString();
            letter = requireNonNull(binding.taskLetter.getEditText()).getText().toString();
            taskName = requireNonNull(binding.taskName.getEditText()).getText().toString();
            comment = requireNonNull(binding.taskComment.getEditText()).getText().toString();
            dateComplete = requireNonNull(binding.taskDateComplete.getEditText()).getText().toString();
            taskStatus = requireNonNull(binding.taskStatus.getEditText()).getText().toString();
            urgent = binding.urgentCheckBox.isChecked();

            if (!validateFields()) {
                return;
            }

            if (!networkUtils.isOnline(AddTaskAdminActivity.this)) {
                showDialogNoInternet();
                return;
            }

            saveTask();
        });

    }

    private void saveTask() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Ваша заявка сохраняется...");
        progressDialog.show();

        String image = null;

        String dateCreate = timeUtils.getDate() + " " + timeUtils.getTime();

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        Task task = new Task(zone, NEW_TASKS, taskName, comment, address, floor,
                cabinet, letter, urgent, dateComplete, executorId, userId, dateCreate, image);

        taskViewModel.createTask(task).observe(this, message -> {
            Log.d(TAG, "saveTask: " + message.getMessage());

            if (message.getMessage().equals("Task successfully created!")) {
                startListTasks();
                progressDialog.dismiss();
            } else {
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    private void startListTasks() {
        Intent intent = new Intent(this, AllTasksAdminActivity.class);
        intent.putExtra(ZONE, zone);
        intent.putExtra(STATUS, NEW_TASKS);
        intent.putExtra(IS_EXECUTE, false);
        startActivity(intent);
        finish();
    }

    void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection_main))
                .setPositiveButton("Ок", (dialog, id) -> onBackPressed());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initFields(String location) {

        if (location.equals(OST_SCHOOL)) {

            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskAdminActivity.this,
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
                AddTaskAdminActivity.this,
                R.layout.dropdown_menu_categories,
                floors
        );
        binding.floorAutoComplete.setAdapter(floorsAdapter);

        binding.taskStatus.getEditText().setText("Новая заявка");

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> letterAdapter = new ArrayAdapter<>(
                AddTaskAdminActivity.this,
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
                new DatePickerDialog(AddTaskAdminActivity.this, date,
                        datePickCalendar.get(Calendar.YEAR),
                        datePickCalendar.get(Calendar.MONTH),
                        datePickCalendar.get(Calendar.DAY_OF_MONTH))
                        .show()
        );

    }

    void setDate() {
        String dateText = "dd.MM.yyyy";
        SimpleDateFormat format = new SimpleDateFormat(dateText, Locale.US);

        requireNonNull(binding.taskDateComplete.getEditText()).setText(format.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        return textValidatorUtils.isEmptyField(address, binding.taskAddress) &
                textValidatorUtils.isEmptyField(floor, binding.taskFloor) &
                textValidatorUtils.isEmptyField(cabinet, binding.taskCabinet) &
                textValidatorUtils.isEmptyField(taskName, binding.taskName) &
                textValidatorUtils.isEmptyField(dateComplete, binding.taskDateComplete) &
                textValidatorUtils.isEmptyField(taskStatus, binding.taskStatus) &
                textValidatorUtils.isEmptyField(executorName, binding.taskNameExecutor) &
                textValidatorUtils.validateNumberField(cabinet, binding.taskCabinet, 3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                    AddTaskAdminActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);
            addImage.dismiss();
        }

        if (button.equals("existing photo")) {
            ActivityCompat.requestPermissions(
                    AddTaskAdminActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);
            addImage.dismiss();
        }
    }

    private void showAddExecutorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recyclerViewListExecutors = dialog.findViewById(R.id.recyclerViewListExecutors);
        Chip chipAdminDialog = dialog.findViewById(R.id.chip_root_dialog);
        Chip chipExecutorsDialog = dialog.findViewById(R.id.chip_executors_dialog);
        Chip chipDeveloperDialog = dialog.findViewById(R.id.chip_developer_dialog);

        UserAdapter userAdapter = new UserAdapter();

        setUsers(userAdapter,"ROLE_ADMIN");

        recyclerViewListExecutors.setHasFixedSize(true);
        recyclerViewListExecutors.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListExecutors.setAdapter(userAdapter);

        chipAdminDialog.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUsers(userAdapter, "ROLE_ADMIN");
            }
        });

        chipExecutorsDialog.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUsers(userAdapter, "ROLE_EXECUTOR");
            }
        });

        chipDeveloperDialog.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                userAdapter.clearUsers();
                setUsers(userAdapter, "ROLE_DEVELOPER");
            }
        });

        userAdapter.setOnItemClickListener((executor, position) -> {
            executorId = (int) executor.getId();
            executorName = executor.getLastName() + " " + executor.getName() + " " + executor.getPatronymic();
            Objects.requireNonNull(binding.taskNameExecutor.getEditText()).setText(executorName);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setUsers(UserAdapter userAdapter, String role) {
        userViewModel.getUsersByRoleName(role).observe(this, users -> {
            if(users == null) {
                return;
            }

            userAdapter.setUsers(users);
        });
    }

}