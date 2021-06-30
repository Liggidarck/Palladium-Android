package com.george.vector.root.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.george.vector.R;
import com.george.vector.common.bottom_sheets.ProfileBottomSheet;
import com.george.vector.common.bottom_sheets.ConsoleBottomSheet;
import com.george.vector.root.main.fragments.fragment_bar;
import com.george.vector.root.main.fragments.fragment_ost;
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

        bottomAppBar_root.setNavigationOnClickListener(v -> {
            ConsoleBottomSheet bottomSheet = new ConsoleBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ConsoleBottomSheet");
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile_item) {
            ProfileBottomSheet bottomSheet = new ProfileBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ProfileBottomSheet");
        }

        return super.onOptionsItemSelected(item);
    }
}