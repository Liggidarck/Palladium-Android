package com.george.vector.develop;

import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.databinding.ActivityDevelopKotlinBinding;
import com.george.vector.notifications.SendNotification;

public class DevelopJavaActivity extends AppCompatActivity {

    ActivityDevelopKotlinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevelopKotlinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.sendButton.setOnClickListener(v -> {
            String title = binding.developTitleEditText.getEditText().getText().toString();
            String message = binding.developMessageEditText.getEditText().getText().toString();

            SendNotification sendNotification = new SendNotification();
            sendNotification.sendNotification(title, message, TOPIC_NEW_TASKS_CREATE);
        });
    }
}