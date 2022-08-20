package com.george.vector.ui.tasks;

import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.ID;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.databinding.ActivityImageTaskBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class ImageTaskActivity extends AppCompatActivity {

    String id, collection, image;

    ActivityImageTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityImageTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarImage.setNavigationOnClickListener(v -> onBackPressed());

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);

        String bufferSizePreference = PreferenceManager
                .getDefaultSharedPreferences(ImageTaskActivity.this)
                .getString("buffer_size", "2");
        int bufferSize = Integer.parseInt(bufferSizePreference);

        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.getApplication(),
                collection)
        ).get(TaskViewModel.class);

        binding.imageViewTask.setOnClickListener(v ->
                binding.imageViewTask
                        .animate()
                        .rotation(binding.imageViewTask.getRotation() + 90)
                        .setDuration(60));

        taskViewModel.getTask(id).observe(this, task -> {
            binding.progressBarImage.setVisibility(View.VISIBLE);
            image = task.getImage();
            if (image != null) {
                taskViewModel.setImage(image, binding.progressBarImage, binding.imageViewTask, bufferSize);
            } else {
                binding.imageViewTask.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            }
        });

    }
}