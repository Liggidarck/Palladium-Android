package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static java.util.Objects.requireNonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityEditTaskExecutorBinding;
import com.george.vector.network.model.Task;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.users.executor.main.MainExecutorActivity;

public class EditTaskExecutorActivity extends AppCompatActivity {

    private long taskId, creatorID, executorId;
    private String zone, comment, dateCreate;
    private boolean urgent;

    private TaskViewModel taskViewModel;

    private ActivityEditTaskExecutorBinding binding;
    private final NetworkUtils networkUtils = new NetworkUtils();

    public static final String TAG = EditTaskExecutorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userPrefViewModel.getToken())
        ).get(TaskViewModel.class);

        taskId = arguments.getLong(ID);
        zone = arguments.getString(ZONE);
        executorId = userPrefViewModel.getId();

        binding.toolbarEditTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        taskViewModel.getTaskById(taskId).observe(this, task -> {
            dateCreate = task.getDateCreate();
            urgent = task.isUrgent();
            comment = task.getComment();
            creatorID = task.getCreatorId();
            String status = task.getStatus();
            String nameExecutor = userPrefViewModel.getUser().getLastName() + " "
                    + userPrefViewModel.getUser().getName() + " "
                    + userPrefViewModel.getUser().getPatronymic();


            if (status.equals(NEW_TASKS))
                status = "Новая заявка";

            if (status.equals(IN_PROGRESS_TASKS))
                status = "Заявка в работе";

            if (status.equals(COMPLETED_TASKS))
                status = "Завершенная заявка";

            if (status.equals(ARCHIVE_TASKS))
                status = "Архив";

            requireNonNull(binding.textAddress.getEditText()).setText(task.getAddress());
            requireNonNull(binding.textFloor.getEditText()).setText(task.getFloor());
            requireNonNull(binding.textCabinet.getEditText()).setText(task.getCabinet());
            requireNonNull(binding.textLetter.getEditText()).setText(task.getLetter());
            requireNonNull(binding.textNameTask.getEditText()).setText(task.getName());
            requireNonNull(binding.textStatus.getEditText()).setText(status);
            requireNonNull(binding.textDateComplete.getEditText()).setText(task.getDateDone());
            requireNonNull(binding.textExecutor).getEditText().setText(nameExecutor);

            if (comment.equals("Нет коментария к заявке"))
                requireNonNull(binding.textComment.getEditText()).setText("");
            else
                requireNonNull(binding.textComment.getEditText()).setText(comment);

            binding.progressEditTaskExecutor.setVisibility(View.INVISIBLE);
            initializeFields(zone);
        });

        binding.btnSaveTask.setOnClickListener(v -> {
            if (!networkUtils.isOnline(this)) {
                showDialog();
                return;
            }

            updateTask();
        });
    }

    void updateTask() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Ваша заявка обновляется...");
        progressDialog.show();

        String updateAddress = requireNonNull(binding.textAddress.getEditText()).getText().toString();
        String updateFloor = requireNonNull(binding.textFloor.getEditText()).getText().toString();
        String updateCabinet = requireNonNull(binding.textCabinet.getEditText()).getText().toString();
        String updateLetter = requireNonNull(binding.textLetter.getEditText()).getText().toString();
        String updateName = requireNonNull(binding.textNameTask.getEditText()).getText().toString();
        String updateComment = requireNonNull(binding.textComment.getEditText()).getText().toString();
        String updateDateDone = requireNonNull(binding.textDateComplete.getEditText()).getText().toString();
        String updateStatus = requireNonNull(binding.textStatus.getEditText()).getText().toString();

        if (updateStatus.equals("Новая заявка"))
            updateStatus = NEW_TASKS;

        if (updateStatus.equals("Заявка в работе"))
            updateStatus = IN_PROGRESS_TASKS;

        if (updateStatus.equals("Завершенная заявка"))
            updateStatus = COMPLETED_TASKS;

        if (updateStatus.equals("Архив"))
            updateStatus = ARCHIVE_TASKS;

        Task task = new Task(zone, updateStatus, updateName, updateComment,
                updateAddress, updateFloor, updateCabinet, updateLetter,
                urgent, updateDateDone, (int) executorId, (int) creatorID, dateCreate, null);

        taskViewModel.editTask(task, taskId).observe(this, message -> {
            if (message.getMessage().equals("Task successfully edited")) {
                progressDialog.dismiss();

                Intent intent = new Intent(this, MainExecutorActivity.class);
                startActivity(intent);
            }
        });
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

    void initializeFields(String zone) {
        if (zone.equals(OST_SCHOOL)) {
            String[] itemsAddresses = getResources().getStringArray(R.array.addressesOstSchool);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditTaskExecutorActivity.this,
                    R.layout.dropdown_menu_categories,
                    itemsAddresses
            );
            binding.autoCompleteAddress.setAdapter(adapter);
        }

        String[] itemsStatus = getResources().getStringArray(R.array.status_name);
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