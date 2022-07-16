package com.george.vector.ui.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.databinding.ActivityImageTaskBinding;
import com.george.vector.network.utilsLegacy.GetDataTask;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ImageTaskActivity extends AppCompatActivity {

    String id, collection, location, image;

    ActivityImageTaskBinding binding;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "ImageTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle arguments = getIntent().getExtras();
        id = arguments.getString(ID);
        collection = arguments.getString(COLLECTION);
        location = arguments.getString(LOCATION);

        String buffer_size_preference = PreferenceManager
                .getDefaultSharedPreferences(ImageTaskActivity.this)
                .getString("buffer_size", "2");
        Log.d(TAG, "buffer_size_preference: " + buffer_size_preference);

        int buffer_size = Integer.parseInt(buffer_size_preference);

        binding.topAppBarTaskActivity.setNavigationOnClickListener(v -> onBackPressed());

        binding.taskImageActivity.setOnClickListener(v ->
                binding.taskImageActivity
                        .animate()
                        .rotation(binding.taskImageActivity.getRotation() + 90)
                        .setDuration(60));

        DocumentReference documentReference = firebaseFirestore.collection(collection).document(id);
        documentReference.addSnapshotListener(this, (value, error) -> {
            binding.progressBarImageActivity.setVisibility(View.VISIBLE);
            assert value != null;
            image = value.getString("image");

            if (image == null) {
                Log.d(TAG, "No image, stop loading");
                binding.taskImageActivity.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            } else {
                GetDataTask getDataTask = new GetDataTask();
                getDataTask.setImage(image, binding.progressBarImageActivity, binding.taskImageActivity, buffer_size);
            }
        });
    }
}