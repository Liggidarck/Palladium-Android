package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static java.util.Objects.requireNonNull;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.data.preferences.UserDataViewModel;
import com.george.vector.databinding.ActivityEditTaskExecutorBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;

public class EditTaskExecutorActivity extends AppCompatActivity {

    String id;
    String collection;
    String comment;
    String dateCreate;
    String timeCreate;
    String email;
    String image;
    String fullNameExecutor;
    String emailCreator;
    String nameCreator;
    boolean urgent;

    TaskViewModel taskViewModel;

    ActivityEditTaskExecutorBinding binding;
    NetworkUtils networkUtils = new NetworkUtils();
    public static final String TAG = EditTaskExecutorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);

        Log.d(TAG, "onCreate: id: " + id);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                collection)
        ).get(TaskViewModel.class);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        email = userPrefViewModel.getUser().getEmail();

        binding.toolbarEditTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        taskViewModel.getTask(id).observe(this, task -> {
            dateCreate = task.getDateCreate();
            timeCreate = task.getTimeCreate();
            email = task.getEmailCreator();
            fullNameExecutor = task.getFullNameExecutor();
            emailCreator = task.getEmailCreator();
            nameCreator = task.getNameCreator();
            urgent = task.getUrgent();
            image = task.getImage();
            comment = task.getComment();

            requireNonNull(binding.textAddress.getEditText()).setText(task.getAddress());
            requireNonNull(binding.textFloor.getEditText()).setText(task.getFloor());
            requireNonNull(binding.textCabinet.getEditText()).setText(task.getCabinet());
            requireNonNull(binding.textLetter.getEditText()).setText(task.getLitera());
            requireNonNull(binding.textNameTask.getEditText()).setText(task.getNameTask());
            requireNonNull(binding.textStatus.getEditText()).setText(task.getStatus());
            requireNonNull(binding.textDateComplete.getEditText()).setText(task.getDateDone());
            requireNonNull(binding.textExecutor.getEditText()).setText(task.getExecutor());

            if (comment.equals("Нет коментария к заявке"))
                requireNonNull(binding.textComment.getEditText()).setText("");
            else
                requireNonNull(binding.textComment.getEditText()).setText(comment);

            initializeFields(collection);
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

        taskViewModel.updateTask(id, task);

        Intent intent = new Intent(this, MainExecutorActivity.class);
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

    void initializeFields(String collection) {
        if (collection.equals(OST_SCHOOL)) {
            String[] itemsAddresses = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskExecutorActivity.this,
                    R.layout.dropdown_menu_categories,
                    itemsAddresses
            );
            binding.autoCompleteAddress.setAdapter(adapter);
        }

        String[] itemsStatus = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter_status = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                itemsStatus
        );

        binding.autoCompleteStatus.setAdapter(adapter_status);

        String[] itemsLetter = getResources().getStringArray(R.array.letter);
        ArrayAdapter<String> adapterLetter = new ArrayAdapter<>(
                EditTaskExecutorActivity.this,
                R.layout.dropdown_menu_categories,
                itemsLetter
        );

        binding.autoCompleteLetter.setAdapter(adapterLetter);

    }

}