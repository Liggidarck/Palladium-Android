package com.george.vector.users.caretaker.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

        bottomAppBar_caretaker.setNavigationOnClickListener(v -> {
            ConsoleBottomSheet bottomSheet = new ConsoleBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ConsoleBottomSheet");
        });

        Bundle arguments = getIntent().getExtras();
        String permission = arguments.get((String) getText(R.string.permission)).toString();

        if(permission.contentEquals(getText(R.string.ost)))
            setUp((String) getText(R.string.ost));

        if(permission.contentEquals(getText(R.string.bar)))
            setUp((String) getText(R.string.bar));

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

    @Override
    public void onBackPressed() {

    }
}