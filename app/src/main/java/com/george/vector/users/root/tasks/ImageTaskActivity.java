package com.george.vector.users.root.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.george.vector.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageTaskActivity extends AppCompatActivity {

    private static final String TAG = "ImageTaskActivity";
    ImageView task_image_activity;
    String id, collection, location, USER_EMAIL, image;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_task);


        firebaseFirestore = FirebaseFirestore.getInstance();

        task_image_activity = findViewById(R.id.task_image_activity);

        Bundle arguments = getIntent().getExtras();
        id = arguments.get(getString(R.string.id)).toString();
        collection = arguments.get(getString(R.string.collection)).toString();
        location = arguments.get(getString(R.string.location)).toString();

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;
            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                task_image_activity.setImageResource(R.drawable.no_image);
            } else {

                String IMAGE_URL = String.format("https://firebasestorage.googleapis.com/v0/b/school-2122.appspot.com/o/images%%2F%s?alt=media", image);
                Picasso.with(this)
                        .load(IMAGE_URL)
                        .into(task_image_activity, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Log.e(TAG, "Error on download");
                            }
                        });
            }
        });
    }
}