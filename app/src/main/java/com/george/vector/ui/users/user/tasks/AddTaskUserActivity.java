package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.PERMISSION;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.utils.consts.Logs.TAG_ADD_TASK_USER_ACTIVITY;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.common.utils.TextValidatorUtils;
import com.george.vector.common.utils.TimeUtils;
import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.ActivityAddTaskUserBinding;
import com.george.vector.network.model.Task;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.network.viewmodel.ViewModelFactory;
import com.george.vector.ui.tasks.BottomSheetAddImage;

import java.io.File;
import java.util.Objects;

public class AddTaskUserActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    String address, floor, cabinet, letter, nameTask, comment, email, permission, fullNameCreator;
    final String status = "Новая заявка";

    private Uri fileUri;

    private final TextValidatorUtils textValidator = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final TimeUtils timeUtils = new TimeUtils();
    private ActivityAddTaskUserBinding binding;

    final ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageTaskUser.setImageURI(fileUri);
            });

    final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageTaskUser.setImageURI(fileUri);
                }
            });

    final BottomSheetAddImage addImage = new BottomSheetAddImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        permission = arguments.getString(PERMISSION);
        email = arguments.getString(EMAIL);

        Log.i(TAG_ADD_TASK_USER_ACTIVITY, "permission: " + permission);
        Log.d(TAG_ADD_TASK_USER_ACTIVITY, "email: " + email);

        UserPreferencesViewModel userPrefViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);
        String nameUser = userPrefViewModel.getUser().getName();
        String lastNameUser = userPrefViewModel.getUser().getLast_name();
        String patronymicUser = userPrefViewModel.getUser().getPatronymic();
        fullNameCreator = nameUser + " " + lastNameUser + " " + patronymicUser;

        binding.topAppBarNewTaskUser.setNavigationOnClickListener(v -> onBackPressed());

        binding.crateTask.setOnClickListener(v -> {
            address = Objects.requireNonNull(binding.textInputLayoutAddress.getEditText()).getText().toString();
            floor = Objects.requireNonNull(binding.textInputLayoutFloor.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(binding.textInputLayoutCabinet.getEditText()).getText().toString();
            letter = Objects.requireNonNull(binding.textInputLayoutCabinetLiterUser.getEditText()).getText().toString();
            nameTask = Objects.requireNonNull(binding.textInputLayoutNameTask.getEditText()).getText().toString();
            comment = Objects.requireNonNull(binding.textInputLayoutComment.getEditText()).getText().toString();

            if(!validateFields()) {
                return;
            }

            if (!networkUtils.isOnline(AddTaskUserActivity.this)) {
                showDialogNoInternet();
                return;
            }

            saveTask(permission);
        });

        binding.cardImage.setOnClickListener(v -> showDialogImage());

        initializeField(permission);
    }

    void saveTask(String location) {
        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(this.getApplication(),
                location)).get(TaskViewModel.class);

        String image;

        if (fileUri != null)
            image = taskViewModel.uploadImage(fileUri, AddTaskUserActivity.this);
        else
            image = null;

        String dateCreate = timeUtils.getDate();
        String timeCreate = timeUtils.getTime();

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        Task task = new Task(nameTask, address, dateCreate, floor, cabinet, letter, comment,
                null, null, status, timeCreate, email, false, image,
                null, fullNameCreator);

        taskViewModel.createTask(task);
        onBackPressed();
    }

    boolean validateFields() {
        return textValidator.isEmptyField(address, binding.textInputLayoutAddress) &
                textValidator.isEmptyField(floor, binding.textInputLayoutFloor) &
                textValidator.isEmptyField(cabinet, binding.textInputLayoutCabinet) &
                textValidator.isEmptyField(nameTask, binding.textInputLayoutNameTask);
    }

    void initializeField(String permission) {
        if (permission.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            binding.addressAutoComplete.setAdapter(adapter);
        }

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                AddTaskUserActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        binding.literAutoCompleteUser.setAdapter(adapter_letter);


        if (permission.equals(BAR_SCHOOL))
            Objects.requireNonNull(binding.textInputLayoutAddress.getEditText()).setText(getText(R.string.bar_school_address));

    }

    void showDialogImage() {
        addImage.show(getSupportFragmentManager(), "BottomSheetAddImage");
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
                            .setPositiveButton("Настройки", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
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

    void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> saveTask(permission))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> onBackPressed());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void getPhotoFromDevice(String button) {
        if (button.equals("new photo")) {
            ActivityCompat.requestPermissions(
                    AddTaskUserActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);

            addImage.dismiss();
        }

        if (button.equals("existing photo")) {
            ActivityCompat.requestPermissions(
                    AddTaskUserActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);

            addImage.dismiss();
        }
    }
}