package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityTaskUserBinding;
import com.george.vector.network.model.user.User;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

public class TaskUserActivity extends AppCompatActivity {

    private ActivityTaskUserBinding binding;

    private final NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        long id = arguments.getLong(ID);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        User currentUser = userPrefViewModel.getUser();

        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                TaskUserActivity.this.getApplication(),
                userPrefViewModel.getToken()
        )).get(TaskViewModel.class);

        setSupportActionBar(binding.topAppBarTaskUser);
        binding.topAppBarTaskUser.setNavigationOnClickListener(v -> onBackPressed());

        taskViewModel.getTaskById(id).observe(this, task -> {
            String comment = task.getComment();
            String dateCreate = task.getDateCreate();
            String letter = task.getLetter();
            String floor = "Этаж: " + task.getFloor();
            String cabinet = "Кабинет:  " + task.getCabinet();
            String dateCreateText = "Созданно: " + dateCreate;
            String nameCreator = currentUser.getLastName() + " " + currentUser.getName() + " " + currentUser.getPatronymic();
            String emailCreator = currentUser.getEmail();
            String status = task.getStatus();

            if (!letter.equals("-") && !letter.isEmpty())
                cabinet = "Кабинет: " + cabinet + " " + letter;

            binding.textViewAddressTaskUser.setText(task.getAddress());
            binding.textViewFloorTaskUser.setText(floor);
            binding.textViewCabinetTaskUser.setText(cabinet);
            binding.textViewNameTaskUser.setText(task.getName());
            binding.textViewCommentTaskUser.setText(comment);
            binding.textViewDateCreateTaskUser.setText(dateCreateText);
            binding.textViewFullNameCreatorUser.setText(nameCreator);
            binding.textViewEmailCreatorTaskUser.setText(emailCreator);

            if (status.equals(NEW_TASKS)) {
                binding.circleStatusUser.setImageResource(R.color.red);
                binding.textViewStatusTaskUser.setText("Новая заявка");
            }

            if (status.equals(IN_PROGRESS_TASKS)) {
                binding.circleStatusUser.setImageResource(R.color.orange);
                binding.textViewStatusTaskUser.setText("Заявка в работе");
            }

            if (status.equals(ARCHIVE_TASKS)) {
                binding.circleStatusUser.setImageResource(R.color.green);
                binding.textViewStatusTaskUser.setText("Архив");
            }

            if (status.equals(COMPLETED_TASKS)) {
                binding.circleStatusUser.setImageResource(R.color.green);
                binding.textViewStatusTaskUser.setText("Завершенная заявка");
            }

            binding.progressBarTaskUser.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!networkUtils.isOnline(TaskUserActivity.this)) {
            Snackbar.make(findViewById(R.id.coordinator_task_user),
                    getString(R.string.error_no_connection),
                    Snackbar.LENGTH_LONG
            ).show();
        }
    }
}