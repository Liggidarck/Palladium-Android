package com.george.vector.develop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.george.vector.databinding.ActivityDevelopBinding;
import com.george.vector.network.model.Task;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.network.viewmodel.ViewModelFactory;

public class DevelopActivity extends AppCompatActivity {

    ActivityDevelopBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevelopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
//        TaskViewModel taskViewModel =
//                new ViewModelProvider(this, new ViewModelFactory(this.getApplication(),
//                        "ost_school"),)
//                        .get(TaskViewModel.class);

        binding.createNewTask.setOnClickListener(v -> {
            Task task = new Task("test", "test", "test", "test",
                    "test", "test", "test", "test", "test",
                    "test", "test", "test", false, "test",
                    "test", "test");
//            taskViewModel.createTask(task);
        });
    }
}