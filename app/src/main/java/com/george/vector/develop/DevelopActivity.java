package com.george.vector.develop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;

public class DevelopActivity extends AppCompatActivity {

    private static final String TAG = "DevelopActivity";
    ImageView image_develop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);

        Button button_notifications = findViewById(R.id.button_notifications);
        Button button_camera = findViewById(R.id.button_camera);
        image_develop = findViewById(R.id.image_develop);

        button_camera.setOnClickListener(v -> {

        });


    }




}
