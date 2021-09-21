package com.george.vector.users.root.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;
import static com.george.vector.common.consts.Keys.OST_SCHOOL;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.george.vector.R;
import com.george.vector.common.edit_users.User;
import com.george.vector.common.edit_users.UserAdapter;
import com.george.vector.common.tasks.utils.DeleteTask;
import com.george.vector.common.tasks.utils.SaveTask;
import com.george.vector.common.tasks.utils.Task;
import com.george.vector.common.utils.Utils;
import com.george.vector.users.root.main.RootMainActivity;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EditTaskRootActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskRoot";

    MaterialToolbar topAppBar_new_task_root;
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
    MaterialAutoCompleteTextView address_autoComplete_root, status_autoComplete_root, liter_autoComplete_root;
    ImageView image_task;

    Calendar datePickCalendar;

    String id, collection, address, floor, cabinet, litera, name_task, comment, status, date_create, time_create,
            date_done, email, location, USER_EMAIL, image;
    boolean urgent;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("users");

    String name_executor;
    String last_name_executor;
    String patronymic_executor;
    String email_executor;

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_root);

        topAppBar_new_task_root = findViewById(R.id.topAppBar_new_task_root);
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
        address_autoComplete_root = findViewById(R.id.address_autoComplete_root);
        status_autoComplete_root = findViewById(R.id.status_autoComplete_root);
        add_executor_root = findViewById(R.id.add_executor_root);
        text_input_layout_cabinet_liter_root = findViewById(R.id.text_input_layout_cabinet_liter_root);
        liter_autoComplete_root = findViewById(R.id.liter_autoComplete_root);
        urgent_request_check_box = findViewById(R.id.urgent_request_check_box);
        image_task = findViewById(R.id.image_task);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        topAppBar_new_task_root.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(ID).toString();
        collection = arguments.get(COLLECTION).toString();
        location = arguments.get(LOCATION).toString();
        USER_EMAIL = arguments.get(EMAIL).toString();

        add_executor_root.setOnClickListener(v -> show_add_executor_dialog());

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_add_task_root.setVisibility(View.VISIBLE);

            assert value != null;
            address = value.getString("address");
            floor = value.getString("floor");
            cabinet = value.getString("cabinet");
            litera = value.getString("litera");
            name_task = value.getString("name_task");
            comment = value.getString("comment");
            status = value.getString("status");

            date_done = value.getString("date_done");
            email_executor = value.getString("executor");

            date_create = value.getString("date_create");
            time_create = value.getString("time_create");
            email = value.getString("email_creator");

            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                progress_bar_add_task_root.setVisibility(View.INVISIBLE);
                image_task.setImageResource(R.drawable.no_image);
            } else {
                String IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image);
                Picasso.with(this)
                        .load(IMAGE_URL)
                        .into(image_task, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress_bar_add_task_root.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                Log.e(TAG, "Error on download");
                            }
                        });
            }

            try {
                urgent = value.getBoolean("urgent");

                Objects.requireNonNull(text_input_layout_address_root.getEditText()).setText(address);
                Objects.requireNonNull(text_input_layout_floor_root.getEditText()).setText(floor);
                Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).setText(cabinet);
                Objects.requireNonNull(text_input_layout_cabinet_liter_root.getEditText()).setText(litera);
                Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).setText(name_task);
                Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(date_done);
                Objects.requireNonNull(text_input_layout_executor_root.getEditText()).setText(email_executor);
                Objects.requireNonNull(text_input_layout_status_root.getEditText()).setText(status);

                if (comment.equals("Нет коментария к заявке"))
                    Objects.requireNonNull(text_input_layout_comment_root.getEditText()).setText("");
                else
                    Objects.requireNonNull(text_input_layout_comment_root.getEditText()).setText(comment);

                urgent_request_check_box.setChecked(urgent);

            } catch (Exception e) {
                Log.i(TAG, "Error! " + e);
            }

            initialize_fields(location);
        });

        done_task_root.setOnClickListener(v -> {

            if(validateFields()) {
                if(!isOnline())
                    show_dialog();
                else
                    updateTask(collection);
            }

        });
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
            if(isChecked){
                Log.i(TAG, "root checked");

                query = usersRef.whereEqualTo("role", "Root");

                FirestoreRecyclerOptions<User> UserOptions = new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

                adapter.updateOptions(UserOptions);
            }
        });

        chip_executors_dialog.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
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

    void updateTask(String collection) {

        String update_image = image;

        Task task = new Task();
        DeleteTask deleteTask = new DeleteTask();
        deleteTask.delete_task(collection, id);

        String update_address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        String update_floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        String update_cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        String update_litera = Objects.requireNonNull(text_input_layout_cabinet_liter_root.getEditText()).getText().toString();
        String update_name = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String update_comment = Objects.requireNonNull(text_input_layout_comment_root.getEditText()).getText().toString();
        String update_date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        String update_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        String update_status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();
        boolean update_urgent = urgent_request_check_box.isChecked();

        task.save(new SaveTask(), location, update_name, update_address, date_create, update_floor,
                update_cabinet, update_litera, update_comment, update_date_task,
                update_executor, update_status, time_create, email, update_urgent, update_image);

        Intent intent = new Intent(this, RootMainActivity.class);
        intent.putExtra(EMAIL, USER_EMAIL);
        startActivity(intent);
    }

    void show_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask(collection))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    Intent intent = new Intent(this, RootMainActivity.class);
                    intent.putExtra(EMAIL, USER_EMAIL);
                    startActivity(intent);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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

        address = Objects.requireNonNull(text_input_layout_address_root.getEditText()).getText().toString();
        floor = Objects.requireNonNull(text_input_layout_floor_root.getEditText()).getText().toString();
        cabinet = Objects.requireNonNull(text_input_layout_cabinet_root.getEditText()).getText().toString();
        name_task = Objects.requireNonNull(text_input_layout_name_task_root.getEditText()).getText().toString();
        String date_task = Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).getText().toString();
        email_executor = Objects.requireNonNull(text_input_layout_executor_root.getEditText()).getText().toString();
        status = Objects.requireNonNull(text_input_layout_status_root.getEditText()).getText().toString();

        boolean check_address = utils.validate_field(address, text_input_layout_address_root);
        boolean check_floor = utils.validate_field(floor, text_input_layout_floor_root);
        boolean check_cabinet = utils.validate_field(cabinet, text_input_layout_cabinet_root);
        boolean check_name_task = utils.validate_field(name_task, text_input_layout_name_task_root);
        boolean check_date_task = utils.validate_field(date_task, text_input_layout_date_task_root);
        boolean check_executor = utils.validate_field(email_executor, text_input_layout_executor_root);
        boolean check_status = utils.validate_field(status, text_input_layout_status_root);

        return check_address & check_floor & check_cabinet & check_name_task & check_date_task & check_executor & check_status;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void initialize_fields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addresses_ost_school);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskRootActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            address_autoComplete_root.setAdapter(adapter);
        }

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        status_autoComplete_root.setAdapter(adapter_status);

        String[] itemsLitera = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_litera = new ArrayAdapter<>(
                EditTaskRootActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLitera
        );

        liter_autoComplete_root.setAdapter(adapter_litera);

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edit_text_date_task_root.setOnClickListener(v -> new DatePickerDialog(EditTaskRootActivity.this, date, datePickCalendar
                .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());

    }

    void updateLabel() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);

        Objects.requireNonNull(text_input_layout_date_task_root.getEditText()).setText(sdf.format(datePickCalendar.getTime()));
    }
}