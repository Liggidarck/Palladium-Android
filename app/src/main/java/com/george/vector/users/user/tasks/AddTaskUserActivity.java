package com.george.vector.users.user.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.root.tasks.AddTaskRootActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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

public class AddTaskUserActivity extends AppCompatActivity {

    MaterialToolbar topAppBar_new_task_user;

    TextInputLayout text_input_layout_address, text_input_layout_floor,
                    text_input_layout_cabinet, text_input_layout_name_task,
                    text_input_layout_comment, text_input_layout_cabinet_liter_user;
    MaterialAutoCompleteTextView address_autoComplete, liter_autoComplete_user;

    ExtendedFloatingActionButton crate_task;
    LinearProgressIndicator progress_bar_add_task_user;

    String address, floor, cabinet, litera, name_task, comment, userID, email, status = "Новая заявка", permission;
    private static final String TAG = "AddTaskUserActivity";

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

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
        text_input_layout_cabinet_liter_user = findViewById(R.id.text_input_layout_cabinet_liter_user);
        liter_autoComplete_user = findViewById(R.id.liter_autoComplete_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle arguments = getIntent().getExtras();
        permission = arguments.get(getString(R.string.permission)).toString();
        Log.i(TAG, "Permission: " + permission);

        topAppBar_new_task_user.setNavigationOnClickListener(v -> onBackPressed());

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
            litera = Objects.requireNonNull(text_input_layout_cabinet_liter_user.getEditText()).getText().toString();
            name_task = Objects.requireNonNull(text_input_layout_name_task.getEditText()).getText().toString();
            comment = Objects.requireNonNull(text_input_layout_comment.getEditText()).getText().toString();

            if(validateFields()) {
                if(!isOnline()) {
                    show_dialog();
                } else {
                    save_task(permission);
                }
            }

        });

        initialize_field(permission);
    }

    void save_task(@NotNull String location) {
        Task task = new Task();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date_create = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time_create = timeFormat.format(currentDate);

        task.save(new SaveTask(), location, name_task, address, date_create, floor, cabinet, litera, comment,
                null, null, status, time_create, email);

        onBackPressed();
    }

    void initialize_field(String permission) {
        if(permission.equals(getString(R.string.ost_school))) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddTaskUserActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            address_autoComplete.setAdapter(adapter);
        }

        String[] itemsLitera = getResources().getStringArray(R.array.litera);
        ArrayAdapter<String> adapter_litera = new ArrayAdapter<>(
                AddTaskUserActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLitera
        );

        liter_autoComplete_user.setAdapter(adapter_litera);


        if(permission.equals(getString(R.string.bar_school)))
            Objects.requireNonNull(text_input_layout_address.getEditText()).setText(getText(R.string.bar_school_address));

    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> save_task(permission))
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
        Utils utils = new Utils();

        utils.clear_error(text_input_layout_address);
        utils.clear_error(text_input_layout_floor);
        utils.clear_error(text_input_layout_cabinet);
        utils.clear_error(text_input_layout_name_task);

        boolean check_address = utils.validate_field(address, text_input_layout_address);
        boolean check_floor = utils.validate_field(floor, text_input_layout_floor);
        boolean check_cabinet = utils.validate_field(cabinet, text_input_layout_cabinet);
        boolean check_name_task = utils.validate_field(name_task, text_input_layout_name_task);

        return check_address & check_floor & check_cabinet & check_name_task;
    }
}