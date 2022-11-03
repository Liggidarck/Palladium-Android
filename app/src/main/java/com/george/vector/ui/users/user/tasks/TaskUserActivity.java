package com.george.vector.ui.users.user.tasks;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.ID;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.R;
import com.george.vector.common.utils.NetworkUtils;
import com.george.vector.databinding.ActivityTaskUserBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;
import com.george.vector.ui.tasks.FragmentImageTask;
import com.google.android.material.snackbar.Snackbar;

public class TaskUserActivity extends AppCompatActivity {

    private long id;
    private String collection;
    private String cabinet;
    private String comment;
    private String dateCreate;
    private String timeCreate;
    private String image;
    private String letter;

    private ActivityTaskUserBinding binding;

    private final NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getLong(ID);
        collection = arguments.getString(ZONE);

        TaskViewModel taskViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(TaskUserActivity.this.getApplication(), collection)
        ).get(TaskViewModel.class);

        setSupportActionBar(binding.topAppBarTaskUser);
        binding.topAppBarTaskUser.setNavigationOnClickListener(v -> onBackPressed());

        taskViewModel.getTaskById(id).observe(this, task -> {
            comment = task.getComment();
            dateCreate = task.getDateCreate();

            image = task.getImage();
            letter = task.getLetter();

            String dateCreateText = "Созданно: " + dateCreate + " " + timeCreate;
            if (!letter.equals("-") && !letter.isEmpty())
                cabinet = String.format("%s%s", cabinet, letter);

            binding.textViewAddressTaskUser.setText(task.getAddress());
            binding.textViewFloorTaskUser.setText(task.getFloor());
            binding.textViewCabinetTaskUser.setText(task.getCabinet());
            binding.textViewNameTaskUser.setText(task.getName());
            binding.textViewCommentTaskUser.setText(comment);
            binding.textViewDateCreateTaskUser.setText(dateCreateText);
            binding.textViewStatusTaskUser.setText(task.getStatus());


            if (image != null) {
                Fragment imageFragment = new FragmentImageTask();
                Bundle bundle = new Bundle();
                bundle.putString("image_id", image);
                bundle.putLong(ID, id);
                bundle.putString(ZONE, collection);
                bundle.putString(ZONE, collection);
                imageFragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_image_task_user, imageFragment)
                        .commit();
            }

        });

        binding.progressBarTaskUser.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!networkUtils.isOnline(TaskUserActivity.this))
            Snackbar.make(findViewById(R.id.coordinator_task_user),
                            getString(R.string.error_no_connection),
                            Snackbar.LENGTH_LONG
                    ).show();
    }
}