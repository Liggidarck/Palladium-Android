package com.george.vector.user.tasks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.george.vector.R;
import com.george.vector.common.utils.ErrorsUtils;
import com.george.vector.common.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddTaskUserActivity extends AppCompatActivity {


    MaterialToolbar topAppBar_new_task_user;

    TextInputLayout text_input_layout_address, text_input_layout_floor, text_input_layout_cabinet,
            text_input_layout_name_task, text_input_layout_comment;
    MaterialAutoCompleteTextView address_autoComplete;

    ExtendedFloatingActionButton crate_task;
    LinearProgressIndicator progress_bar_add_task_user;
    ImageView image_view_task_user;

    String address, floor, cabinet, name_task, comment, userID, email, randomKey;

    String  status = "Новая заявка";
    private static final String TAG = "AddTaskUserActivity";

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public Uri imageUri;

    String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_user);

        topAppBar_new_task_user = findViewById(R.id.topAppBar_new_task_user);
        text_input_layout_address = findViewById(R.id.text_input_layout_address);
        text_input_layout_floor = findViewById(R.id.text_input_layout_floor);
        text_input_layout_cabinet = findViewById(R.id.text_input_layout_cabinet);
        text_input_layout_name_task = findViewById(R.id.text_input_layout_name_task);
        text_input_layout_comment = findViewById(R.id.text_input_layout_comment);
        address_autoComplete = findViewById(R.id.address_autoComplete);
        crate_task = findViewById(R.id.crate_task);
        progress_bar_add_task_user = findViewById(R.id.progress_bar_add_task_user);
        image_view_task_user = findViewById(R.id.image_view_add_task_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle arguments = getIntent().getExtras();
        permission = arguments.get("permission").toString();

        topAppBar_new_task_user.setNavigationOnClickListener(v -> onBackPressed());

        String[] items = getResources().getStringArray(R.array.addresses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AddTaskUserActivity.this,
                R.layout.dropdown_menu_categories,
                items
        );

        address_autoComplete.setAdapter(adapter);

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            email = value.getString("email");
        });

        crate_task.setOnClickListener(v -> {
            address = Objects.requireNonNull(text_input_layout_address.getEditText()).getText().toString();
            floor = Objects.requireNonNull(text_input_layout_floor.getEditText()).getText().toString();
            cabinet = Objects.requireNonNull(text_input_layout_cabinet.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();

            Log.i(TAG, "address: " + address);
            Log.i(TAG, "floor: " + floor);
            Log.i(TAG, "cabinet: " + cabinet);
            Log.i(TAG, "name_task: " + name_task);
            Log.i(TAG, "comment: " + comment);

            if(comment.isEmpty())
                comment = "Нет коментария к заявке";

            Log.i(TAG, "comment(update): " + comment);

            if(validateFields()) {
                if(!isOnline()) {
                    show_dialog();
                } else
                    initialize_location(permission);
            }

        });

        clear_errors();

    }

    void saveTask(String collection) {
        progress_bar_add_task_user.setVisibility(View.VISIBLE);

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        CollectionReference taskRef = FirebaseFirestore.getInstance().collection(collection);

        taskRef.add(new Task(name_task, address, dateText, floor, cabinet, comment, null,
                null, status, timeText, email, "62d7f792-2144-4da4-bfe6-b1ea80d348d7"));

        taskRef.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                Log.i(TAG, "add completed!");
                progress_bar_add_task_user.setVisibility(View.INVISIBLE);
                finish();
            } else {
                Log.i(TAG, "Error: " + task.getException());
            }
        });
    }

    void initialize_location(@NotNull String location) {
        if(location.equals("ost_school")) {
            if (status.equals("Новая заявка"))
                saveTask("ost_school_new");

            if (status.equals("В работе"))
                saveTask("ost_school_progress");

            if (status.equals("Архив"))
                saveTask("ost_school_archive");
        }
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Внимание!")
                .setMessage("Отсуствует интернет подключение. Вы можете сохранить заявку у себя в телефоне и когда интренет снова появиться заявка автоматически будет отправлена в фоновом режиме. Или вы можете отправить заявку заявку позже, когда появиться интрнет.")

                .setPositiveButton("Сохранить", (dialog, id) -> initialize_location(permission))

                .setNegativeButton(android.R.string.cancel, (dialog, id) -> onBackPressed());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            image_view_task_user.setImageURI(imageUri);
        }

    }

    private void uploadImage() {
        randomKey = UUID.randomUUID().toString();
        String final_url = String.format("images/%s", randomKey);

        Log.i(TAG, "url: " + final_url);

        StorageReference reference = storageReference.child(final_url);

        reference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    progress_bar_add_task_user.setVisibility(View.INVISIBLE);
                    Log.i(TAG, "Image Uploaded");

                })
                .addOnFailureListener(e -> {
                    progress_bar_add_task_user.setVisibility(View.INVISIBLE);
                    Log.i(TAG, "Error! " + e);
                })
                .addOnProgressListener(snapshot -> {
                    progress_bar_add_task_user.setVisibility(View.VISIBLE);
                    double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    Log.i(TAG, "Progress: " + (int) progress + "%");
                    progress_bar_add_task_user.setProgress((int) progress);
                });
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    boolean validateFields() {
        ErrorsUtils errorsUtils = new ErrorsUtils();

        boolean check_address = errorsUtils.validate_field(address);
        boolean check_floor = errorsUtils.validate_field(floor);
        boolean check_cabinet = errorsUtils.validate_field(cabinet);
        boolean check_name_task = errorsUtils.validate_field(name_task);

        if(check_address & check_floor & check_cabinet & check_name_task ) {
            return true;
        } else {

            if(!check_address)
                text_input_layout_address.setError("Это поле не может быть пустым");

            if(!check_floor)
                text_input_layout_floor.setError("Это поле не может быть пустым");

            if(!check_cabinet)
                text_input_layout_cabinet.setError("Это поле не может быть пустым");

            if(!check_name_task)
                text_input_layout_name_task.setError("Это поле не может быть пустым");

            return false;
        }

    }

    void clear_errors() {

        Objects.requireNonNull(text_input_layout_address.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_address.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_floor.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_floor.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_cabinet.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_cabinet.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_name_task.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_name_task.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(text_input_layout_comment.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_input_layout_comment.setError(null);
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