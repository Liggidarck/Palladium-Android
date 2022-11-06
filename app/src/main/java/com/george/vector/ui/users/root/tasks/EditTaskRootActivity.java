package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static java.util.Objects.requireNonNull;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import androidx.preference.PreferenceManager;
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
import com.george.vector.ui.users.root.main.MainRootActivity;
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

public class EditTaskRootActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    private ActivityAddTaskRootBinding binding;

    private String comment, dateCreate, zone, image;
    private long taskId;
    private long executorId;
    private long creatorId;

    private Calendar datePickCalendar;
    private Uri fileUri;

    private TaskViewModel taskViewModel;
    private UserViewModel userViewModel;

    private final TextValidatorUtils textValidatorUtils = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final BottomSheetAddImage addImage = new BottomSheetAddImage();

    private final ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageViewTask.setImageURI(fileUri);
                Snackbar.make(binding.addEditTaskCoordinator, "Ваше изображение отображено в коне формы заявки", Snackbar.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageViewTask.setImageURI(fileUri);
                    Snackbar.make(binding.addEditTaskCoordinator, "Ваше изображение отображено в коне формы заявки", Snackbar.LENGTH_SHORT).show();
                }
            });


    public static final String TAG = EditTaskRootActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        taskId = arguments.getLong(ID);
        zone = arguments.getString(ZONE);

        String bufferSizePreference = PreferenceManager
                .getDefaultSharedPreferences(EditTaskRootActivity.this)
                .getString("buffer_size", "2");
        int bufferSize = Integer.parseInt(bufferSizePreference);

        initViewModels();
        getTask(bufferSize);

        binding.doneBtn.setOnClickListener(v -> {
            if (!validateFields()) {
                return;
            }

            if (!networkUtils.isOnline(EditTaskRootActivity.this)) {
                showDialog();
                return;
            }

            updateTask(zone);
        });

        binding.toolbarAddEditTask.setNavigationOnClickListener(v -> onBackPressed());
        binding.addExecutorBtn.setOnClickListener(v -> {
            UserAdapter userAdapter = new UserAdapter();
            userViewModel.getAllUsers().observe(this, userAdapter::setUsers);
            showAddExecutorDialog(userAdapter);
        });
    }

    private void showAddExecutorDialog(UserAdapter userAdapter) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recyclerViewListExecutors = dialog.findViewById(R.id.recyclerViewListExecutors);
        Chip chipRootDialog = dialog.findViewById(R.id.chip_root_dialog);
        Chip chipExecutorsDialog = dialog.findViewById(R.id.chip_executors_dialog);

        recyclerViewListExecutors.setHasFixedSize(true);
        recyclerViewListExecutors.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListExecutors.setAdapter(userAdapter);

        userAdapter.setOnItemClickListener((executor, position) -> {
            executorId = (int) executor.getId();
            Objects.requireNonNull(binding.taskNameExecutor.getEditText()).setText(executor.getName());
            dialog.dismiss();
        });

        dialog.show();

    }

    private void initViewModels() {
        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(TaskViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(UserViewModel.class);
    }

    private void getTask(int bufferSize) {
        taskViewModel.getTaskById(taskId).observe(EditTaskRootActivity.this, task -> {
            binding.progressBarAddEditTask.setVisibility(View.VISIBLE);

            comment = task.getComment();
            dateCreate = task.getDateCreate();
            image = task.getImage();

            executorId = task.getExecutorId();
            creatorId = task.getCreatorId();

            requireNonNull(binding.taskAddress.getEditText()).setText(task.getAddress());
            requireNonNull(binding.taskFloor.getEditText()).setText(task.getFloor());
            requireNonNull(binding.taskCabinet.getEditText()).setText(task.getCabinet());
            requireNonNull(binding.taskLetter.getEditText()).setText(task.getLetter());
            requireNonNull(binding.taskName.getEditText()).setText(task.getName());
            requireNonNull(binding.taskDateComplete.getEditText()).setText(task.getDateDone());
            requireNonNull(binding.taskStatus.getEditText()).setText(task.getStatus());
            binding.urgentCheckBox.setChecked(task.isUrgent());

            if (comment.equals("Нет коментария к заявке"))
                requireNonNull(binding.taskComment.getEditText()).setText("");
            else
                requireNonNull(binding.taskComment.getEditText()).setText(comment);

            if (image == null) {
                binding.addImageBtn.setOnClickListener(v -> addImage.show(getSupportFragmentManager(), "BottomSheetAddImage"));
            } else {
                binding.addImageBtn.setEnabled(false);
                taskViewModel.setImage(image, binding.progressBarAddEditTask, binding.imageViewTask, bufferSize);
            }

            userViewModel.getUserById(executorId).observe(this, user -> {
                String name = user.getLastName() + " " + user.getName() + " " + user.getPatronymic();
                binding.taskNameExecutor.getEditText().setText(name);
            });

            binding.progressBarAddEditTask.setVisibility(View.INVISIBLE);
            initializeFields(zone);
        });
    }

    void updateTask(String zone) {
        String updateImage;

        if (fileUri != null)
            updateImage = taskViewModel.uploadImage(fileUri, EditTaskRootActivity.this);
        else
            updateImage = image;

        String updateAddress = requireNonNull(binding.taskAddress.getEditText()).getText().toString();
        String updateFloor = requireNonNull(binding.taskFloor.getEditText()).getText().toString();
        String updateCabinet = requireNonNull(binding.taskCabinet.getEditText()).getText().toString();
        String updateLetter = requireNonNull(binding.taskLetter.getEditText()).getText().toString();
        String updateName = requireNonNull(binding.taskName.getEditText()).getText().toString();
        String updateComment = requireNonNull(binding.taskComment.getEditText()).getText().toString();
        String updateDateDone = requireNonNull(binding.taskDateComplete.getEditText()).getText().toString();
        String updateStatus = requireNonNull(binding.taskStatus.getEditText()).getText().toString();
        boolean updateUrgent = binding.urgentCheckBox.isChecked();

        Task task = new Task(zone, updateStatus, updateName, updateComment,
                updateAddress, updateFloor, updateCabinet, updateLetter,
                updateUrgent, updateDateDone, (int) executorId, (int) creatorId,
                dateCreate, updateImage);

        taskViewModel.editTask(task, taskId);

        startActivity(new Intent(this, MainRootActivity.class));
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(zone))
                .setNegativeButton(android.R.string.cancel, (dialog, id) ->
                        startActivity(new Intent(this, MainRootActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean validateFields() {
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

    @Override
    public void getPhotoFromDevice(String button) {
        if (button.equals("new photo")) {
            ActivityCompat.requestPermissions(
                    EditTaskRootActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);
            addImage.dismiss();
        }

        if (button.equals("existing photo")) {
            ActivityCompat.requestPermissions(
                    EditTaskRootActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);
            addImage.dismiss();
        }
    }
}