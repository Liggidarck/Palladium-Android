package com.george.vector.common.tasks.images;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.GetDataTask;
import com.google.android.material.card.MaterialCardView;

public class FragmentImageTask extends Fragment {

    private static final String TAG = "fragmentImageTask";
    Button rotate_image_task_root, download_image_btn;
    ImageView image_root_task;
    MaterialCardView image_root_card;
    ProgressBar view_progress_indicator_image;
    View error_view;

    String image, id, collection, location, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_task, container, false);

        rotate_image_task_root = view.findViewById(R.id.rotate_image_task_root);
        image_root_task = view.findViewById(R.id.image_root_task);
        image_root_card = view.findViewById(R.id.image_root_card);
        view_progress_indicator_image = view.findViewById(R.id.view_progress_indicator_image);
        download_image_btn = view.findViewById(R.id.download_image_btn);
        error_view = view.findViewById(R.id.error_view);

        Bundle args = getArguments();
        assert args != null;
        image = args.getString("image_id");
        id = args.getString(ID);
        collection = args.getString(COLLECTION);
        location = args.getString(LOCATION);
        email = args.getString(EMAIL);

        boolean economy_traffic = PreferenceManager
                .getDefaultSharedPreferences(FragmentImageTask.this.getContext())
                .getBoolean("economy_traffic", false);

        String buffer_size_preference = PreferenceManager
                .getDefaultSharedPreferences(FragmentImageTask.this.getContext())
                .getString("buffer_size", "2");
        Log.d(TAG, "buffer_size_preference: " + buffer_size_preference);

        int buffer_size = Integer.parseInt(buffer_size_preference);

        Log.d(TAG, "buffer_size: " + buffer_size);

        rotate_image_task_root.setOnClickListener(v ->
                image_root_task
                        .animate()
                        .rotation(image_root_task.getRotation() + 90)
                        .setDuration(60));

        if (economy_traffic) {
            download_image_btn.setVisibility(View.VISIBLE);
            view_progress_indicator_image.setVisibility(View.INVISIBLE);
            image_root_task.setVisibility(View.INVISIBLE);
            rotate_image_task_root.setVisibility(View.INVISIBLE);

            download_image_btn.setOnClickListener(v -> {
                download_image_btn.setVisibility(View.INVISIBLE);
                view_progress_indicator_image.setVisibility(View.VISIBLE);
                image_root_task.setVisibility(View.VISIBLE);
                rotate_image_task_root.setVisibility(View.VISIBLE);

                GetDataTask getDataTask = new GetDataTask();
                getDataTask.setImageFuture(image, view_progress_indicator_image, image_root_task, buffer_size);
            });

        } else {
            download_image_btn.setVisibility(View.INVISIBLE);
            GetDataTask getDataTask = new GetDataTask();
            getDataTask.setImageFuture(image, view_progress_indicator_image, image_root_task, buffer_size);
        }

        image_root_card.setOnClickListener(v -> goActivityImage());

        return view;
    }

    void goActivityImage() {
        Intent intent = new Intent(FragmentImageTask.this.getContext(), ImageTaskActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(COLLECTION, collection);
        intent.putExtra(LOCATION, location);
        intent.putExtra(EMAIL, email);
        startActivity(intent);
    }

}
