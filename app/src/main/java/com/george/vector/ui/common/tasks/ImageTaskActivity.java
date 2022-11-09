package com.george.vector.ui.common.tasks;

import static com.george.vector.common.utils.consts.Keys.ZONE;
import static com.george.vector.common.utils.consts.Keys.ID;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.ActivityImageTaskBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class ImageTaskActivity extends AppCompatActivity {

    long id;
    String image;

    ActivityImageTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityImageTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarImage.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getLong(ID);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);


        String bufferSizePreference = PreferenceManager
                .getDefaultSharedPreferences(ImageTaskActivity.this)
                .getString("buffer_size", "2");
        int bufferSize = Integer.parseInt(bufferSizePreference);

        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                userPrefViewModel.getToken())
        ).get(TaskViewModel.class);

        binding.imageViewTask.setOnClickListener(v ->
                binding.imageViewTask
                        .animate()
                        .rotation(binding.imageViewTask.getRotation() + 90)
                        .setDuration(60));

        taskViewModel.getTaskById(id).observe(this, task -> {
            binding.progressBarImage.setVisibility(View.VISIBLE);
            image = task.getImage();
            if (image != null) {

            } else {
                binding.imageViewTask.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            }
        });

    }
}