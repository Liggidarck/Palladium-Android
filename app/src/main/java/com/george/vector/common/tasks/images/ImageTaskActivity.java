package com.george.vector.common.tasks.images;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.GetDataTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ImageTaskActivity extends AppCompatActivity {

    private static final String TAG = "ImageTaskActivity";
    ImageView task_image_activity;
    MaterialToolbar top_app_bar_task_activity;
    LinearProgressIndicator progress_bar_image_activity;

    String id, collection, location, image;

    FirebaseFirestore firebase_firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_task);

        firebase_firestore = FirebaseFirestore.getInstance();

        task_image_activity = findViewById(R.id.task_image_activity);
        top_app_bar_task_activity = findViewById(R.id.topAppBar_task_activity);
        progress_bar_image_activity = findViewById(R.id.progress_bar_image_activity);

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);

        String buffer_size_preference = PreferenceManager
                .getDefaultSharedPreferences(ImageTaskActivity.this)
                .getString("buffer_size", "2");
        Log.d(TAG, "buffer_size_preference: " + buffer_size_preference);

        int buffer_size = Integer.parseInt(buffer_size_preference);

        top_app_bar_task_activity.setNavigationOnClickListener(v -> onBackPressed());

        task_image_activity.setOnClickListener(v ->
                task_image_activity
                        .animate()
                        .rotation(task_image_activity.getRotation() + 90)
                        .setDuration(60));

        DocumentReference documentReference = firebase_firestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_image_activity.setVisibility(View.VISIBLE);
            assert value != null;
            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                task_image_activity.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            } else {
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.setImage(image, progress_bar_image_activity, task_image_activity, buffer_size);
            }
        });
    }
}