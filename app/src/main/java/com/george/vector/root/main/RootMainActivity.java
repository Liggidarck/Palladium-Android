package com.george.vector.root.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.local_admin.AdminBottomSheet;
import com.george.vector.root.main.fragments.fragment_bar;
import com.george.vector.root.main.fragments.fragment_ost;
import com.george.vector.root.tasks.AddTaskRootActivity;
import com.george.vector.root.tasks.BottomSheetAddTask;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class RootMainActivity extends AppCompatActivity {

    Chip chip_root_ost, chip_root_bar;
    FloatingActionButton fab_add_root;
    BottomAppBar bottomAppBar_root;

    String zone = "ost";
    private static final String TAG = "RootMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_main);

        chip_root_ost = findViewById(R.id.chip_root_ost);
        chip_root_bar = findViewById(R.id.chip_root_bar);
        fab_add_root = findViewById(R.id.fab_add_root);
        bottomAppBar_root = findViewById(R.id.bottomAppBar_root);

        setSupportActionBar(bottomAppBar_root);

        fab_add_root.setOnClickListener(v -> {
            BottomSheetAddTask bottomSheet = new BottomSheetAddTask();
            bottomSheet.show(getSupportFragmentManager(), "BottomSheetAddTask");
        });

        chip_root_ost.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });
        chip_root_bar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "Барыши checked");
                zone = "bar";
                updateZones(zone);
            }
        });

        updateZones("ost");
    }

    void updateZones(@NotNull String zone_update) {
        Fragment currentFragment = null;
        switch (zone_update) {
            case "ost":
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new fragment_ost();
                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new fragment_bar();
                break;
        }
        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_root, currentFragment)
                .commit();
    }

}