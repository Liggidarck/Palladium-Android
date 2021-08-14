package com.george.vector.users.root.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.george.vector.R;
import com.george.vector.common.bottom_sheets.ProfileBottomSheet;
import com.george.vector.common.bottom_sheets.ConsoleBottomSheet;
import com.george.vector.users.root.main.fragments.fragment_bar;
import com.george.vector.users.root.main.fragments.fragment_ost;
import com.george.vector.users.root.tasks.BottomSheetAddTask;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class RootMainActivity extends AppCompatActivity {

    Chip chip_root_ost, chip_root_bar;
    FloatingActionButton fab_add_root;
    BottomAppBar bottomAppBar_root;

    String zone;
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

        zone = PreferenceManager.getDefaultSharedPreferences(this).getString("default_root_location", getString(R.string.ost));

        setSupportActionBar(bottomAppBar_root);
        bottomAppBar_root.setNavigationOnClickListener(v -> {
            ConsoleBottomSheet bottomSheet = new ConsoleBottomSheet();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.permission), "all");
            bottomSheet.setArguments(bundle);

            bottomSheet.show(getSupportFragmentManager(), "ConsoleBottomSheet");
        });

        fab_add_root.setOnClickListener(v -> {
            BottomSheetAddTask bottomSheet = new BottomSheetAddTask();
            bottomSheet.show(getSupportFragmentManager(), "BottomSheetAddTask");
        });

        if(zone.equals("ost"))
            chip_root_ost.setChecked(true);

        if(zone.equals("bar"))
            chip_root_bar.setChecked(true);

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

        updateZones(zone);
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

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_main_root), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                            Log.i(TAG, "Update status: " + isOnline());
                            onStart();
                    }).show();
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

    @Override
    public void onBackPressed() {

    }
}