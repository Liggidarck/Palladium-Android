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
import android.view.View;
import android.widget.Toast;

import com.george.vector.databinding.ActivityDevelopKotlinBinding;

import java.io.File;

public class DevelopJavaActivity extends AppCompatActivity {

    Uri tempImageUri;

    ActivityDevelopKotlinBinding activityDevelopJavaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDevelopJavaBinding = ActivityDevelopKotlinBinding.inflate(getLayoutInflater());
        View view = activityDevelopJavaBinding.getRoot();
        setContentView(view);


    }



}