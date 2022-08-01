package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.LOCATION;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static java.util.Objects.requireNonNull;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.databinding.ActivityEditTaskExecutorBinding;
import com.george.vector.network.model.Task;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;

public class EditTaskExecutorActivity extends AppCompatActivity {

    String id;
    String collection;
    String location;
    String comment;
    String dateCreate;
    String timeCreate;
    String email;
    String image;
    String fullNameExecutor;
    String emailCreator;
    String nameCreator;
    String emailMailActivity;
    boolean urgent;

    TaskViewModel taskViewModel;

    ActivityEditTaskExecutorBinding binding;
    NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);
        emailMailActivity = arguments.getString(EMAIL);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        binding.toolbarEditTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        taskViewModel.getTask(id).observe(this, task -> {
            dateCreate = task.getDate_create();
            timeCreate = task.getTime_create();
            email = task.getEmail_creator();
            fullNameExecutor = task.getFullNameExecutor();
            emailCreator = task.getEmail_creator();
            nameCreator = task.getNameCreator();
            urgent = task.getUrgent();
            image = task.getImage();

            requireNonNull(binding.textAddress.getEditText()).setText(task.getAddress());
            requireNonNull(binding.textFloor.getEditText()).setText(task.getFloor());
            requireNonNull(binding.textCabinet.getEditText()).setText(task.getCabinet());
            requireNonNull(binding.textLetter.getEditText()).setText(task.getLitera());
            requireNonNull(binding.textNameTask.getEditText()).setText(task.getName_task());
            requireNonNull(binding.textStatus.getEditText()).setText(task.getStatus());
            requireNonNull(binding.textDateComplete.getEditText()).setText(task.getDate_done());
            requireNonNull(binding.textExecutor.getEditText()).setText(task.getExecutor());

            if (comment.equals("Нет коментария к заявке"))
                requireNonNull(binding.textComment.getEditText()).setText("");
            else
                requireNonNull(binding.textComment.getEditText()).setText(comment);

            initializeFields(location);
        });

        binding.progressEditTaskExecutor.setVisibility(View.INVISIBLE);

        binding.btnSaveTask.setOnClickListener(v -> {
            if (!networkUtils.isOnline(this)) {
                showDialog();
                return;
            }

            updateTask();
        });
    }

    void updateTask() {
        String updateImage = image;

        String updateAddress = requireNonNull(binding.textAddress.getEditText()).getText().toString();
        String updateFloor = requireNonNull(binding.textFloor.getEditText()).getText().toString();
        String updateCabinet = requireNonNull(binding.textCabinet.getEditText()).getText().toString();
        String updateLetter = requireNonNull(binding.textLetter.getEditText()).getText().toString();
        String updateName = requireNonNull(binding.textNameTask.getEditText()).getText().toString();
        String updateComment = requireNonNull(binding.textComment.getEditText()).getText().toString();
        String updateDateTask = requireNonNull(binding.textDateComplete.getEditText()).getText().toString();
        String updateExecutor = requireNonNull(binding.textExecutor.getEditText()).getText().toString();
        String updateStatus = requireNonNull(binding.textStatus.getEditText()).getText().toString();

        Task task = new Task(updateName, updateAddress, dateCreate, updateFloor,
                updateCabinet, updateLetter, updateComment, updateDateTask,
                updateExecutor, updateStatus, timeCreate, email, urgent, updateImage,
                fullNameExecutor, nameCreator);

        taskViewModel.createTask(task);

        Intent intent = new Intent(this, MainExecutorActivity.class);
        intent.putExtra(EMAIL, emailMailActivity);
        startActivity(intent);
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_no_connection))
                .setPositiveButton(getText(R.string.save), (dialog, id) -> updateTask())
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> startActivity(new Intent(this, MainExecutorActivity.class)));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void initializeFields(String location) {
        if (location.equals(OST_SCHOOL)) {
            String[] items = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskExecutorActivity.this,
                    R.layout.dropdown_menu_categories,
                    items
            );
            binding.autoCompleteAddress.setAdapter(adapter);
        }

        String[] items_status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                items_status
        );

        binding.autoCompleteStatus.setAdapter(adapter_status);

        String[] items_letter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapter_letter = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                items_letter
        );

        binding.autoCompleteLetter.setAdapter(adapter_letter);

    }

}