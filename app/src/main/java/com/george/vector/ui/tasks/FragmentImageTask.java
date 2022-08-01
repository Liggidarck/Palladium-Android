package com.george.vector.ui.tasks;

import static com.george.vector.common.utils.consts.Keys.COLLECTION;
import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.ID;
import static com.george.vector.common.utils.consts.Keys.LOCATION;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.databinding.FragmentImageTaskBinding;
import com.george.vector.network.viewmodel.TaskViewModel;
import com.george.vector.network.viewmodel.ViewModelFactory;

public class FragmentImageTask extends Fragment {

    private static final String TAG = "fragmentImageTask";

    FragmentImageTaskBinding binding;

    String image, id, collection, location, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImageTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        image = args.getString("image_id");
        id = args.getString(ID);
        collection = args.getString(COLLECTION);
        location = args.getString(LOCATION);
        email = args.getString(EMAIL);

        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.requireActivity().getApplication(),
                collection))
                .get(TaskViewModel.class);

        boolean economyTraffic = PreferenceManager
                .getDefaultSharedPreferences(FragmentImageTask.this.getContext())
                .getBoolean("economy_traffic", false);

        String buffer_size_preference = PreferenceManager
                .getDefaultSharedPreferences(FragmentImageTask.this.getContext())
                .getString("buffer_size", "2");
        Log.d(TAG, "buffer_size_preference: " + buffer_size_preference);

        int buffer_size = Integer.parseInt(buffer_size_preference);

        Log.d(TAG, "buffer_size: " + buffer_size);

        binding.rotateImageTaskRoot.setOnClickListener(v ->
                binding.imageRootTask
                        .animate()
                        .rotation(binding.imageRootTask.getRotation() + 90)
                        .setDuration(60));

        if (economyTraffic) {
            binding.downloadImageBtn.setVisibility(View.VISIBLE);
            binding.viewProgressIndicatorImage.setVisibility(View.INVISIBLE);
            binding.imageRootTask.setVisibility(View.INVISIBLE);
            binding.rotateImageTaskRoot.setVisibility(View.INVISIBLE);

            binding.downloadImageBtn.setOnClickListener(v -> {
                binding.downloadImageBtn.setVisibility(View.INVISIBLE);
                binding.viewProgressIndicatorImage.setVisibility(View.VISIBLE);
                binding.imageRootTask.setVisibility(View.VISIBLE);
                binding.rotateImageTaskRoot.setVisibility(View.VISIBLE);


                taskViewModel.setImage(image, binding.viewProgressIndicatorImage, binding.imageRootTask, buffer_size);
            });

        } else {
            binding.downloadImageBtn.setVisibility(View.INVISIBLE);

            taskViewModel.setImage(image, binding.viewProgressIndicatorImage, binding.imageRootTask, buffer_size);
        }

        binding.imageRootCard.setOnClickListener(v -> goActivityImage());

        return root;
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
