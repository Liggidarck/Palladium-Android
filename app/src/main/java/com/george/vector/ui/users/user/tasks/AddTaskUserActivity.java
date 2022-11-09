package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.utils.consts.Keys.PERMISSION_GALLERY_CODE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityAddTaskUserBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.common.tasks.BottomSheetAddImage;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

import java.io.File;
import java.util.Objects;

public class AddTaskUserActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    private String address, floor, cabinet, letter, nameTask, comment, zone;
    private long userId;

    private Uri fileUri;

    private final TextValidatorUtils textValidator = new TextValidatorUtils();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private final TimeUtils timeUtils = new TimeUtils();
    private ActivityAddTaskUserBinding binding;

    private final ActivityResultLauncher<String> selectPictureLauncher
            = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageViewTaskUser.setImageURI(fileUri);
            });

    private final ActivityResultLauncher<Uri> cameraLauncher
            = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageViewTaskUser.setImageURI(fileUri);
                }
            });

    private final BottomSheetAddImage addImage = new BottomSheetAddImage();

    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        zone = userDataViewModel.getUser().getZone();
        userId = userDataViewModel.getId();

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(TaskViewModel.class);


        binding.toolbarAddTask.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnCreateTask.setOnClickListener(v -> {
            address = Objects.requireNonNull(binding.textInputAddressUser.getEditText()).getText().toString();
            floor = Objects.requireNonNull(binding.textInputFloorUser.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(binding.textInputCabinetUser.getEditText()).getText().toString();
            letter = Objects.requireNonNull(binding.textInputLetterUser.getEditText()).getText().toString();
            nameTask = Objects.requireNonNull(binding.textInputNameUser.getEditText()).getText().toString();
            comment = Objects.requireNonNull(binding.textInputCommentUser.getEditText()).getText().toString();

            if (!validateFields()) {
                return;
            }

            if (!networkUtils.isOnline(AddTaskUserActivity.this)) {
                showDialogNoInternet();
                return;
            }

            saveTask();
        });

        binding.materialCardViewImage.setOnClickListener(v -> showDialogImage());

        initializeField(zone);
    }

    void saveTask() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Ваша заявка сохраняется...");
        progressDialog.show();

        String dateCreate = timeUtils.getDate() + " " + timeUtils.getTime();

        if (comment.isEmpty())
            comment = "Нет коментария к заявке";

        Task task = new Task(zone, NEW_TASKS, nameTask, comment, address,
                floor, cabinet, letter, false, null, 0, (int) userId,
                dateCreate, null);

        taskViewModel.createTask(task).observe(this, response -> {
            if (response.getMessage().equals("Task successfully created!")) {
                Toast.makeText(this, "Заявка созданна", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                onBackPressed();
            }
        });
    }

    boolean validateFields() {
        return textValidator.isEmptyField(address, binding.textInputAddressUser) &
                textValidator.isEmptyField(floor, binding.textInputFloorUser) &
                textValidator.isEmptyField(cabinet, binding.textInputCabinetUser) &
                textValidator.isEmptyField(nameTask, binding.textInputNameUser);
    }

    void initializeField(String permission) {
        if (permission.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            binding.autoCompleteAddressUser.setAdapter(adapter);
        }

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                AddTaskUserActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        binding.autoCompleteLetterUser.setAdapter(adapter_letter);


        if (permission.equals(BAR_SCHOOL))
            Objects.requireNonNull(binding.textInputAddressUser.getEditText())
                    .setText(getText(R.string.bar_school_address));

    }

    void showDialogImage() {
        addImage.show(getSupportFragmentManager(), "BottomSheetAddImage");
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
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
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
                .setMessage(getText(R.string.warning_no_connection_main))
                .setPositiveButton("Ок", (dialog, id) -> onBackPressed());

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