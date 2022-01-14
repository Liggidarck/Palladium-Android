package com.george.vector.develop;

import static com.george.vector.common.consts.Keys.TOPIC_NEW_TASKS_CREATE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.common.tasks.ui.BottomSheetAddImage;
import com.george.vector.databinding.ActivityDevelopKotlinBinding;
import com.george.vector.databinding.BottomSheetAddImageBinding;
import com.george.vector.notifications.SendNotification;
import com.george.vector.users.root.tasks.BottomSheetAddTask;

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

        binding.sendButton.setOnClickListener(v -> {
            BottomSheetAddImage addImage = new BottomSheetAddImage();
            addImage.show(getSupportFragmentManager(), "BottomSheetAddImage");
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICTURE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.iamgeData.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getPhotoFromDevice(String button) {
        Log.d(TAG, "DATA: " + button);
    }
}