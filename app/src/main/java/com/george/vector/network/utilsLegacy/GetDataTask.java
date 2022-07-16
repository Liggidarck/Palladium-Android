package com.george.vector.network.utilsLegacy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GetDataTask {

    private static final String TAG = "GetDataTask";

    public void setImage(String image_name, LinearProgressIndicator progressIndicator, ImageView imageView, int buffer_size) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("images/" + image_name);

        long ONE_MEGABYTE = buffer_size * (1024 * 1024);
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            progressIndicator.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(bmp);
        });

    }

    public void setImageFuture(String image_name, ProgressBar progressBar, ImageView imageView, int buffer_size) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("images/" + image_name);

        long BUFFER = buffer_size * (1024 * 1024);
        photoReference.getBytes(BUFFER).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(bmp);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error! " + e);
            progressBar.setVisibility(View.INVISIBLE);
        });

    }

}
