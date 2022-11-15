package com.george.vector.ui.users.admin.tasks;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.ZONE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityTaskRootBinding;
import com.george.vector.ui.common.tasks.FragmentImageTask;
import com.george.vector.ui.users.admin.tasks.contoll.EditTaskAdminActivity;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.common.tasks.FragmentUrgentRequest;
import com.google.android.material.snackbar.Snackbar;

public class TaskAdminActivity extends AppCompatActivity {

    private long taskId;
    private String zone;
    private String address;
    private String floor;
    private String cabinet;
    private String letter;
    private String nameTask;
    private String comment;
    private String status;
    private String dateCreate;
    private String imageId;
    private int creatorId;
    private int executorId;
    private String dateDone;
    private String executorName;
    private String userName;
    private boolean confirmDelete;
    private boolean urgent;
    private boolean executed;

    private ActivityTaskRootBinding binding;

    private TaskViewModel taskViewModel;
    private UserViewModel userViewModel;

    private final NetworkUtils networkUtils = new NetworkUtils();

    public static final String TAG = TaskAdminActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();

        setSupportActionBar(binding.topAppBarTasksRoot);
        binding.topAppBarTasksRoot.setNavigationOnClickListener(v -> onBackPressed());

        binding.editTaskRootBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskAdminActivity.class);
            intent.putExtra(ID, taskId);
            intent.putExtra(ZONE, zone);
            intent.putExtra(IS_EXECUTE, executed);
            startActivity(intent);
        });

        getTask(taskId);
    }

    private void initData() {
        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userPrefViewModel.getToken()
        )).get(TaskViewModel.class);

        userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userPrefViewModel.getToken()
        )).get(UserViewModel.class);

        Bundle arguments = getIntent().getExtras();

        taskId = arguments.getLong(ID);
        zone = arguments.getString(ZONE);
        executed = arguments.getBoolean(IS_EXECUTE);

        confirmDelete = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("confirm_before_deleting_root", true);
    }

    void getTask(long id) {
        taskViewModel.getTaskById(id).observe(this, task -> {
            address = task.getAddress();
            floor = String.format("Этаж: %s", task.getFloor());
            cabinet = String.format("Кабинет: %s", task.getCabinet());
            letter = task.getLetter();
            nameTask = task.getName();
            comment = task.getComment();
            status = task.getStatus();
            dateCreate = task.getDateCreate();
            imageId = task.getImage();
            creatorId = task.getCreatorId();
            executorId = task.getExecutorId();
            dateDone = task.getDateDone();
            urgent = task.isUrgent();
            String dateCreateText = "Созданно: " + dateCreate;

            binding.textViewAddressTaskRoot.setText(address);
            binding.textViewFloorTaskRoot.setText(floor);
            binding.textViewCabinetTaskRoot.setText(cabinet);
            binding.textViewNameTaskRoot.setText(nameTask);
            binding.textViewCommentTaskRoot.setText(comment);
            binding.textViewDateCreateTaskRoot.setText(dateCreateText);

            if (status.equals(NEW_TASKS)) {
                binding.circleStatusRoot.setImageResource(R.color.red);
                binding.textViewStatusTaskRoot.setText("Новая заявка");
            }

            if (status.equals(IN_PROGRESS_TASKS)) {
                binding.circleStatusRoot.setImageResource(R.color.orange);
                binding.textViewStatusTaskRoot.setText("Заявка в работе");
            }

            if (status.equals(ARCHIVE_TASKS)) {
                binding.circleStatusRoot.setImageResource(R.color.green);
                binding.textViewStatusTaskRoot.setText("Архив");
            }

            if (status.equals(COMPLETED_TASKS)) {
                binding.circleStatusRoot.setImageResource(R.color.green);
                binding.textViewStatusTaskRoot.setText("Завершенная заявка");
            }

            if (!letter.equals("-") && !letter.isEmpty())
                cabinet = String.format("%s%s", cabinet, letter);

            if (dateDone == null)
                binding.textViewDateDoneTaskRoot.setText("Дата выполнения не назначена");
            else {
                String dateDoneText = "Дата выполнения: " + dateDone;
                binding.textViewDateDoneTaskRoot.setText(dateDoneText);
            }


            if (imageId != null) {
                Fragment imageFragment = new FragmentImageTask();
                Bundle bundle = new Bundle();
                bundle.putString("image_id", imageId);
                bundle.putLong(ID, id);
                bundle.putString(ZONE, zone);
                bundle.putString(ZONE, this.zone);
                imageFragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_image_task, imageFragment)
                        .commit();
            }

            if (urgent) {
                Fragment urgentFragment = new FragmentUrgentRequest();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_urgent_task, urgentFragment)
                        .commit();
            }

            userViewModel.getUserById(creatorId).observe(this, user -> {
                userName = user.getLastName() + " " + user.getLastName() + " " + user.getPatronymic();
                String email = user.getEmail();

                binding.textViewFullNameCreator.setText(userName);
                binding.textViewEmailCreatorTaskRoot.setText(email);

                binding.progressBarTaskRoot.setVisibility(View.INVISIBLE);
            });

            if(executorId == 0) {
                binding.textViewFullNameExecutor.setText("Нет назначенного исполнителя");
                binding.textViewEmailExecutorTaskRoot.setText("Нет назначенного исполнителя");
                return;
            }

            userViewModel.getUserById(executorId).observe(this, executor -> {
                executorName = executor.getLastName() + " " + executor.getName() + " " + executor.getPatronymic();
                String email = executor.getEmail();

                binding.textViewFullNameExecutor.setText(executorName);
                binding.textViewEmailExecutorTaskRoot.setText(email);

                binding.progressBarTaskRoot.setVisibility(View.INVISIBLE);
            });


        });

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.task_root_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.root_delete_task) {
            if (confirmDelete)
                showDialogDelete();
            else
                deleteTask();
        }

        if (item.getItemId() == R.id.root_share_task) {
            String imageUrl;

            if (imageId == null)
                imageUrl = "К заявке не прикреплено изображение";
            else
                imageUrl = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", imageId);

            String sharingData = nameTask + "\n" + comment + "\n \n" +
                    address + "\n" + "Этаж: " + floor + "\n" + "Кабинет: " + cabinet + "\n \n" +
                    "Создатель заявки" + "\n" + userName + "\n \n" +
                    "Исполнитель" + "\n" + "\n" + executorName + "\n" + "Дата выполнения: " + dateDone + "\n \n" +
                    "Статус" + "\n" + status + "\n" + "Созданно: " + dateCreate + "\n \n" +
                    "Изображение" + "\n" + imageUrl;

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharingData);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        return true;
    }

    void showDialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.warning))
                .setMessage(getText(R.string.warning_delete_task))
                .setPositiveButton(getText(R.string.delete), (dialog, id) -> {
                    deleteTask();
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void deleteTask() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.setMessage("Ваша заявка удаляется...");
        progressDialog.show();

        taskViewModel.deleteTask(taskId).observe(this, message -> {

            if (message.getMessage().equals("Task successfully deleted")) {
                onBackPressed();
                progressDialog.dismiss();
            } else {
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!networkUtils.isOnline(TaskAdminActivity.this))
            Snackbar.make(findViewById(R.id.task_root_coordinator), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
    }

}