package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.PERMISSION_CAMERA_CODE;
import static com.george.vector.common.consts.Keys.PERMISSION_GALLERY_CODE;
import static com.george.vector.common.consts.Keys.USERS;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.databinding.ActivityAddTaskRootBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddTaskRootActivity extends AppCompatActivity {

    String location, user_id, email, address, floor, cabinet, letter, name_task, date_complete, status, comment,
            USER_EMAIL, NAME_IMAGE, full_name_executor, name_executor, last_name_executor, patronymic_executor, email_executor, full_name_creator;
    boolean urgent;
    private static final String TAG = "AddTaskRoot";

    FirebaseAuth firebase_auth;
    StorageReference storage_reference;
    FirebaseStorage firebase_storage;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = firebaseFirestore.collection(USERS);
    Query query;
    private Uri fileUri;

    Calendar datePickCalendar;
    Utils utils = new Utils();

    ActivityAddTaskRootBinding mBuilding;

    ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                fileUri = uri;
                mBuilding.imageTask.setImageURI(fileUri);
            });

    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    mBuilding.imageTask.setImageURI(fileUri);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBuilding = ActivityAddTaskRootBinding.inflate(getLayoutInflater());
        setContentView(mBuilding.getRoot());

        firebase_auth = FirebaseAuth.getInstance();
        firebase_storage = FirebaseStorage.getInstance();
        storage_reference = firebase_storage.getReference();

        mBuilding.topAppBarNewTaskRoot.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        location = arguments.get(LOCATION).toString();
        USER_EMAIL = arguments.get(EMAIL).toString();
        Log.d(TAG, location);

        initializeFields(location);

        user_id = Objects.requireNonNull(firebase_auth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection(USERS).document(user_id);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
            String name_creator = value.getString("name");
            String last_name_creator = value.getString("last_name");
            String patronymic_creator = value.getString("patronymic");
            full_name_creator = last_name_creator + " " + name_creator + " " + patronymic_creator;
        });

        mBuilding.addExecutorRoot.setOnClickListener(v -> showAddExecutorDialog());

        mBuilding.imageTask.setOnClickListener(v -> showDialogImage());

        mBuilding.doneTaskRoot.setOnClickListener(v -> {
            address = Objects.requireNonNull(mBuilding.textInputLayoutAddressRoot.getEditText()).getText().toString();
            floor = Objects.requireNonNull(mBuilding.textInputLayoutFloorRoot.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(mBuilding.textInputLayoutCabinetRoot.getEditText()).getText().toString();
            letter = Objects.requireNonNull(mBuilding.textInputLayoutCabinetLiterRoot.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(mBuilding.textInputLayoutNameTaskRoot.getEditText()).getText().toString();
            comment = Objects.requireNonNull(mBuilding.textInputLayoutCommentRoot.getEditText()).getText().toString();
            date_complete = Objects.requireNonNull(mBuilding.textInputLayoutDateTaskRoot.getEditText()).getText().toString();
            email_executor = Objects.requireNonNull(mBuilding.textInputLayoutExecutorRoot.getEditText()).getText().toString();
            status = Objects.requireNonNull(mBuilding.textInputLayoutStatusRoot.getEditText()).getText().toString();
            urgent = mBuilding.urgentRequestCheckBox.isChecked();
            full_name_executor = mBuilding.textInputLayoutFullNameExecutorRoot.getEditText().getText().toString();

            if (validateFields()) {
                if (!isOnline())
                    showDialogNoInternet();
                else
                    saveTask(location);
            }

        });

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

    private void uploadImage() {
        if (fileUri != null) {
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                Log.d(TAG, "Bitmap: " + bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            Log.d(TAG, "Bitmap: " + bmp);
            byte[] data = byteArrayOutputStream.toByteArray();
            Log.d(TAG, "Data: " + Arrays.toString(data));

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Загрузка...");
            progressDialog.show();

            NAME_IMAGE = UUID.randomUUID().toString();

            StorageReference ref = storage_reference.child("images/" + NAME_IMAGE);
            ref.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        onBackPressed();
                    })
                    .addOnFailureListener(e -> progressDialog.dismiss())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Прогресс: " + (int) progress + "%");
                    });
        } else {
            NAME_IMAGE = null;
            onBackPressed();
        }
    }

    void saveTask(String location) {
        Task task = new Task();

        uploadImage();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_create = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time_create = timeFormat.format(currentDate);

        task.save(new SaveTask(), location, name_task, address, date_create, floor, cabinet, letter, comment,
                date_complete, email_executor, status, time_create, email, urgent, NAME_IMAGE, full_name_executor, full_name_creator);
    }

    void showAddExecutorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recycler_view_list_executors = dialog.findViewById(R.id.recycler_view_list_executors);
        Chip chip_root_dialog = dialog.findViewById(R.id.chip_root_dialog);
        Chip chip_executors_dialog = dialog.findViewById(R.id.chip_executors_dialog);

        query = usersRef.whereEqualTo("role", "Root");
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        UserAdapter adapter = new UserAdapter(options);

        recycler_view_list_executors.setHasFixedSize(true);
        recycler_view_list_executors.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_list_executors.setAdapter(adapter);

        chip_root_dialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "root checked");

                query = usersRef.whereEqualTo("role", "Root");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        chip_executors_dialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "Executor checked");

                query = usersRef.whereEqualTo("role", "Исполнитель");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(id);
            documentReference.addSnapshotListener((value, error) -> {
                assert value != null;
                name_executor = value.getString("name");
                last_name_executor = value.getString("last_name");
                patronymic_executor = value.getString("patronymic");
                email_executor = value.getString("email");
                full_name_executor = String.format("%s %s %s", last_name_executor, name_executor, patronymic_executor);

                Log.i(TAG, String.format("name: %s", name_executor));
                Log.i(TAG, String.format("last_name: %s", last_name_executor));
                Log.i(TAG, String.format("patronymic: %s", patronymic_executor));
                Log.i(TAG, String.format("email: %s", email_executor));

                Objects.requireNonNull(mBuilding.textInputLayoutExecutorRoot.getEditText()).setText(email_executor);
                Objects.requireNonNull(mBuilding.textInputLayoutFullNameExecutorRoot.getEditText()).setText(full_name_executor);

                adapter.stopListening();
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void showDialogImage() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_image);

        RelativeLayout camera_btn = dialog.findViewById(R.id.layout_new_photo);
        RelativeLayout gallery_btn = dialog.findViewById(R.id.layout_folder);

        camera_btn.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(
                    AddTaskRootActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_CAMERA_CODE);

            dialog.dismiss();
        });

        gallery_btn.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(
                    AddTaskRootActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, PERMISSION_GALLERY_CODE);

            dialog.dismiss();
        });

        dialog.show();
    }

    void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> saveTask(location))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> onBackPressed());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initializeFields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            mBuilding.addressAutoCompleteRoot.setAdapter(adapter);
        }

        if (location.equals(BAR_SCHOOL))
            Objects.requireNonNull(mBuilding.textInputLayoutAddressRoot.getEditText()).setText(getText(R.string.bar_school_address));

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        mBuilding.statusAutoCompleteRoot.setAdapter(adapter_status);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        mBuilding.literAutoCompleteRoot.setAdapter(adapter_letter);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDate();
        };

        mBuilding.editTextDateTaskRoot.setOnClickListener(v -> new DatePickerDialog(AddTaskRootActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

        mBuilding.textInputLayoutFloorRoot.getEditText().addTextChangedListener(new TextValidator(mBuilding.textInputLayoutFloorRoot.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, mBuilding.textInputLayoutFloorRoot, mBuilding.doneTaskRoot, 1);
            }
        });

        mBuilding.textInputLayoutCabinetRoot.getEditText().addTextChangedListener(new TextValidator(mBuilding.textInputLayoutCabinetRoot.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, mBuilding.textInputLayoutCabinetRoot, mBuilding.doneTaskRoot, 3);
            }
        });
    }

    void setDate() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(mBuilding.textInputLayoutDateTaskRoot.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(mBuilding.textInputLayoutAddressRoot);
        utils.clear_error(mBuilding.textInputLayoutFloorRoot);
        utils.clear_error(mBuilding.textInputLayoutCabinetRoot);
        utils.clear_error(mBuilding.textInputLayoutNameTaskRoot);
        utils.clear_error(mBuilding.textInputLayoutDateTaskRoot);
        utils.clear_error(mBuilding.textInputLayoutExecutorRoot);
        utils.clear_error(mBuilding.textInputLayoutStatusRoot);
        utils.clear_error(mBuilding.textInputLayoutFullNameExecutorRoot);

        boolean check_address = utils.validate_field(address, mBuilding.textInputLayoutAddressRoot);
        boolean check_name_task = utils.validate_field(name_task, mBuilding.textInputLayoutNameTaskRoot);
        boolean check_floor = utils.validate_field(floor, mBuilding.textInputLayoutFloorRoot);
        boolean check_cabinet = utils.validate_field(cabinet, mBuilding.textInputLayoutCabinetRoot);
        boolean check_date_task = utils.validate_field(date_complete, mBuilding.textInputLayoutDateTaskRoot);
        boolean check_executor = utils.validate_field(email_executor, mBuilding.textInputLayoutExecutorRoot);
        boolean check_status = utils.validate_field(status, mBuilding.textInputLayoutStatusRoot);
        boolean check_name_executor = utils.validate_field(full_name_executor, mBuilding.textInputLayoutFullNameExecutorRoot);

        return check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status & check_name_executor;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}