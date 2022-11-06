package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.ID;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.databinding.ActivityTaskExecutorBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.common.tasks.FragmentImageTask;
import com.george.vector.ui.common.tasks.FragmentUrgentRequest;
import com.google.android.material.snackbar.Snackbar;

public class TaskExecutorActivity extends AppCompatActivity {

    long id;
    String collection;
    String cabinet;
    String letter;
    String status;
    String dateCreate;
    String timeCreate;
    String dateDone;
    String image;

    boolean urgent;

    ActivityTaskExecutorBinding taskExecutorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        taskExecutorBinding = ActivityTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(taskExecutorBinding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getLong(ID);
        collection = arguments.getString(ZONE);

        TaskViewModel taskViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(TaskExecutorActivity.this.getApplication(), collection)
        ).get(TaskViewModel.class);

        taskExecutorBinding.topAppBarTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        taskExecutorBinding.editTaskExecutorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskExecutorActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(ZONE, collection);
            startActivity(intent);
        });

        taskViewModel.getTaskById(id).observe(this, task -> {
            image = task.getImage();
            letter = task.getLetter();
            dateCreate = task.getDateCreate();

            dateDone = task.getDateDone();
            cabinet = task.getCabinet();
            urgent = task.isUrgent();
            status = task.getStatus();
            String dateCreateText = String.format("Созданно: %s %s", dateCreate, timeCreate);

            if (status.equals("Новая заявка"))
                taskExecutorBinding.circleStatusExecutor.setImageResource(R.color.red);

            if (status.equals("В работе"))
                taskExecutorBinding.circleStatusExecutor.setImageResource(R.color.orange);

            if (status.equals("Архив"))
                taskExecutorBinding.circleStatusExecutor.setImageResource(R.color.green);

            if (!letter.equals("-") && !letter.isEmpty())
                cabinet = String.format("%s%s", cabinet, letter);

            if (urgent) {

                Fragment urgent_fragment = new FragmentUrgentRequest();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_urgent_task_executor, urgent_fragment)
                        .commit();

            }

            if (image != null) {
                Fragment fragmentImageTask = new FragmentImageTask();
                Bundle bundle = new Bundle();
                bundle.putString("image_id", image);
                bundle.putLong(ID, id);
                bundle.putString(ZONE, collection);
                fragmentImageTask.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_image_task_executor, fragmentImageTask)
                        .commit();
            }

            taskExecutorBinding.textViewAddressTaskExecutor.setText(task.getAddress());
            taskExecutorBinding.textViewFloorTaskExecutor.setText(String.format("%s %s", getText(R.string.floor), task.getFloor()));
            taskExecutorBinding.textViewCabinetTaskExecutor.setText(String.format("%s %s", getText(R.string.cabinet), cabinet));
            taskExecutorBinding.textViewNameTaskExecutor.setText(task.getName());
            taskExecutorBinding.textViewCommentTaskExecutor.setText(task.getComment());
            taskExecutorBinding.textViewStatusTaskExecutor.setText(status);
            taskExecutorBinding.textViewDateCreateTaskExecutor.setText(dateCreateText);
//            taskExecutorBinding.textViewFullNameCreatorExecutor.setText(task.getNameCreator());
//            taskExecutorBinding.textViewEmailCreatorTaskExecutor.setText(task.getEmailCreator());
//            taskExecutorBinding.textViewFullNameExecutorEX.setText(task.getFullNameExecutor());
//            taskExecutorBinding.textViewEmailExecutorTaskExecutor.setText(task.getExecutor());
            if (dateDone == null)
                taskExecutorBinding.textViewDateDoneTaskExecutor.setText("Дата выполнения не назначена");
            else {
                String date_done_text = "Дата выполнения: " + dateDone;
                taskExecutorBinding.textViewDateDoneTaskExecutor.setText(date_done_text);
            }

        });

        taskExecutorBinding.progressBarTaskExecutor.setVisibility(View.INVISIBLE);

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_task_executor), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v -> {
                        onStart();
                    }).show();
    }

}