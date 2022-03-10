package com.george.vector.users.user.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_PATRONYMIC;
import static com.george.vector.common.consts.Logs.TAG_SAVE_TASK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.george.vector.R;
import com.george.vector.common.tasks.ui.BottomSheetAddImage;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityAddTaskUserBinding;
import com.george.vector.notifications.SendNotification;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddTaskUserActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener {

    private static final String TAG = "AddTaskUser";

    String address, floor, cabinet, letter, name_task, comment, email, permission, nameImage, fullNameCreator;
    String status = "Новая заявка";

    SharedPreferences mDataUser;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private Uri fileUri;

    Utils utils = new Utils();

    ActivityAddTaskUserBinding binding;

    ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                binding.imageTaskUser.setImageURI(fileUri);
            });

    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    binding.imageTaskUser.setImageURI(fileUri);
                }
            });

    BottomSheetAddImage addImage = new BottomSheetAddImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle arguments = getIntent().getExtras();
        permission = arguments.getString(PERMISSION);
        Log.i(TAG_SAVE_TASK, String.format("permission: %s", permission));

        mDataUser = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        String name_user = mDataUser.getString(USER_PREFERENCES_NAME, "");
        String last_name_user = mDataUser.getString(USER_PREFERENCES_LAST_NAME, "");
        String patronymic_user = mDataUser.getString(USER_PREFERENCES_PATRONYMIC, "");
        fullNameCreator = name_user + " " + last_name_user + " " + patronymic_user;

        binding.topAppBarNewTaskUser.setNavigationOnClickListener(v -> onBackPressed());

        binding.crateTask.setOnClickListener(v -> {
            address = Objects.requireNonNull(binding.textInputLayoutAddress.getEditText()).getText().toString();
            floor = Objects.requireNonNull(binding.textInputLayoutFloor.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(binding.textInputLayoutCabinet.getEditText()).getText().toString();
            letter = Objects.requireNonNull(binding.textInputLayoutCabinetLiterUser.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(binding.textInputLayoutNameTask.getEditText()).getText().toString();
            comment = Objects.requireNonNull(binding.textInputLayoutComment.getEditText()).getText().toString();

            if (validateFields()) {
                if (!isOnline())
                    showDialogNoInternet();
                else
                    saveTask(permission);
            }

        });

        binding.cardImage.setOnClickListener(v -> showDialogImage());

        initializeField(permission);
    }

    void saveTask(@NotNull String location) {
        Task task = new Task();

        uploadImage();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_create = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time_create = timeFormat.format(currentDate);

        task.save(new SaveTask(), location, name_task, address, date_create, floor, cabinet, letter, comment,
                null, null, status, time_create, email, false, nameImage, null, fullNameCreator);

        SendNotification sendNotification = new SendNotification();
        sendNotification.sendNotification("Созданна новая заявка", name_task, TOPIC_NEW_TASKS_CREATE);

    }

    void uploadImage() {
        if (isOnline()) {
            if (fileUri != null) {
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();

                final ProgressDialog progressDialog = new ProgressDialog(AddTaskUserActivity.this);
                progressDialog.setTitle("Загрузка...");
                progressDialog.show();

                nameImage = UUID.randomUUID().toString();

                StorageReference ref = storageReference.child("images/" + nameImage);
                ref.putBytes(data)
                        .addOnSuccessListener(taskSnapshot -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "Изображение успешно загружено");
                            onBackPressed();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.e(TAG, "Failed: " + e.getMessage());
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Загрузка: " + (int) progress + "%");
                        });
            } else {
                onBackPressed();
            }
        } else {
            onBackPressed();
        }
    }

    void initializeField(String permission) {
        if (permission.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
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

        binding.textInputLayoutFloor.getEditText().addTextChangedListener(new TextValidator(binding.textInputLayoutFloor.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, binding.textInputLayoutFloor, binding.crateTask, 1);
            }
        });

        binding.textInputLayoutCabinet.getEditText().addTextChangedListener(new TextValidator(binding.textInputLayoutCabinet.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, binding.textInputLayoutCabinet, binding.crateTask, 3);
            }
        });

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

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    boolean validateFields() {
        utils.clear_error(binding.textInputLayoutAddress);
        utils.clear_error(binding.textInputLayoutFloor);
        utils.clear_error(binding.textInputLayoutCabinet);
        utils.clear_error(binding.textInputLayoutNameTask);

        boolean check_address = utils.validate_field(address, binding.textInputLayoutAddress);
        boolean check_floor = utils.validate_field(floor, binding.textInputLayoutFloor);
        boolean check_cabinet = utils.validate_field(cabinet, binding.textInputLayoutCabinet);
        boolean check_name_task = utils.validate_field(name_task, binding.textInputLayoutNameTask);

        return check_address & check_floor & check_cabinet & check_name_task;
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