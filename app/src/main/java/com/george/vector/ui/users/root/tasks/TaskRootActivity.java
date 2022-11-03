package com.george.vector.ui.users.root.tasks;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.ID;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.databinding.ActivityTaskRootBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.tasks.FragmentImageTask;
import com.george.vector.ui.tasks.FragmentUrgentRequest;
import com.google.android.material.snackbar.Snackbar;

public class TaskRootActivity extends AppCompatActivity {

    private long id;
    private String address;
    private String floor;
    private String cabinet;
    private String letter;
    private String nameTask;
    private String comment;
    private String status;
    private String dateCreate;
    private String timeCreate;
    private String collection;
    private String imageId;
    private int creatorId;
    private int executorId;
    private String dateDone;
    private String fullNameExecutor;
    private String fullNameCreator;
    private boolean confirmDelete;
    private boolean urgent;

    private ActivityTaskRootBinding binding;

    TaskViewModel taskViewModel;
    NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskRootBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();

        taskViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(TaskRootActivity.this.getApplication(), collection)
        ).get(TaskViewModel.class);

        setSupportActionBar(binding.topAppBarTasksRoot);
        binding.topAppBarTasksRoot.setNavigationOnClickListener(v -> onBackPressed());

        binding.editTaskRootBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskRootActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(ZONE, collection);
            startActivity(intent);
        });

        getTask(collection, id);
    }

    private void initData() {
        Bundle arguments = getIntent().getExtras();

        id = arguments.getLong(ID);
        collection = arguments.getString(ZONE);

        confirmDelete = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("confirm_before_deleting_root", true);
    }

    void getTask(String collection, long id) {
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

            String dateCreateText = "Созданно: " + dateCreate + " " + timeCreate;

            binding.textViewAddressTaskRoot.setText(address);
            binding.textViewFloorTaskRoot.setText(floor);
            binding.textViewCabinetTaskRoot.setText(cabinet);
            binding.textViewNameTaskRoot.setText(nameTask);
            binding.textViewCommentTaskRoot.setText(comment);
            binding.textViewStatusTaskRoot.setText(status);
            binding.textViewDateCreateTaskRoot.setText(dateCreateText);
            binding.textViewEmailCreatorTaskRoot.setText(creatorId);

            if (status.equals("Новая заявка"))
                binding.circleStatusRoot.setImageResource(R.color.red);

            if (status.equals("В работе"))
                binding.circleStatusRoot.setImageResource(R.color.orange);

            if (status.equals("Архив") || status.equals("Завершенная заявка"))
                binding.circleStatusRoot.setImageResource(R.color.green);

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
                bundle.putString(ZONE, collection);
                bundle.putString(ZONE, this.collection);
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

            binding.progressBarTaskRoot.setVisibility(View.INVISIBLE);
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

            String sharing_data = nameTask + "\n" + comment + "\n \n" +
                    address + "\n" + "Этаж: " + floor + "\n" + "Кабинет: " + cabinet + "\n \n" +
                    "Создатель заявки" + "\n" + fullNameCreator + "\n" + creatorId + "\n \n" +
                    "Исполнитель" + "\n" + fullNameExecutor + "\n" + executorId + "\n" + "Дата выполнения: " + dateDone + "\n \n" +
                    "Статус" + "\n" + status + "\n" + "Созданно: " + dateCreate + " " + timeCreate + "\n \n" +
                    "Изображение" + "\n" + imageUrl;

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharing_data);
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
                .setPositiveButton(getText(R.string.delete), (dialog, id) -> deleteTask())
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void deleteTask() {
        taskViewModel.deleteTask(id, imageId);
        onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!networkUtils.isOnline(TaskRootActivity.this))
            Snackbar.make(findViewById(R.id.task_root_coordinator), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
    }

}