package com.george.vector.common.tasks;

import static com.george.vector.common.consts.Keys.COLLECTION;
import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.ID;
import static com.george.vector.common.consts.Keys.LOCATION;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.common.tasks.utils.GetDataTask;
import com.google.android.material.card.MaterialCardView;

public class fragmentImageTask extends Fragment {

    Button rotate_image_task_root;
    ImageView image_root_task;
    MaterialCardView image_root_card;
    ProgressBar view_progress_indicator_image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_task, container, false);

        rotate_image_task_root = view.findViewById(R.id.rotate_image_task_root);
        image_root_task = view.findViewById(R.id.image_root_task);
        image_root_card = view.findViewById(R.id.image_root_card);
        view_progress_indicator_image = view.findViewById(R.id.view_progress_indicator_image);

        Bundle args = getArguments();
        assert args != null;
        String image = args.getString("image_id");
        String id = args.getString(ID);
        String collection = args.getString(COLLECTION);
        String location = args.getString(LOCATION);
        String email = args.getString(EMAIL);

        rotate_image_task_root.setOnClickListener(v ->
                image_root_task
                        .animate()
                        .rotation(image_root_task.getRotation() + 90));

        GetDataTask getDataTask = new GetDataTask();
        getDataTask.setImageFuture(image, view_progress_indicator_image, image_root_task);

        image_root_card.setOnClickListener(v -> {
            Intent intent = new Intent(fragmentImageTask.this.getContext(), ImageTaskActivity.class);
            intent.putExtra(ID, id);
            intent.putExtra(COLLECTION, collection);
            intent.putExtra(LOCATION, location);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        return view;
    }
}
