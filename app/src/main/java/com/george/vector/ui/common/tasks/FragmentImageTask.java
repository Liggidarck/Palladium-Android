package com.george.vector.ui.common.tasks;

import static com.george.vector.common.utils.consts.Keys.ID;

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

import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.FragmentImageTaskBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class FragmentImageTask extends Fragment {

    private static final String TAG = "fragmentImageTask";

    private FragmentImageTaskBinding binding;

    private String image, id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImageTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        image = args.getString("image_id");
        id = args.getString(ID);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.requireActivity().getApplication(),
                userPrefViewModel.getToken())
        ).get(TaskViewModel.class);

        boolean economyTraffic = PreferenceManager
                .getDefaultSharedPreferences(FragmentImageTask.this.getContext())
                .getBoolean("economy_traffic", false);

        String bufferSizePreference = PreferenceManager
                .getDefaultSharedPreferences(FragmentImageTask.this.getContext())
                .getString("buffer_size", "2");
        Log.d(TAG, "buffer_size_preference: " + bufferSizePreference);

        int bufferSize = Integer.parseInt(bufferSizePreference);

        Log.d(TAG, "buffer_size: " + bufferSize);

        binding.btnRotateImage.setOnClickListener(v ->
                binding.imageViewTask
                        .animate()
                        .rotation(binding.imageViewTask.getRotation() + 90)
                        .setDuration(60));

        if (economyTraffic) {
            binding.btnDownload.setVisibility(View.VISIBLE);
            binding.progressBarImage.setVisibility(View.INVISIBLE);
            binding.imageViewTask.setVisibility(View.INVISIBLE);
            binding.btnRotateImage.setVisibility(View.INVISIBLE);

            binding.btnDownload.setOnClickListener(v -> {
                binding.btnDownload.setVisibility(View.INVISIBLE);
                binding.progressBarImage.setVisibility(View.VISIBLE);
                binding.imageViewTask.setVisibility(View.VISIBLE);
                binding.btnRotateImage.setVisibility(View.VISIBLE);
                taskViewModel.setImage(image, binding.progressBarImage, binding.imageViewTask, bufferSize);
            });
        } else {
            binding.btnDownload.setVisibility(View.INVISIBLE);
            taskViewModel.setImage(image, binding.progressBarImage, binding.imageViewTask, bufferSize);
        }

        binding.cardViewImage.setOnClickListener(v -> goActivityImage());

        return root;
    }

    void goActivityImage() {
        Intent intent = new Intent(FragmentImageTask.this.getContext(), ImageTaskActivity.class);
        intent.putExtra(ID, id);
        startActivity(intent);
    }

}
