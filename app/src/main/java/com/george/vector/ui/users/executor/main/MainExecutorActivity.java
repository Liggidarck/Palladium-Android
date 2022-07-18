package com.george.vector.ui.users.executor.main;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.OST;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Logs.TAG_MAIN_EXECUTOR_ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.george.vector.R;
import com.george.vector.databinding.ActivityMainExecutorBinding;
import com.george.vector.ui.users.executor.main.fragments.FragmentBarExecutor;
import com.george.vector.ui.users.executor.main.fragments.FragmentOstExecutor;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class MainExecutorActivity extends AppCompatActivity {

    String zone, email;
    ActivityMainExecutorBinding executorBinding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        executorBinding = ActivityMainExecutorBinding.inflate(getLayoutInflater());
        setContentView(executorBinding.getRoot());

        sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);

        email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");
        zone = PreferenceManager.getDefaultSharedPreferences(this).getString("default_executor_location", OST);

        executorBinding.technicalSupportExecutor.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "georgyfilatov@yandex.ru", null));
            intent.putExtra("android.intent.extra.SUBJECT", "Помощь с приложением");
            startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));
        });

        setSupportActionBar(executorBinding.bottomAppBarWorker);
        executorBinding.bottomAppBarWorker.setNavigationOnClickListener(v -> {
            SettingsExecutorBottomSheet bottomSheet = new SettingsExecutorBottomSheet();
            Bundle bundle = new Bundle();

            bundle.putString(EMAIL, email);
            bottomSheet.setArguments(bundle);

            bottomSheet.show(getSupportFragmentManager(), "SettingsExecutorBottomSheet");
        });

        if(zone.equals("ost"))
            executorBinding.chipExecutorOst.setChecked(true);

        if(zone.equals("bar"))
            executorBinding.chipExecutorBar.setChecked(true);

        executorBinding.chipExecutorOst.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG_MAIN_EXECUTOR_ACTIVITY, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });

        executorBinding.chipExecutorBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.i(TAG_MAIN_EXECUTOR_ACTIVITY, "Барыши checked");
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
                Log.i(TAG_MAIN_EXECUTOR_ACTIVITY, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentOstExecutor();

                Bundle email = new Bundle();
                email.putString(EMAIL, this.email);
                currentFragment.setArguments(email);

                break;
            case "bar":
                Log.i(TAG_MAIN_EXECUTOR_ACTIVITY, "Запуск фрагмента Барыши");
                currentFragment = new FragmentBarExecutor();

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
                        Log.i(TAG_MAIN_EXECUTOR_ACTIVITY, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_executor_navigation, menu);
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
}