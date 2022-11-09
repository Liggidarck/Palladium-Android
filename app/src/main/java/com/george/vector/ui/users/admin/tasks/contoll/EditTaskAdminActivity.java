package com.george.vector.ui.users.admin.tasks.contoll;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
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
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

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
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.adapter.UserAdapter;
import com.george.vector.ui.common.tasks.BottomSheetAddImage;
import com.george.vector.ui.users.admin.main.MainAdminActivity;
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

public class EditTaskAdminActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    private ActivityAddTaskRootBinding binding;

    private String comment, dateCreate, zone;
    private long taskId;
    private long executorId;
    private long creatorId;
    private boolean executed;

    private Calendar datePickCalendar;
    private Uri fileUri;

    private TaskViewModel taskViewModel;
    private UserViewModel userViewModel;

    private final TextValidatorUtils textValidatorUtils = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final BottomSheetAddImage addImage = new BottomSheetAddImage();

    private final ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                fileUri = uri;
                binding.imageViewTask.setImageURI(fileUri);
                Snackbar.make(binding.addEditTaskCoordinator, R.string.add_image_to_end_text,
                        Snackbar.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), result -> {
                if (result) {
                    binding.imageViewTask.setImageURI(fileUri);
                    Snackbar.make(binding.addEditTaskCoordinator, R.string.add_image_to_end_text,
                            Snackbar.LENGTH_SHORT).show();
                }
            });


    public static final String TAG = EditTaskAdminActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        taskId = arguments.getLong(ID);
        zone = arguments.getString(ZONE);
        executed = arguments.getBoolean(IS_EXECUTE);

        initViewModels();
        getTask();

        binding.doneBtn.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }

            if (!networkUtils.isOnline(EditTaskAdminActivity.this)) {
                showDialog();
                return;
            }

            updateTask(zone);
        });

        binding.toolbarAddEditTask.setNavigationOnClickListener(v -> onBackPressed());
        binding.addExecutorBtn.setOnClickListener(v -> showAddExecutorDialog());
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
            String executorName = executor.getLastName() + " " + executor.getName() + " " + executor.getPatronymic();
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

    private void initViewModels() {
        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken())
        ).get(TaskViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken())
        ).get(UserViewModel.class);
    }

    private void getTask() {
        taskViewModel.getTaskById(taskId).observe(EditTaskAdminActivity.this, task -> {
            binding.progressBarAddEditTask.setVisibility(View.VISIBLE);

            comment = task.getComment();
            dateCreate = task.getDateCreate();
            executorId = task.getExecutorId();
            creatorId = task.getCreatorId();
            String status = task.getStatus();

            if (status.equals(NEW_TASKS))
                status = "Новая заявка";

            if (status.equals(IN_PROGRESS_TASKS))
                status = "Заявка в работе";

            if (status.equals(COMPLETED_TASKS))
                status = "Завершенная заявка";

            if (status.equals(ARCHIVE_TASKS))
                status = "Архив";

            requireNonNull(binding.taskAddress.getEditText()).setText(task.getAddress());
            requireNonNull(binding.taskFloor.getEditText()).setText(task.getFloor());
            requireNonNull(binding.taskCabinet.getEditText()).setText(task.getCabinet());
            requireNonNull(binding.taskLetter.getEditText()).setText(task.getLetter());
            requireNonNull(binding.taskName.getEditText()).setText(task.getName());
            requireNonNull(binding.taskDateComplete.getEditText()).setText(task.getDateDone());
            requireNonNull(binding.taskStatus.getEditText()).setText(status);
            binding.urgentCheckBox.setChecked(task.isUrgent());

            if (comment.equals("Нет коментария к заявке"))
                requireNonNull(binding.taskComment.getEditText()).setText("");
            else requireNonNull(binding.taskComment.getEditText()).setText(comment);

            userViewModel.getUserById(executorId).observe(this, user -> {
                String name = user.getLastName() + " " + user.getName() + " " + user.getPatronymic();
                binding.taskNameExecutor.getEditText().setText(name);
            });

            binding.progressBarAddEditTask.setVisibility(View.INVISIBLE);
            initializeFields(zone);
        });
    }

    void updateTask(String zone) {
        String updateAddress = requireNonNull(binding.taskAddress.getEditText()).getText().toString();
        String updateFloor = requireNonNull(binding.taskFloor.getEditText()).getText().toString();
        String updateCabinet = requireNonNull(binding.taskCabinet.getEditText()).getText().toString();
        String updateLetter = requireNonNull(binding.taskLetter.getEditText()).getText().toString();
        String updateName = requireNonNull(binding.taskName.getEditText()).getText().toString();
        String updateComment = requireNonNull(binding.taskComment.getEditText()).getText().toString();
        String updateDateDone = requireNonNull(binding.taskDateComplete.getEditText()).getText().toString();
        String updateStatus = requireNonNull(binding.taskStatus.getEditText()).getText().toString();
        boolean updateUrgent = binding.urgentCheckBox.isChecked();

        if (updateStatus.equals("Новая заявка"))
            updateStatus = NEW_TASKS;

        if (updateStatus.equals("Заявка в работе"))
            updateStatus = IN_PROGRESS_TASKS;

        if (updateStatus.equals("Завершенная заявка"))
            updateStatus = COMPLETED_TASKS;

        if (updateStatus.equals("Архив"))
            updateStatus = ARCHIVE_TASKS;

        Task task = new Task(zone, updateStatus, updateName, updateComment, updateAddress,
                updateFloor, updateCabinet, updateLetter, updateUrgent, updateDateDone,
                (int) executorId, (int) creatorId, dateCreate, null);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Ваша заявка обновляется...");
        progressDialog.show();

        String finalUpdateStatus = updateStatus;
        taskViewModel.editTask(task, taskId).observe(this, message -> {
            if (message.getMessage().equals("Task successfully edited")) {
                progressDialog.dismiss();
                startAllTasks(finalUpdateStatus);
            }
        });
    }

    private void startAllTasks(String status) {
        Intent intent = new Intent(this, AllTasksAdminActivity.class);
        intent.putExtra(ZONE, zone);
        intent.putExtra(STATUS, status);
        intent.putExtra(IS_EXECUTE, executed);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(zone))
                .setNegativeButton(android.R.string.cancel, (dialog, id) ->
                        startActivity(new Intent(this, MainAdminActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateFields() {
        String address = binding.taskAddress.getEditText().getText().toString();
        String floor = binding.taskFloor.getEditText().getText().toString();
        String cabinet = binding.taskCabinet.getEditText().getText().toString();
        String taskName = binding.taskName.getEditText().getText().toString();
        String dateComplete = binding.taskDateComplete.getEditText().getText().toString();
        String taskStatus = binding.taskStatus.getEditText().getText().toString();
        String fullNameExecutor = binding.taskNameExecutor.getEditText().getText().toString();

        return textValidatorUtils.isEmptyField(address, binding.taskAddress) &
                textValidatorUtils.isEmptyField(floor, binding.taskFloor) &
                textValidatorUtils.isEmptyField(cabinet, binding.taskCabinet) &
                textValidatorUtils.isEmptyField(taskName, binding.taskName) &
                textValidatorUtils.isEmptyField(dateComplete, binding.taskDateComplete) &
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

                    fileUri = FileProvider
                            .getUriForFile(this,
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

    private void initializeFields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditTaskAdminActivity.this, R.layout.dropdown_menu_categories, items);
            binding.addressAutoComplete.setAdapter(adapter);
        }

        String[] itemsStatus = getResources().getStringArray(R.array.status_name);
        ArrayAdapter<String> adapterStatus =
                new ArrayAdapter<>(EditTaskAdminActivity.this, R.layout.dropdown_menu_categories, itemsStatus);

        binding.statusAutoComplete.setAdapter(adapterStatus);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(EditTaskAdminActivity.this, R.layout.dropdown_menu_categories, itemsLetter);

        binding.letterAutoComplete.setAdapter(adapter_letter);

        String[] floors_basic_school = getResources().getStringArray(R.array.floors);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditTaskAdminActivity.this, R.layout.dropdown_menu_categories, floors_basic_school);
        binding.floorAutoComplete.setAdapter(arrayAdapter);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        binding.taskDateComplete
                .getEditText()
                .setOnClickListener(v ->
                        new DatePickerDialog(EditTaskAdminActivity.this,
                                date,
                                datePickCalendar.get(Calendar.YEAR),
                                datePickCalendar.get(Calendar.MONTH),
                                datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    private void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        requireNonNull(binding.taskDateComplete.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    @Override
    public void getPhotoFromDevice(String button) {
        if (button.equals("new photo")) {
            ActivityCompat.requestPermissions(EditTaskAdminActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);
            addImage.dismiss();
        }

        if (button.equals("existing photo")) {
            ActivityCompat.requestPermissions(EditTaskAdminActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);
            addImage.dismiss();
        }
    }
}