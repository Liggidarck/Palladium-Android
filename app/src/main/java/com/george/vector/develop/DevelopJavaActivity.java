package com.george.vector.develop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.george.vector.R;

import java.io.File;
import java.util.Arrays;

public class DevelopJavaActivity extends AppCompatActivity {


    ImageView image_dev;
    Button gallery_btn, camera_btn, send_to_server;

    Uri tempImageUri;


    ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                tempImageUri = uri;
                image_dev.setImageURI(tempImageUri);
            });

    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    image_dev.setImageURI(tempImageUri);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop_kotlin);

        gallery_btn = findViewById(R.id.gallery_btn);
        camera_btn = findViewById(R.id.camera_btn);
        send_to_server = findViewById(R.id.send_to_server);
        image_dev = findViewById(R.id.image_dev);

        gallery_btn.setOnClickListener(v ->
                ActivityCompat.requestPermissions(
                        DevelopJavaActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        }, 1));

        camera_btn.setOnClickListener(v -> ActivityCompat.requestPermissions(
                DevelopJavaActivity.this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }, 2));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectPictureLauncher.launch("image/*");

                } else {
                    Toast.makeText(DevelopJavaActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }

                return;
            }

            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    File file = new File(getFilesDir(), "picFromCamera");
                    tempImageUri = FileProvider.getUriForFile(
                            this,
                            getApplicationContext().getPackageName() + ".provider",
                            file);
                    cameraLauncher.launch(tempImageUri);

                } else {
                    Toast.makeText(DevelopJavaActivity.this, "Permission denied to open camera", Toast.LENGTH_SHORT).show();
                }

                return;
            }


        }

    }

}