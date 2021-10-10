package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.consts.Keys.USERS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddTaskRootActivity extends AppCompatActivity {

    MaterialToolbar top_app_bar_new_task_root;
    LinearProgressIndicator progress_bar_add_task_root;
    ExtendedFloatingActionButton done_task_root;
    Button add_executor_root;
    CheckBox urgent_request_check_box;
    TextInputLayout text_input_layout_address_root, text_input_layout_floor_root,
            text_input_layout_cabinet_root, text_input_layout_name_task_root,
            text_input_layout_comment_root, text_input_layout_date_task_root,
            text_input_layout_executor_root, text_input_layout_status_root,
            text_input_layout_cabinet_liter_root;
    TextInputEditText edit_text_date_task_root;
    MaterialAutoCompleteTextView address_auto_complete_root, status_auto_complete_root, liter_auto_complete_root;
    ImageView image_task;

    String location, userID, email, address, floor, cabinet, letter, name_task, date_complete, status, comment,
            USER_EMAIL, NAME_IMAGE, full_name_executor, name_executor, last_name_executor, patronymic_executor, email_executor;
    boolean urgent;
    private static final String TAG = "AddTaskRoot";
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseAuth firebase_auth;
    StorageReference storage_reference;
    FirebaseStorage firebase_storage;

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = firebaseFirestore.collection(USERS);
    Query query;
    private Uri filePath;

    Calendar datePickCalendar;
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_root);

        top_app_bar_new_task_root = findViewById(R.id.topAppBar_new_task_root);
        progress_bar_add_task_root = findViewById(R.id.progress_bar_add_task_root);
        done_task_root = findViewById(R.id.done_task_root);
        text_input_layout_address_root = findViewById(R.id.text_input_layout_address_root);
        text_input_layout_floor_root = findViewById(R.id.text_input_layout_floor_root);
        text_input_layout_cabinet_root = findViewById(R.id.text_input_layout_cabinet_root);
        text_input_layout_name_task_root = findViewById(R.id.text_input_layout_name_task_root);
        text_input_layout_comment_root = findViewById(R.id.text_input_layout_comment_root);
        text_input_layout_date_task_root = findViewById(R.id.text_input_layout_date_task_root);
        text_input_layout_executor_root = findViewById(R.id.text_input_layout_executor_root);
        text_input_layout_status_root = findViewById(R.id.text_input_layout_status_root);
        edit_text_date_task_root = findViewById(R.id.edit_text_date_task_root);
        address_auto_complete_root = findViewById(R.id.address_autoComplete_root);
        status_auto_complete_root = findViewById(R.id.status_autoComplete_root);
        add_executor_root = findViewById(R.id.add_executor_root);
        text_input_layout_cabinet_liter_root = findViewById(R.id.text_input_layout_cabinet_liter_root);
        liter_auto_complete_root = findViewById(R.id.liter_autoComplete_root);
        urgent_request_check_box = findViewById(R.id.urgent_request_check_box);
        image_task = findViewById(R.id.image_task);

        firebase_auth = FirebaseAuth.getInstance();
        firebase_storage = FirebaseStorage.getInstance();
        storage_reference = firebase_storage.getReference();

        top_app_bar_new_task_root.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        location = arguments.get(LOCATION).toString();
        USER_EMAIL = arguments.get(EMAIL).toString();
        Log.d(TAG, location);

        initialize_fields(location);

        userID = Objects.requireNonNull(firebase_auth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection(USERS).document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
        });

        add_executor_root.setOnClickListener(v -> show_add_executor_dialog());

        image_task.setOnClickListener(v -> show_dialog_image());

        done_task_root.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
            letter = Objects.requireNonNull(text_input_layout_cabinet_liter_root.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment_root.getEditText()).getText().toString();
            date_complete = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
            email_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
            status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();
            urgent = urgent_request_check_box.isChecked();

            if (validateFields()) {
                if (!isOnline())
                    show_dialog();
                else
                    save_task(location);
            }

        });

    }

    private void show_dialog_image() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_image);

        RelativeLayout layout_new_photo = dialog.findViewById(R.id.layout_new_photo);
        RelativeLayout layout_folder = dialog.findViewById(R.id.layout_folder);

        layout_new_photo.setOnClickListener(v -> {
            dialog.dismiss();
        });
        layout_folder.setOnClickListener(v -> {
            chooseImage();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_task.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("DefaultLocale")
    private void uploadImage() {
        if (filePath != null) {
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

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
                        progressDialog.setMessage(String.format("Прогресс: %d%%", (int) progress));
                    });
        } else {
            NAME_IMAGE = null;
            onBackPressed();
        }
    }

    void save_task(String location) {
        Task task = new Task();

        uploadImage();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_create = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time_create = timeFormat.format(currentDate);

        task.save(new SaveTask(), location, name_task, address, date_create, floor, cabinet, letter, comment,
                date_complete, email_executor, status, time_create, email, urgent, NAME_IMAGE);

    }

    public void show_add_executor_dialog() {
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

                Objects.requireNonNull(text_input_layout_executor_root.getEditText()).setText(email_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> save_task(location))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> onBackPressed());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initialize_fields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            address_auto_complete_root.setAdapter(adapter);
        }

        if (location.equals(BAR_SCHOOL))
            Objects.requireNonNull(text_input_layout_address_root.getEditText()).setText(getText(R.string.bar_school_address));

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_auto_complete_root.setAdapter(adapter_status);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_litera = new ArrayAdapter<>(
                AddTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        liter_auto_complete_root.setAdapter(adapter_litera);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_root.setOnClickListener(v -> new DatePickerDialog(AddTaskRootActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

        text_input_layout_floor_root.getEditText().addTextChangedListener(new TextValidator(text_input_layout_floor_root.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, text_input_layout_floor_root, done_task_root, 1);
            }
        });

        text_input_layout_cabinet_root.getEditText().addTextChangedListener(new TextValidator(text_input_layout_cabinet_root.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {
                utils.validateNumberField(text, text_input_layout_cabinet_root, done_task_root, 3);
            }
        });
    }

    void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        Utils utils = new Utils();

        utils.clear_error(text_input_layout_address_root);
        utils.clear_error(text_input_layout_floor_root);
        utils.clear_error(text_input_layout_cabinet_root);
        utils.clear_error(text_input_layout_name_task_root);
        utils.clear_error(text_input_layout_date_task_root);
        utils.clear_error(text_input_layout_executor_root);
        utils.clear_error(text_input_layout_status_root);

        boolean check_address = utils.validate_field(address, text_input_layout_address_root);
        boolean check_name_task = utils.validate_field(name_task, text_input_layout_name_task_root);
        boolean check_floor = utils.validate_field(floor, text_input_layout_floor_root);
        boolean check_cabinet = utils.validate_field(cabinet, text_input_layout_cabinet_root);
        boolean check_date_task = utils.validate_field(date_complete, text_input_layout_date_task_root);
        boolean check_executor = utils.validate_field(email_executor, text_input_layout_executor_root);
        boolean check_status = utils.validate_field(status, text_input_layout_status_root);


        return check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}