package com.george.vector.caretaker.tasks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.caretaker.main.MainCaretakerActivity;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
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

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddTaskCaretakerActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_new_task_caretaker;
    LinearProgressIndicator progress_bar_add_task_caretaker;
    ExtendedFloatingActionButton done_task_caretaker;
    Button add_executor_caretaker;

    TextInputEditText edit_text_date_task_caretaker;
    TextInputLayout text_input_layout_address_caretaker, text_input_layout_floor_caretaker,
                    text_input_layout_cabinet_caretaker, text_input_layout_name_task_caretaker,
                    text_input_layout_comment_caretaker, text_input_layout_executor_caretaker,
                    text_input_layout_status_caretaker, text_input_layout_date_task_caretaker;
    MaterialAutoCompleteTextView address_autoComplete_caretaker, status_autoComplete_caretaker;

    ImageView image_view_add_task_caretaker;

    private static final String TAG = "AddTaskCaretaker";
    String location, userID, email, address, floor, cabinet, name_task, date_task, status, comment, permission, randomKey;
    String name_executor;
    String last_name_executor;
    String patronymic_executor;
    String email_executor;

    Calendar datePickCalendar;
    public Uri imageUri;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_caretaker);

        topAppBar_new_task_caretaker = findViewById(R.id.topAppBar_new_task_caretaker);
        progress_bar_add_task_caretaker = findViewById(R.id.progress_bar_add_task_caretaker);
        done_task_caretaker = findViewById(R.id.done_task_caretaker);
        edit_text_date_task_caretaker = findViewById(R.id.edit_text_date_task_caretaker);
        text_input_layout_address_caretaker = findViewById(R.id.text_input_layout_address_caretaker);
        text_input_layout_name_task_caretaker = findViewById(R.id.text_input_layout_name_task_caretaker);
        text_input_layout_comment_caretaker = findViewById(R.id.text_input_layout_comment_caretaker);
        text_input_layout_floor_caretaker = findViewById(R.id.text_input_layout_floor_caretaker);
        text_input_layout_cabinet_caretaker = findViewById(R.id.text_input_layout_cabinet_caretaker);
        text_input_layout_executor_caretaker = findViewById(R.id.text_input_layout_executor_caretaker);
        text_input_layout_status_caretaker = findViewById(R.id.text_input_layout_status_caretaker);
        address_autoComplete_caretaker = findViewById(R.id.address_autoComplete_caretaker);
        text_input_layout_date_task_caretaker = findViewById(R.id.text_input_layout_date_task_caretaker);
        status_autoComplete_caretaker = findViewById(R.id.status_autoComplete_caretaker);
        add_executor_caretaker = findViewById(R.id.add_executor_caretaker);
        image_view_add_task_caretaker = findViewById(R.id.image_view_add_task_caretaker);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle arguments = getIntent().getExtras();
        location = arguments.get("location").toString();

        topAppBar_new_task_caretaker.setNavigationOnClickListener(v -> onBackPressed());
        add_executor_caretaker.setOnClickListener(v -> show_add_executor_dialog());

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
            permission = value.getString("permission");
        });

        done_task_caretaker.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor_caretaker.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet_caretaker.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task_caretaker.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).getText().toString();
            date_task = Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).getText().toString();
            email_executor = Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).getText().toString();
            status = Objects.requireNonNull(text_input_layout_status_caretaker.getEditText()).getText().toString();

            if(validateFields()){
                if(!isOnline())
                    show_alert_dialog();
                else
                    save_task(location);
            }

        });

        initialize_fields(location);
        clearErrors();
    }

    void save_task(@NotNull String location) {
        Task task = new Task();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        uploadImage();
        task.save(new SaveTask(), location, name_task, address, dateText, floor, cabinet, comment,
                date_task, email_executor, status, timeText, email, "62d7f792-2144-4da4-bfe6-b1ea80d348d7");

        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            image_view_add_task_caretaker.setImageURI(imageUri);
            Log.e(TAG, "imageUri: " + imageUri);
        }

    }

    private void uploadImage() {
        randomKey = UUID.randomUUID().toString();
        String final_url = String.format("images/%s", randomKey);

        StorageReference reference = storageReference.child(final_url);
        reference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    progress_bar_add_task_caretaker.setVisibility(View.INVISIBLE);
                    Log.i(TAG, "Image Uploaded");

                })
                .addOnFailureListener(e -> {
                    progress_bar_add_task_caretaker.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "Error! " + e);
                })
                .addOnProgressListener(snapshot -> {
                    progress_bar_add_task_caretaker.setVisibility(View.VISIBLE);
                    double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    Log.i(TAG, "Progress: " + (int) progress + "%");
                    progress_bar_add_task_caretaker.setProgress((int) progress);
                });
    }

    void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    void show_alert_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> save_task(location))
                .setNegativeButton(android.R.string.cancel,
                        (dialog, id) -> {
                            Intent intent = new Intent(this, MainCaretakerActivity.class);
                            intent.putExtra("permission", permission);
                            startActivity(intent);
                        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void show_add_executor_dialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_executor);

        RecyclerView recycler_view_list_executors = dialog.findViewById(R.id.recycler_view_list_executors);

        Query query = usersRef.whereEqualTo("role", "Исполнитель");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        UserAdapter adapter = new UserAdapter(options);

        recycler_view_list_executors.setHasFixedSize(true);
        recycler_view_list_executors.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_list_executors.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id = documentSnapshot.getId();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(id);
            documentReference.addSnapshotListener((value, error) -> {
                assert value != null;
                name_executor = value.getString("name");
                last_name_executor = value.getString("last_name");
                patronymic_executor = value.getString("patronymic");
                email_executor = value.getString("email");

                Log.i(TAG, "name: " + name_executor);
                Log.i(TAG, "last_name: " + last_name_executor);
                Log.i(TAG, "patronymic: " + patronymic_executor);
                Log.i(TAG, "email: " + email_executor);

                Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).setText(email_executor);
                dialog.dismiss();
            });


        });

        adapter.startListening();
        dialog.show();
    }

    void initialize_fields(@NotNull String location) {
        if(location.equals("ost_school")) {

            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskCaretakerActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );

            address_autoComplete_caretaker.setAdapter(adapter);
        }

        if (location.equals("bar_school"))
            Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).setText(getText(R.string.bar_school_address));

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                AddTaskCaretakerActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_caretaker.setAdapter(adapter_status);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_caretaker.setOnClickListener(v -> new DatePickerDialog(AddTaskCaretakerActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void updateLabel() {
        String date_text = "MM.dd.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }

    boolean validateFields() {
        Utils utils = new Utils();

        boolean check_address = utils.validate_field(address);
        boolean check_floor = utils.validate_field(floor);
        boolean check_cabinet = utils.validate_field(cabinet);
        boolean check_name_task = utils.validate_field(name_task);
        boolean check_date_task = utils.validate_field(date_task);
        boolean check_executor = utils.validate_field(email_executor);
        boolean check_status = utils.validate_field(status);

        if(check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status) {
            return true;
        } else {

            if(!check_address)
                text_input_layout_address_caretaker.setError("Это поле не может быть пустым");

            if(!check_floor)
                text_input_layout_floor_caretaker.setError("Это поле не может быть пустым");

            if(!check_cabinet)
                text_input_layout_cabinet_caretaker.setError("Это поле не может быть пустым");

            if(!check_name_task)
                text_input_layout_name_task_caretaker.setError("Это поле не может быть пустым");

            if(!check_date_task)
                text_input_layout_date_task_caretaker.setError("Это поле не может быть пустым");

            if(!check_executor)
                text_input_layout_executor_caretaker.setError("Это поле не может быть пустым");

            if(!check_status)
                text_input_layout_status_caretaker.setError("Это поле не может быть пустым");

            return false;
        }
    }

    void clearErrors() {
        Objects.requireNonNull(text_input_layout_address_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_address_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_floor_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_floor_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_cabinet_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_cabinet_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_name_task_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_task_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_comment_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_comment_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_date_task_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_date_task_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_executor_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_executor_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_status_caretaker.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_status_caretaker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}