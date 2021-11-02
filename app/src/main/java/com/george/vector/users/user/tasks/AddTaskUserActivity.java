package com.george.vector.users.user.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.PERMISSION;
import static com.george.vector.common.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;
import static com.george.vector.common.consts.Keys.USERS;
import static com.george.vector.common.consts.Logs.TAG_SAVE_TASK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityAddTaskUserBinding;
import com.george.vector.notifications.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

public class AddTaskUserActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskUser";

    String address, floor, cabinet, letter, name_task, comment, userID, email, permission, name_image, full_name_creator;
    String status = "Новая заявка";

    FirebaseAuth firebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle arguments = getIntent().getExtras();
        permission = arguments.getString(PERMISSION);
        Log.i(TAG_SAVE_TASK, String.format("permission: %s", permission));

        binding.topAppBarNewTaskUser.setNavigationOnClickListener(v -> onBackPressed());

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection(USERS).document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
            String name_creator = value.getString("name");
            String last_name_creator = value.getString("last_name");
            String patronymic_creator = value.getString("patronymic");
            full_name_creator = last_name_creator + " " + name_creator + " " + patronymic_creator;
        });

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

        binding.imageTaskUser.setOnClickListener(v -> showDialogImage());

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
                null, null, status, time_create, email, false, name_image, null, full_name_creator);

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

                name_image = UUID.randomUUID().toString();

                StorageReference ref = storageReference.child("images/" + name_image);
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
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_image);

        RelativeLayout camera_btn = dialog.findViewById(R.id.layout_new_photo);
        RelativeLayout gallery_btn = dialog.findViewById(R.id.layout_folder);

        camera_btn.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(
                    AddTaskUserActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);

            dialog.dismiss();
        });

        gallery_btn.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(
                    AddTaskUserActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);

            dialog.dismiss();
        });

        dialog.show();
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
                            .setMessage("Для того, чтобы загрузить изображение необходимо разрешить Palladium доступ файловому хранилищу")
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
                            .setMessage("Для того, чтобы загрузить изображение необходимо разрешить Palladium доступ к камере")
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
}