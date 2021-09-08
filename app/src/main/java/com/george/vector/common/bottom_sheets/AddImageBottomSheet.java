package com.george.vector.common.bottom_sheets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.george.vector.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddImageBottomSheet extends BottomSheetDialogFragment {

    private final int PICK_IMAGE_REQUEST = 71;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_image_bottom_sheet, container, false);

        RecyclerView layout_new_photo = view.findViewById(R.id.layout_new_photo);
        RecyclerView layout_folder = view.findViewById(R.id.layout_folder);

        layout_folder.setOnClickListener(v -> {
            chooseImage();
        });

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}
