package com.george.vector.develop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.common.tasks.ui.BottomSheetAddImage;
import com.george.vector.databinding.ActivityDevelopKotlinBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DevelopJavaActivity extends AppCompatActivity implements BottomSheetAddImage.StateListener{

    private static final String TAG = "DevelopJavaActivity";
    ActivityDevelopKotlinBinding binding;

    private static final int REQUEST_CODE_PICTURE= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevelopKotlinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }

    @Override
    public void getPhotoFromDevice(String button) {
        Log.d(TAG, "DATA: " + button);
    }
}