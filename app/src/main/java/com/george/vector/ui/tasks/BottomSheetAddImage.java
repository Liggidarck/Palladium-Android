package com.george.vector.ui.tasks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.george.vector.databinding.BottomSheetAddImageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetAddImage extends BottomSheetDialogFragment {

    BottomSheetAddImageBinding binding;

    StateListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAddImageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.layoutNewPhotoBottomSheet.setOnClickListener(v -> listener.getPhotoFromDevice("new photo"));

        binding.layoutFolderBottomSheet.setOnClickListener(v -> listener.getPhotoFromDevice("existing photo"));

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface StateListener {
        void getPhotoFromDevice(String button);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (StateListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

}
