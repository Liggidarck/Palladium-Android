package com.george.vector.users.executor.main;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.OST;

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
import com.george.vector.users.executor.main.fragments.fragment_bar;
import com.george.vector.users.executor.main.fragments.fragment_ost;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class MainExecutorActivity extends AppCompatActivity {

    private static final String TAG = "ExecutorMain";
    BottomAppBar bottom_app_bar_worker;

    Chip chip_executor_ost, chip_executor_bar;
    String zone;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_executor);

        Bundle arguments = getIntent().getExtras();
        email = arguments.getString(EMAIL);
        zone = PreferenceManager.getDefaultSharedPreferences(this).getString("default_executor_location", OST);

        bottom_app_bar_worker = findViewById(R.id.bottomAppBarWorker);
        chip_executor_ost = findViewById(R.id.chip_executor_ost);
        chip_executor_bar = findViewById(R.id.chip_executor_bar);

        setSupportActionBar(bottom_app_bar_worker);
        bottom_app_bar_worker.setNavigationOnClickListener(v -> {
            SettingsExecutorBottomSheet bottomSheet = new SettingsExecutorBottomSheet();
            Bundle bundle = new Bundle();

            bundle.putString(EMAIL, email);
            bottomSheet.setArguments(bundle);

            bottomSheet.show(getSupportFragmentManager(), "SettingsExecutorBottomSheet");
        });

        if(zone.equals("ost"))
            chip_executor_ost.setChecked(true);

        if(zone.equals("bar"))
            chip_executor_bar.setChecked(true);

        chip_executor_ost.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });

        chip_executor_bar.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

                Bundle email = new Bundle();
                email.putString(EMAIL, this.email);
                currentFragment.setArguments(email);

                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new fragment_bar();

                Bundle email_bar = new Bundle();
                email_bar.putString(EMAIL, this.email);
                currentFragment.setArguments(email_bar);

                break;
        }
        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_executor, currentFragment)
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
            Snackbar.make(findViewById(R.id.coordinator_main_executor), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
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
        if(item.getItemId() == R.id.profile_item){
            ProfileBottomSheet bottomSheet = new ProfileBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ProfileBottomSheet");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "nope!");
    }
}