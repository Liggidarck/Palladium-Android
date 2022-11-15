package com.george.vector.data.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.george.vector.databinding.ActivityInitializeBinding;

public class InitializeActivity extends AppCompatActivity {

    private ActivityInitializeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitializeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}