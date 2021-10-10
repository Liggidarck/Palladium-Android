package com.george.vector.common.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.GetDataTask;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ImageTaskActivity extends AppCompatActivity {

    private static final String TAG = "ImageTaskActivity";
    ImageView task_image_activity;
    MaterialToolbar topAppBar_task_activity;
    LinearProgressIndicator progress_bar_image_activity;

    String id, collection, location, image;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_task);

        firebaseFirestore = FirebaseFirestore.getInstance();

        task_image_activity = findViewById(R.id.task_image_activity);
        topAppBar_task_activity = findViewById(R.id.topAppBar_task_activity);
        progress_bar_image_activity = findViewById(R.id.progress_bar_image_activity);

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);

        topAppBar_task_activity.setNavigationOnClickListener(v -> onBackPressed());

        task_image_activity.setOnClickListener(v ->
                task_image_activity
                        .animate()
                        .rotation(task_image_activity.getRotation() + 90));

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            progress_bar_image_activity.setVisibility(View.VISIBLE);
            assert value != null;
            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                task_image_activity.setImageResource(R.drawable.no_image);
            } else {
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.setImage(image, progress_bar_image_activity, task_image_activity);
            }
        });
    }
}