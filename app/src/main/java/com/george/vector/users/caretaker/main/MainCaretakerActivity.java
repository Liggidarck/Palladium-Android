package com.george.vector.users.caretaker.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.george.vector.R;
import com.george.vector.users.caretaker.main.fragments.fragment_bar_caretaker;
import com.george.vector.users.caretaker.main.fragments.fragment_ost_caretaker;
import com.george.vector.users.caretaker.tasks.bottom_sheets.BottomSheetAddTaskBar;
import com.george.vector.users.caretaker.tasks.bottom_sheets.BottomSheetAddTaskOst;
import com.george.vector.common.bottom_sheets.ProfileBottomSheet;
import com.george.vector.common.bottom_sheets.ConsoleBottomSheet;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class MainCaretakerActivity extends AppCompatActivity {

    private static final String TAG = "CaretakerMain";
    FloatingActionButton fab_add_caretaker;
    BottomAppBar bottomAppBar_caretaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_main);

        fab_add_caretaker = findViewById(R.id.fab_add_caretaker);
        bottomAppBar_caretaker = findViewById(R.id.bottomAppBar_caretaker);

        setSupportActionBar(bottomAppBar_caretaker);

        Bundle arguments = getIntent().getExtras();
        String permission = arguments.get(getString(R.string.permission)).toString();

        bottomAppBar_caretaker.setNavigationOnClickListener(v -> {
            ConsoleBottomSheet bottomSheet = new ConsoleBottomSheet();
            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.permission), permission);
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), "ConsoleBottomSheet");
        });


        if(permission.equals(getString(R.string.ost)))
            setUp(getString(R.string.ost));

        if(permission.equals(getString(R.string.bar)))
            setUp(getString(R.string.bar));

        fab_add_caretaker.setOnClickListener(v -> {
            switch (permission) {
                case "ost":
                    BottomSheetAddTaskOst bottomSheetAddTaskOst = new BottomSheetAddTaskOst();
                    bottomSheetAddTaskOst.show(getSupportFragmentManager(),"bottomSheetAddTask");
                    break;
                case "bar":
                    BottomSheetAddTaskBar bottomSheetAddTaskBar = new BottomSheetAddTaskBar();
                    bottomSheetAddTaskBar.show(getSupportFragmentManager(),"bottomSheetAddTaskBar");
                    break;
            }
        });
    }

    void setUp(@NotNull String permission) {
        Fragment currentFragment = null;
        switch (permission) {
            case "ost":
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new fragment_ost_caretaker();
                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new fragment_bar_caretaker();
                break;
        }
        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_caretaker, currentFragment)
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

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isOnline())
            Snackbar.make(findViewById(R.id.coordinator_main_caretaker), getString(R.string.error_no_connection), Snackbar.LENGTH_LONG)
                    .setAction("Повторить", v ->  {
                        Log.i(TAG, "Update status: " + isOnline());
                        onStart();
                    }).show();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "nope!");
    }
}