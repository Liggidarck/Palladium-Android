package com.george.vector.ui.users.executor.tasks;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.ZONE;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityTaskExecutorBinding;
import com.george.vector.network.model.user.User;
import com.george.vector.ui.common.tasks.FragmentUrgentRequest;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.UserViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

public class TaskExecutorActivity extends AppCompatActivity {

    private long id;

    ActivityTaskExecutorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskExecutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getLong(ID);
        String zone = arguments.getString(ZONE);

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        TaskViewModel taskViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(TaskExecutorActivity.this.getApplication(), userDataViewModel.getToken())
        ).get(TaskViewModel.class);

        UserViewModel userViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userDataViewModel.getToken()
        )).get(UserViewModel.class);

        binding.topAppBarTaskExecutor.setNavigationOnClickListener(v -> onBackPressed());

        binding.editTaskExecutorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTaskExecutorActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(ZONE, zone);
            startActivity(intent);
        });

        taskViewModel.getTaskById(id).observe(this, task -> {
            String letter = task.getLetter();
            String dateCreate = task.getDateCreate();
            String dateDone = task.getDateDone();
            String cabinet = task.getCabinet();
            boolean urgent = task.isUrgent();
            String status = task.getStatus();
            String dateCreateText = String.format("Созданно: %s", dateCreate);
            long creatorId = task.getCreatorId();

            User executor = userDataViewModel.getUser();
            String nameExecutor = executor.getLastName() + " " + executor.getName() + " " + executor.getPatronymic();

            if (!letter.equals("-") && !letter.isEmpty())
                cabinet = String.format("%s%s", cabinet, letter);

            if (urgent) {
                Fragment urgent_fragment = new FragmentUrgentRequest();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_urgent_task_executor, urgent_fragment)
                        .commit();

            }

            binding.textViewAddressTaskExecutor.setText(task.getAddress());
            binding.textViewFloorTaskExecutor.setText(String.format("%s %s", getText(R.string.floor), task.getFloor()));
            binding.textViewCabinetTaskExecutor.setText(String.format("%s %s", getText(R.string.cabinet), cabinet));
            binding.textViewNameTaskExecutor.setText(task.getName());
            binding.textViewCommentTaskExecutor.setText(task.getComment());
            binding.textViewDateCreateTaskExecutor.setText(dateCreateText);
            binding.textViewFullNameExecutorEX.setText(nameExecutor);
            binding.textViewEmailExecutorTaskExecutor.setText(executor.getEmail());

            if (dateDone == null)
                binding.textViewDateDoneTaskExecutor.setText("Дата выполнения не назначена");
            else {
                String date_done_text = "Дата выполнения: " + dateDone;
                binding.textViewDateDoneTaskExecutor.setText(date_done_text);
            }

            if (status.equals(NEW_TASKS)) {
                binding.circleStatusExecutor.setImageResource(R.color.red);
                binding.textViewStatusTaskExecutor.setText("Новая заявка");
            }

            if (status.equals(IN_PROGRESS_TASKS)) {
                binding.circleStatusExecutor.setImageResource(R.color.orange);
                binding.textViewStatusTaskExecutor.setText("Заявка в работе");
            }

            if (status.equals(ARCHIVE_TASKS)) {
                binding.circleStatusExecutor.setImageResource(R.color.green);
                binding.textViewStatusTaskExecutor.setText("Архив");
            }

            if (status.equals(COMPLETED_TASKS)) {
                binding.circleStatusExecutor.setImageResource(R.color.green);
                binding.textViewStatusTaskExecutor.setText("Завершенная заявка");
            }

            userViewModel.getUserById(creatorId).observe(this, creator -> {
                String nameCreator = creator.getLastName() + " " + creator.getName() + " " + creator.getPatronymic();
                binding.textViewFullNameCreatorExecutor.setText(nameCreator);
                binding.textViewEmailCreatorTaskExecutor.setText(creator.getEmail());
            });

        });

        binding.progressBarTaskExecutor.setVisibility(View.INVISIBLE);

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
                    .setAction("Повторить", v -> onStart()).show();
    }

}